package com.example.remindtetitb.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
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

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Info>> searchResults = new MutableLiveData<>();
    private HttpRequest httpRequest = RetrofitClient.getRetrofitInstance().create(HttpRequest.class);

    public MutableLiveData<ArrayList<Info>> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(String query) {
        String searchEndPoint = "/title/" + query;

        Call<ArrayList<Info>> callSearchResults = httpRequest.searchByQuery(searchEndPoint);
        APIHelper.enqueueWithRetry(callSearchResults, new Callback<ArrayList<Info>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Info>> call, @NonNull Response<ArrayList<Info>> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: " + response);
                    searchResults.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Info>> call, @NonNull Throwable t) {

            }
        });
    }
}
