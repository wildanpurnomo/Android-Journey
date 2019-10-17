package com.example.remindtetitb.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.remindtetitb.apirequest.HttpRequest;
import com.example.remindtetitb.apirequest.RetrofitClient;
import com.example.remindtetitb.model.Info;
import com.example.remindtetitb.apirequest.CallbackWithRetry;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class InfoViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Info>> listInfo = new MutableLiveData<>();

    private HttpRequest httpRequest = RetrofitClient.getRetrofitInstance().create(HttpRequest.class);

    public LiveData<ArrayList<Info>> getListInfo() {
        return listInfo;
    }

    public void setListInfo() {
        Call<ArrayList<Info>> callInfo = httpRequest.getInfoAkademik("/");
        callInfo.enqueue(new CallbackWithRetry<ArrayList<Info>>(callInfo) {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Info>> call, @NonNull Response<ArrayList<Info>> response) {
                if (response.body() != null) {
                    listInfo.postValue(response.body());
                }
            }
        });
    }
}
