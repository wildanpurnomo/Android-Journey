package com.example.eiga.model;

import com.google.gson.annotations.SerializedName;

public class Genre {
    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
