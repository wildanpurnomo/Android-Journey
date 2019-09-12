package com.example.eiga.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.eiga.apirequest.HttpRequest;
import com.example.eiga.apirequest.RetrofitClient;
import com.example.eiga.model.GeneralResponseModel;
import com.example.eiga.model.Show;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Show>> listTrending = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Show>> listPopular = new MutableLiveData<>();

    private HttpRequest httpRequest = RetrofitClient.getRetrofitInstance().create(HttpRequest.class);

    public LiveData<ArrayList<Show>> getListTrending() {
        return listTrending;
    }

    public void setListTrending(int index) {
        String movRoute = "/3/trending/movie/day?api_key=7f01ed1112b9a61ce81a64ad8a18d56c";
        String tvRoute = "/3/trending/tv/day?api_key=7f01ed1112b9a61ce81a64ad8a18d56c";
        String targetRoute = index == 0 ? movRoute : tvRoute;

        Call<GeneralResponseModel> callTrending = httpRequest.getTrending(targetRoute);
        callTrending.enqueue(new Callback<GeneralResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<GeneralResponseModel> call, @NonNull Response<GeneralResponseModel> response) {
                if (response.body() != null) {
                    listTrending.postValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GeneralResponseModel> call, @NonNull Throwable t) {
                Log.d("onFailureTrending", t.getMessage());
            }
        });
    }

    public LiveData<ArrayList<Show>> getListPopular() {
        return listPopular;
    }

    public void setListPopular(int index) {
        String movRoute = "/3/discover/movie?api_key=7f01ed1112b9a61ce81a64ad8a18d56c&language=en-US&sort_by=popularity.desc";
        String tvRoute = "/3/discover/tv?api_key=7f01ed1112b9a61ce81a64ad8a18d56c&language=en-US&sort_by=popularity.desc";
        String targetRoute = index == 0 ? movRoute : tvRoute;

        Call<GeneralResponseModel> callPopular = httpRequest.getPopular(targetRoute);
        callPopular.enqueue(new Callback<GeneralResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<GeneralResponseModel> call, @NonNull Response<GeneralResponseModel> response) {
                if (response.body() != null) {
                    listPopular.postValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GeneralResponseModel> call, @NonNull Throwable t) {
                Log.d("onFailurePopular", t.getMessage());
            }
        });

    }
}
