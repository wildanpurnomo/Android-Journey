package com.example.eiga.apirequest;

import com.example.eiga.model.GeneralResponseModel;
import com.example.eiga.model.Show;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface HttpRequest {

    @GET()
    Call<GeneralResponseModel> getPopular(@Url String url);

    @GET()
    Call<GeneralResponseModel> getTrending(@Url String url);

    @GET
    Call<Show> getDetail(@Url String url);

    @GET
    Call<GeneralResponseModel> getSearchResult(@Url String url);

    @GET
    Call<GeneralResponseModel> getReleasedMovie(@Url String url);
}
