package com.example.remindtetitb.ui.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.remindtetitb.R;
import com.example.remindtetitb.alarm.AlarmWorker;
import com.example.remindtetitb.helper.SharedPrefManager;
import com.example.remindtetitb.model.Info;
import com.example.remindtetitb.ui.fragments.DatePickerFragment;
import com.example.remindtetitb.ui.fragments.TimePickerFragment;
import com.example.remindtetitb.utils.ParcelableUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {
    public static final String EXTRA_INFO = "extra_info";

    private TextView tvDetailDateAlarm;
    private TextView tvDetailHourAlarm;
    private Button btnCancelAlarm;
    private Info info = new Info();

    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvDetailTitle = findViewById(R.id.tv_detail_title);
        TextView tvDetailContent = findViewById(R.id.tv_detail_content);
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
                String date = tvDetailDateAlarm.getText().toString();
                String hour = tvDetailHourAlarm.getText().toString();

                if(date.equals(getString(R.string.detail_alarm_set_default_text)) || hour.equals(getString(R.string.detail_alarm_set_default_text))) {
                    Toast.makeText(this, "Jadwal belum diisi", Toast.LENGTH_SHORT).show();
                } else{
                    sharedPrefManager.setAlarmSchedule(info.getId(), date + "_" + hour);
                    btnCancelAlarm.setEnabled(true);

                    String[] dateArray = date.split("-");
                    String[] timeArray = hour.split(":");

                    Calendar dueTime = Calendar.getInstance();
                    dueTime.set(Calendar.YEAR, Integer.parseInt(dateArray[0]));
                    dueTime.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
                    dueTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]));
                    dueTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
                    dueTime.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
                    dueTime.set(Calendar.SECOND, 0);

                    Calendar currentTIme = Calendar.getInstance();
                    long timeDiff = dueTime.getTimeInMillis() - currentTIme.getTimeInMillis();

                    Constraints constraints = new Constraints.Builder()
                            .build();

                    Data.Builder builder = new Data.Builder();
                    byte[] bytifiedInfo = ParcelableUtil.marshall(info);
                    builder.putByteArray(AlarmWorker.BYTES, bytifiedInfo);
                    Data data = builder.build();

                    OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(AlarmWorker.class)
                            .setInputData(data)
                            .setConstraints(constraints)
                            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                            .build();

                    WorkManager.getInstance(this).enqueue(workRequest);
                    Toast.makeText(this, "Pengingat dipasang", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_detail_cancel_alarm:
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
