package com.example.eiga.ui.notificationsettings;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.eiga.MainActivity;
import com.example.eiga.R;

import java.util.Calendar;

import static com.example.eiga.ui.notificationsettings.AlarmReceiver.EXTRA_TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationSettings extends Fragment implements Switch.OnCheckedChangeListener {
    private static final String PREFERENCE_DAILY = "com.example.eiga.preference_daily";
    private static final String PREFERENCE_RELEASE = "com.example.eiga.preference_release";

    private SharedPreferences sharedPreferences;

    private AlarmReceiver alarmReceiver;
    private AlarmManager dailyAlarmManager, releaseAlarmManager;
    private PendingIntent dailyPendingIntent, releasePendingIntent;

    public NotificationSettings() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_notification_settings, container, false);
        Switch switchDailyReminder = root.findViewById(R.id.switch_notif_daily_reminder);
        Switch switchReleaseReminder = root.findViewById(R.id.switch_notif_release_reminder);

        alarmReceiver = new AlarmReceiver();

        if (getActivity() != null) {
            sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            switchDailyReminder.setChecked(sharedPreferences.getBoolean(PREFERENCE_DAILY, false));
            switchReleaseReminder.setChecked(sharedPreferences.getBoolean(PREFERENCE_RELEASE, false));

            dailyAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            releaseAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            Intent dailyNotifIntent = new Intent(getContext(), AlarmReceiver.class);
            dailyNotifIntent.putExtra(EXTRA_TYPE, AlarmReceiver.TYPE_DAILY);
            dailyPendingIntent = PendingIntent.getBroadcast(getContext(), AlarmReceiver.ID_DAILY, dailyNotifIntent, 0);

            Intent releaseNotifIntent = new Intent(getContext(), ReleaseService.class);
            releasePendingIntent = PendingIntent.getService(getContext(), 0, releaseNotifIntent, 0);
        }

        switchDailyReminder.setOnCheckedChangeListener(this);
        switchReleaseReminder.setOnCheckedChangeListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getContext() != null) {
            getContext().registerReceiver(alarmReceiver, new IntentFilter(MainActivity.ACTION_GET_RELEASE));
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (getContext() != null) {
            getContext().unregisterReceiver(alarmReceiver);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!buttonView.isPressed()) {
            return;
        }

        switch (buttonView.getId()) {
            case R.id.switch_notif_daily_reminder:
                if (isChecked) {
                    setDailyReminder();
                } else {
                    stopDailyReminder();
                }

                writeToSharedPref(PREFERENCE_DAILY, isChecked);

                break;

            case R.id.switch_notif_release_reminder:
                if (isChecked) {
                    setReleaseReminder();
                } else {
                    stopReleaseReminder();
                }

                writeToSharedPref(PREFERENCE_RELEASE, isChecked);

                break;
        }
    }

    private void setDailyReminder() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (dailyAlarmManager != null) {
            dailyAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, dailyPendingIntent);

            if (getContext() != null) {
                Toast.makeText(getContext(), R.string.daily_reminder_setup_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void stopDailyReminder() {
        if (dailyAlarmManager != null) {
            dailyAlarmManager.cancel(dailyPendingIntent);

            if (getContext() != null) {
                Toast.makeText(getContext(), R.string.daily_reminder_off_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setReleaseReminder() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (releaseAlarmManager != null) {
            releaseAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, releasePendingIntent);
        }

        if (getContext() != null) {
            Toast.makeText(getContext(), R.string.release_reminder_setup_msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void stopReleaseReminder() {
        if (releaseAlarmManager != null) {
            releaseAlarmManager.cancel(releasePendingIntent);
            Toast.makeText(getContext(), R.string.release_reminder_off_msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void writeToSharedPref(String key, boolean isChecked) {
        if (getActivity() != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            switch (key) {
                case PREFERENCE_DAILY:
                    editor.putBoolean(PREFERENCE_DAILY, isChecked);
                    editor.apply();
                    break;

                case PREFERENCE_RELEASE:
                    editor.putBoolean(PREFERENCE_RELEASE, isChecked);
                    editor.apply();
                    break;
            }
        }
    }
}
