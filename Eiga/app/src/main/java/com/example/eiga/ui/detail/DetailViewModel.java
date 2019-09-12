package com.example.eiga.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.eiga.apirequest.HttpRequest;
import com.example.eiga.apirequest.RetrofitClient;
import com.example.eiga.model.Show;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailViewModel extends ViewModel {
    private MutableLiveData<Show> detailShow = new MutableLiveData<>();
    private HttpRequest httpRequest = RetrofitClient.getRetrofitInstance().create(HttpRequest.class);

    public LiveData<Show> getDetailShow() {
        return detailShow;
    }

    public void setDetailShow(int indexPage, long showID) {
        String movROute = "/3/movie/" + showID + "?api_key=7f01ed1112b9a61ce81a64ad8a18d56c&language=en-US";
        String tvRoute = "/3/tv/" + showID + "?api_key=7f01ed1112b9a61ce81a64ad8a18d56c&language=en-US";
        String targetRoute = indexPage == 0 ? movROute : tvRoute;

        Call<Show> callDetail = httpRequest.getDetail(targetRoute);
        callDetail.enqueue(new Callback<Show>() {
            @Override
            public void onResponse(@NonNull Call<Show> call, @NonNull Response<Show> response) {
                if (response.body() != null) {
                    detailShow.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Show> call, @NonNull Throwable t) {
                Log.d("onFailureDetailAPI", "Gagal");
            }
        });
    }
}
