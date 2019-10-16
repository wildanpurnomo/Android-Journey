package com.example.remindtetitb.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.remindtetitb.apirequest.HttpRequest;
import com.example.remindtetitb.apirequest.RetrofitClient;
import com.example.remindtetitb.model.Info;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class InfoViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Info>> listInfo = new MutableLiveData<>();

    private HttpRequest httpRequest = RetrofitClient.getRetrofitInstance().create(HttpRequest.class);

    public LiveData<ArrayList<Info>> getListInfo() {
        return listInfo;
    }

    public void setListInfo() {
        Call<ArrayList<Info>> callInfo = httpRequest.getInfoAkademik("/");
        callInfo.enqueue(new Callback<ArrayList<Info>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Info>> call, @NonNull Response<ArrayList<Info>> response) {
                if (response.body() != null) {
                    listInfo.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Info>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
