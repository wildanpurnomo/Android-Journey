package com.example.remindtetitb.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.remindtetitb.apirequest.APIHelper;
import com.example.remindtetitb.apirequest.HttpRequest;
import com.example.remindtetitb.apirequest.RetrofitClient;
import com.example.remindtetitb.model.Info;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Info>> listInfo = new MutableLiveData<>();

    private HttpRequest httpRequest = RetrofitClient.getRetrofitInstance().create(HttpRequest.class);

    public LiveData<ArrayList<Info>> getListInfo() {
        return listInfo;
    }

    public void setListInfo() {
        Call<ArrayList<Info>> callInfo = httpRequest.getInfoAkademik("/");
        APIHelper.enqueueWithRetry(callInfo, new Callback<ArrayList<Info>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Info>> call, @NonNull Response<ArrayList<Info>> response) {
                if (response.body() != null) {
                    listInfo.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Info>> call, @NonNull Throwable t) {
                listInfo.postValue(null);
            }
        });
    }
}
