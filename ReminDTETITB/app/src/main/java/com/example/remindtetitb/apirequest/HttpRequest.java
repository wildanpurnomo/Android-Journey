package com.example.remindtetitb.apirequest;

import com.example.remindtetitb.model.Info;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface HttpRequest {
    @GET()
    Call<ArrayList<Info>> getInfoAkademik (@Url String url);
}
