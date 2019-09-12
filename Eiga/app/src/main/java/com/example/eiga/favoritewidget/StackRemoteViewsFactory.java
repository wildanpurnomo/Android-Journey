package com.example.eiga.favoritewidget;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.eiga.R;
import com.example.eiga.model.Show;
import com.example.eiga.ui.favorite.LoadFavoriteCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.support.constraint.Constraints.TAG;
import static com.example.eiga.db.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.example.eiga.helper.MappingHelper.mapCursorToArrayList;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory, LoadFavoriteCallback {
    private final ArrayList<Show> shows = new ArrayList<>();
    private final ArrayList<String> imgPosterPath = new ArrayList<>();
    private final Context context;

    private WidgetObserver widgetObserver;

    public StackRemoteViewsFactory(Context context) {
        Log.d(TAG, "StackRemoteViewsFactory: dijalankan");
        this.context = context;

        HandlerThread handlerThread = new HandlerThread("WidgetObserver");
        handlerThread.start();

        Handler handler = new Handler(handlerThread.getLooper());
        widgetObserver = new WidgetObserver(handler, context, this);
        context.getContentResolver().registerContentObserver(CONTENT_URI, true, widgetObserver);

        new LoadImageAsync(context, this).execute();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: dijalankan");
        context.getContentResolver().unregisterContentObserver(widgetObserver);
    }

    @Override
    public int getCount() {
        return imgPosterPath.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(TAG, "getViewAt: dijalankan");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        if (!imgPosterPath.isEmpty() && !shows.isEmpty()) {
            try {
                Bitmap bitmap = Glide.with(context)
                        .asBitmap()
                        .load("https://image.tmdb.org/t/p/w500" + imgPosterPath.get(position))
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .submit(110, 250)
                        .get();
                remoteViews.setImageViewBitmap(R.id.image_item_in_widget, bitmap);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int indexHelper = shows.get(position).getType().equals("movie") ? 0 : 1;

            Bundle extras = new Bundle();
            extras.putInt("widgetIndexHelper", indexHelper);
            extras.putLong("widgetIdHelper", shows.get(position).getId());

            Intent intent = new Intent();
            intent.putExtras(extras);

            remoteViews.setOnClickFillInIntent(R.id.image_item_in_widget, intent);
        }
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(Cursor shows) {
        Log.d(TAG, "postExecute: dijalankan");
        if (imgPosterPath.size() != 0) {
            imgPosterPath.clear();
            this.shows.clear();
        }

        ArrayList<Show> receivedShows = mapCursorToArrayList(shows);
        for (Show item : receivedShows) {
            imgPosterPath.add(item.getPosterPath());
        }

        this.shows.addAll(receivedShows);
    }

    private static class LoadImageAsync extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavoriteCallback> weakCallback;

        public LoadImageAsync(Context weakContext, LoadFavoriteCallback weakCallback) {
            this.weakContext = new WeakReference<>(weakContext);
            this.weakCallback = new WeakReference<>(weakCallback);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return weakContext.get().getContentResolver().query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            weakCallback.get().preExecute();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            weakCallback.get().postExecute(cursor);
        }
    }

    private static class WidgetObserver extends ContentObserver {
        final Context context;
        final LoadFavoriteCallback callback;

        public WidgetObserver(Handler handler, Context context, LoadFavoriteCallback callback) {
            super(handler);
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadImageAsync(context, callback).execute();
        }
    }
}
