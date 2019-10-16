package com.example.remindtetitb.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("number")
    @Expose
    private int number;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("link")
    @Expose
    private String link;

    public String getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.number);
        dest.writeString(this.label);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.link);
    }

    public Info() {
    }

    protected Info(Parcel in) {
        this.id = in.readString();
        this.number = in.readInt();
        this.label = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.link = in.readString();
    }

    public static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel source) {
            return new Info(source);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };
}

