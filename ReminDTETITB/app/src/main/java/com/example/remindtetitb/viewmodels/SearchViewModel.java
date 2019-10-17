package com.example.remindtetitb.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.remindtetitb.apirequest.CallbackWithRetry;
import com.example.remindtetitb.apirequest.HttpRequest;
import com.example.remindtetitb.apirequest.RetrofitClient;
import com.example.remindtetitb.model.Info;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Info>> searchResults = new MutableLiveData<>();
    private HttpRequest httpRequest = RetrofitClient.getRetrofitInstance().create(HttpRequest.class);

    public LiveData<ArrayList<Info>> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(String query) {
        String searchEndPoint = "/title/" + query;

        Call<ArrayList<Info>> callSearchResults = httpRequest.searchByQuery(searchEndPoint);
        callSearchResults.enqueue(new CallbackWithRetry<ArrayList<Info>>(callSearchResults) {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Info>> call, @NonNull Response<ArrayList<Info>> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: " + response);
                    searchResults.postValue(response.body());
                }
            }
        });
    }
}
