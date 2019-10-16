package com.example.remindtetitb.ui.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.remindtetitb.R;
import com.example.remindtetitb.alarm.AlarmReceiver;
import com.example.remindtetitb.helper.SharedPrefManager;
import com.example.remindtetitb.model.Info;
import com.example.remindtetitb.ui.fragments.DatePickerFragment;
import com.example.remindtetitb.ui.fragments.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {
    public static final String EXTRA_INFO = "extra_info";

    private TextView tvDetailTitle, tvDetailContent, tvDetailDateAlarm, tvDetailHourAlarm;
    private Button btnCancelAlarm;
    private AlarmReceiver alarmReceiver;
    private Info info = new Info();

    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvDetailTitle = findViewById(R.id.tv_detail_title);
        tvDetailContent = findViewById(R.id.tv_detail_content);
        tvDetailDateAlarm = findViewById(R.id.tv_detail_alarm_date_label);
        tvDetailHourAlarm = findViewById(R.id.tv_detail_alarm_hour_label);
        ImageButton imgBtnDateAlarm = findViewById(R.id.imgbtn_detail_alarm_date);
        imgBtnDateAlarm.setOnClickListener(this);
        ImageButton imgBtnHourAlarm = findViewById(R.id.imgbtn_detail_alarm_hour);
        imgBtnHourAlarm.setOnClickListener(this);
        Button btnSetAlarm = findViewById(R.id.btn_detail_set_alarm);
        btnSetAlarm.setOnClickListener(this);
        btnCancelAlarm = findViewById(R.id.btn_detail_cancel_alarm);
        btnCancelAlarm.setOnClickListener(this);
        Button btnDetailLabel = findViewById(R.id.btn_detail_kategori);
        alarmReceiver = new AlarmReceiver();
        sharedPrefManager = new SharedPrefManager(this);

        info = getIntent().getParcelableExtra(EXTRA_INFO);
        tvDetailTitle.setText(info.getTitle());
        tvDetailContent.setText(info.getContent());
        btnDetailLabel.setBackground(getDrawable(info.getLabel().equals("Perkuliahan") ? R.drawable.bg_tag_kuliah : R.drawable.bg_tag_akademik));
        btnDetailLabel.setText(info.getLabel().equals("Perkuliahan") ? "Kuliah" : "Akademik");

        if (sharedPrefManager.getAlarmSchedule(info.getId()) != null) {
            String datetime = sharedPrefManager.getAlarmSchedule(info.getId());
            String[] splitter = datetime.split("_");
            tvDetailDateAlarm.setText(splitter[0]);
            tvDetailHourAlarm.setText(splitter[1]);
            btnCancelAlarm.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        String DATE_PICKER_TAG = "DatePicker";
        String TIME_PICKER_TAG = "TimePicker";
        switch (v.getId()){
            case R.id.imgbtn_detail_alarm_date:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
                break;

            case R.id.imgbtn_detail_alarm_hour:
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(), TIME_PICKER_TAG);
                break;

            case R.id.btn_detail_set_alarm:
                int notifId = info.getNumber();
                String date = tvDetailDateAlarm.getText().toString();
                String hour = tvDetailHourAlarm.getText().toString();
                String title = tvDetailTitle.getText().toString();
                String content = tvDetailContent.getText().toString();
                sharedPrefManager.setAlarmSchedule(info.getId(), date + "_" + hour);
                btnCancelAlarm.setEnabled(true);

                alarmReceiver.setOneTimeAlarm(this, notifId, date, hour, title, content);
                break;

            case R.id.btn_detail_cancel_alarm:
                alarmReceiver.cancelAlarm(this);
                sharedPrefManager.deleteAlarmSchedule(info.getId());
                btnCancelAlarm.setEnabled(false);
                tvDetailDateAlarm.setText(getString(R.string.detail_alarm_set_default_text));
                tvDetailHourAlarm.setText(getString(R.string.detail_alarm_set_default_text));
                break;
        }
    }

    @Override
    public void onDialogDateSet(String tag, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        tvDetailDateAlarm.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onDialogTimeSet(String tag, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        tvDetailHourAlarm.setText(dateFormat.format(calendar.getTime()));
    }
}
