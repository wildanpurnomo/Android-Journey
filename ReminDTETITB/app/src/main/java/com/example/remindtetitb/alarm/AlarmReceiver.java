package com.example.remindtetitb.alarm;

import android.app.AlarmManager;
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
import android.widget.Toast;

import com.example.remindtetitb.R;
import com.example.remindtetitb.helper.SharedPrefManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String EXTRA_INFO_ID = "ExtraInfoID";
    private static final String EXTRA_NOTIF_ID = "ExtraNotifID";
    private static final String EXTRA_TITLE = "ExtraTitle";
    private static final String EXTRA_CONTENT = "ExtraContent";

    @Override
    public void onReceive(Context context, Intent intent) {
        String infoId = intent.getStringExtra(EXTRA_INFO_ID);
        int notifId = intent.getIntExtra(EXTRA_NOTIF_ID, 0);
        String title = intent.getStringExtra(EXTRA_TITLE);
        String content = intent.getStringExtra(EXTRA_CONTENT);
        showAlarmNotification(context, infoId, notifId, title, content);
    }

    private void showAlarmNotification(Context context, String infoId, int notifId, String title, String content){
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_onboarding)
                .setContentTitle(title)
                .setContentText(content)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
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
            notificationManager.notify(notifId, notification);
            sharedPrefManager.deleteAlarmSchedule(infoId);
        }

    }

    public void setOneTimeAlarm(Context context, String infoId, int notifId, String date, String time, String title, String content){
        String DATE_FORMAT = "yyyy-MM-dd";
        String TIME_FORMAT = "HH:mm";
        if(isDateInvalid(date, DATE_FORMAT) || isDateInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_INFO_ID, infoId);
        intent.putExtra(EXTRA_NOTIF_ID, notifId);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_CONTENT, content);

        String[] dateArray = date.split("-");
        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notifId, intent, 0);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        Toast.makeText(context, "Pengingat dipasang", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context, int notifId){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notifId, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, "Pengingat dibatalkan", Toast.LENGTH_SHORT).show();
    }

    public boolean isDateInvalid(String date, String format){
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (Exception e){
            return true;
        }
    }
}
