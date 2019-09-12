package com.example.eiga.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GeneralResponseModel {
    @SerializedName("results")
    private ArrayList<Show> results;

    public ArrayList<Show> getResults() {
        return results;
    }
}
