package com.example.eiga.ui.notificationsettings;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.eiga.MainActivity;
import com.example.eiga.apirequest.HttpRequest;
import com.example.eiga.apirequest.RetrofitClient;
import com.example.eiga.model.GeneralResponseModel;
import com.example.eiga.model.Show;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleaseService extends Service {
    public static final String TAG = "ReleaseService";
    public static final ArrayList<Show> simpenan = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String endpoint = "/3/discover/movie?api_key=7f01ed1112b9a61ce81a64ad8a18d56c&primary_release_date.gte=" + dateFormat.format(date) + "&primary_release_date.lte=" + dateFormat.format(date);

        HttpRequest httpRequest = RetrofitClient.getRetrofitInstance().create(HttpRequest.class);
        Call<GeneralResponseModel> callRelease = httpRequest.getReleasedMovie(endpoint);
        callRelease.enqueue(new Callback<GeneralResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<GeneralResponseModel> call, @NonNull Response<GeneralResponseModel> response) {
                if (response.body() != null) {
                    ArrayList<Show> results = response.body().getResults();

                    Intent notifyIntent = new Intent(MainActivity.ACTION_GET_RELEASE);
                    notifyIntent.putExtra(AlarmReceiver.EXTRA_RELEASED_LIST, results);
                    notifyIntent.putExtra(AlarmReceiver.EXTRA_TYPE, AlarmReceiver.TYPE_RELEASE);

                    sendBroadcast(notifyIntent);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GeneralResponseModel> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        return START_STICKY;
    }
}
