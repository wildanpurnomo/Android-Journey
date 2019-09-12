package com.example.simpleconsumerapp;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.simpleconsumerapp.Model.Show;
import com.example.simpleconsumerapp.adapter.ConsumerAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.simpleconsumerapp.db.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.example.simpleconsumerapp.helper.MappingHelper.mapCursorToArrayList;

public class MainActivity extends AppCompatActivity implements LoadFavoritesCallback {
    private static final String EXTRA_STATE = "extra_state";

    private ConsumerAdapter consumerAdapter;
    private MainActivity.DataObserver dataObserver;

    private RecyclerView rvFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvFavorite = findViewById(R.id.rv_list_fav);

        consumerAdapter = new ConsumerAdapter(this);
        rvFavorite.setLayoutManager(new LinearLayoutManager(this));
        rvFavorite.setHasFixedSize(true);
        rvFavorite.setAdapter(consumerAdapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        dataObserver = new DataObserver(handler, this);

        if (savedInstanceState == null) {
            new LoadFavoritesAsync(this, this).execute();
        } else {
            ArrayList<Show> savedList = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            consumerAdapter.setListShows(savedList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dataObserver != null && getContentResolver() != null) {
            getContentResolver().registerContentObserver(CONTENT_URI, true, dataObserver);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dataObserver != null && getContentResolver() != null) {
            getContentResolver().unregisterContentObserver(dataObserver);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, consumerAdapter.getListShows());
    }

    @Override
    public void postExecute(Cursor shows) {
        ArrayList<Show> convertedList = mapCursorToArrayList(shows);
        consumerAdapter.setListShows(convertedList);
    }

    private static class LoadFavoritesAsync extends AsyncTask<Void, Void, Cursor> {
        final WeakReference<Context> weakContext;
        final WeakReference<LoadFavoritesCallback> weakCallback;

        public LoadFavoritesAsync(Context context, LoadFavoritesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
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
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }
    }

    private static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadFavoritesAsync(context, (LoadFavoritesCallback) context);
        }
    }
}
