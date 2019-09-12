package com.example.eiga.ui.notificationsettings;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

import com.example.eiga.MainActivity;
import com.example.eiga.R;
import com.example.eiga.model.Show;
import com.example.eiga.ui.detail.DetailActivity;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TYPE_DAILY = "dailyReminder";
    public static final String TYPE_RELEASE = "releaseReminder";
    public static final String EXTRA_TYPE = "extraType";
    public static final String EXTRA_RELEASED_LIST = "extraReleased";

    public static final int ID_DAILY = 100;

    public AlarmReceiver() {

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);

        if (type.equals(TYPE_DAILY)) {
            sendDailyNotification(context);
        } else {
            ArrayList<Show> releasedResults = intent.getParcelableArrayListExtra(EXTRA_RELEASED_LIST);
            sendReleaseNotification(context, releasedResults);
        }
    }

    private void sendDailyNotification(Context context) {
        String CHANNEL_ID = "Channel_Daily";
        String CHANNEL_NAME = "AlarmManager channel daily";

        String title = context.getResources().getString(R.string.daily_notif_title);
        String message = context.getResources().getString(R.string.daily_notif_message);

        Intent onClickIntent = new Intent(context, MainActivity.class);
        onClickIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent onClickPendingIntent = PendingIntent.getActivity(context, MainActivity.NOTIF_REQUEST_CODE, onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(onClickPendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManager != null) {
            notificationManager.notify(ID_DAILY, notification);
        }
    }

    private void sendReleaseNotification(final Context context, ArrayList<Show> releasedResults) {
        String CHANNEL_ID = "Channel_Release";
        String CHANNEL_NAME = "AlarmManager channel Release";

        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.tv_notif_header, context.getResources().getString(R.string.release_reminder_title));

        int indexHelper = 0;
        for (final Show item : releasedResults) {
            remoteViews.setTextViewText(R.id.tv_notif_title, item.getTitle());

            Intent onClickIntent = new Intent(context, DetailActivity.class);
            onClickIntent.putExtra(DetailActivity.EXTRA_INDEX, 0);
            onClickIntent.putExtra(DetailActivity.EXTRA_ID, item.getId());
            onClickIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent onClickPendingIntent = PendingIntent.getActivity(context, indexHelper, onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentIntent(onClickPendingIntent)
                    .setSmallIcon(R.drawable.ic_local_movies_white_24dp)
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setContent(remoteViews)
                    .setSound(alarmSound)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                if (builder != null) {
                    builder.setChannelId(CHANNEL_ID);
                }

                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(notificationChannel);
                }
            }

            if (builder != null) {
                Notification notification = builder.build();

                if (notificationManager != null) {
                    notificationManager.notify(indexHelper, notification);
                }
            }
            indexHelper++;
        }
    }
}
