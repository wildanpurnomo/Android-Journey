package com.example.eiga.ui.search;

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

public class SearchViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Show>> searchResults = new MutableLiveData<>();
    private HttpRequest httpRequest = RetrofitClient.getRetrofitInstance().create(HttpRequest.class);
    public boolean isLoading = false;

    public LiveData<ArrayList<Show>> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(int indexHelper, String query) {
        isLoading = true;
        String searchMovEndpoint = "/3/search/movie?api_key=7f01ed1112b9a61ce81a64ad8a18d56c&language=en-US&query=" + query;
        String tvMovEndpoint = "/3/search/tv?api_key=7f01ed1112b9a61ce81a64ad8a18d56c&language=en-US&query=" + query;
        String chosenEndpoint = indexHelper == 0 ? searchMovEndpoint : tvMovEndpoint;
        Call<GeneralResponseModel> callSearchResult = httpRequest.getSearchResult(chosenEndpoint);
        callSearchResult.enqueue(new Callback<GeneralResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<GeneralResponseModel> call, @NonNull Response<GeneralResponseModel> response) {
                if (response.body() != null) {
                    searchResults.postValue(response.body().getResults());
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(@NonNull Call<GeneralResponseModel> call, @NonNull Throwable t) {
                Log.d("onFailureSearch", t.getMessage());
            }
        });
    }
}
