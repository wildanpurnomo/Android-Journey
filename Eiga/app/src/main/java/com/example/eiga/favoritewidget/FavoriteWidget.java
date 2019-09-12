package com.example.eiga.favoritewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.eiga.R;
import com.example.eiga.ui.detail.DetailActivity;

import static android.support.constraint.Constraints.TAG;
import static com.example.eiga.db.DatabaseContract.FavoriteColumns.CONTENT_URI;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteWidget extends AppWidgetProvider {
    private static final String DETAIL_ACTION = "com.example.eiga.DETAIL_ACTION";
    public static final String EXTRA_ITEM = "com.example.eiga.EXTRA_ITEM";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_widget);
        views.setRemoteAdapter(R.id.stack_favorite_in_widget, intent);
        views.setEmptyView(R.id.stack_favorite_in_widget, R.id.tv_empty_view_in_widget);

        Intent toDetailIntent = new Intent(context, FavoriteWidget.class);
        toDetailIntent.setAction(DETAIL_ACTION);
        toDetailIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        toDetailIntent.setData(Uri.parse(toDetailIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toDetailPendingIntent = PendingIntent.getBroadcast(context, 0, toDetailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_favorite_in_widget, toDetailPendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String message = intent.getAction();

        if (message != null) {
            if (message.equals(EXTRA_ITEM)) {
                Log.d(TAG, "onReceive: EXTRA_ITEM");

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(new ComponentName(context, FavoriteWidget.class)), R.id.stack_favorite_in_widget);
            } else if (message.equals(DETAIL_ACTION)) {
                int index = intent.getIntExtra("widgetIndexHelper", 0);
                long id = intent.getLongExtra("widgetIdHelper", 0);
                Uri uri = Uri.parse(CONTENT_URI + "/" + id);

                Intent toDetail = new Intent(context, DetailActivity.class);
                toDetail.setData(uri);
                toDetail.putExtra(DetailActivity.EXTRA_ID, id);
                toDetail.putExtra(DetailActivity.EXTRA_INDEX, index);
                toDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(toDetail);
            }
        }

        super.onReceive(context, intent);
    }

    public static void refreshWidget(Context context) {
        Intent refreshIntent = new Intent(context, FavoriteWidget.class);
        refreshIntent.setAction(EXTRA_ITEM);
        refreshIntent.setComponent(new ComponentName(context, FavoriteWidget.class));
        context.sendBroadcast(refreshIntent);
    }
}

