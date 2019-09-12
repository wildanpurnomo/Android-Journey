package com.example.eiga.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.eiga.db.DatabaseContract;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import static com.example.eiga.db.DatabaseContract.getColumnLong;
import static com.example.eiga.db.DatabaseContract.getColumnString;

public class Show implements Parcelable {
    @SerializedName("id")
    private long id;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName(value = "title", alternate = "name")
    private String title;

    @SerializedName(value = "release_date", alternate = "first_air_date")
    private String releaseDate;

    @SerializedName("original_language")
    private String language;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("genres")
    private ArrayList<Genre> genres;

    @SerializedName("overview")
    private String overview;

    @SerializedName("media_type")
    private String type;

    public long getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getLanguage() {
        return language;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Double getPopularity() {
        return popularity;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public String getOverview() {
        return overview;
    }

    public String getType() {
        return type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.posterPath);
        dest.writeString(this.title);
        dest.writeString(this.releaseDate);
        dest.writeString(this.language);
        dest.writeValue(this.voteAverage);
        dest.writeValue(this.popularity);
        dest.writeString(this.overview);
        dest.writeString(this.type);
    }

    public Show() {
    }

    public Show(long id, String posterPath, String title, String type, double voteAverage) {
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
        this.type = type;
        this.voteAverage = voteAverage;
    }

    public Show(Cursor cursor) {
        this.id = getColumnLong(cursor, DatabaseContract.FavoriteColumns.IDTMDB);
        this.posterPath = getColumnString(cursor, DatabaseContract.FavoriteColumns.IMGURL);
        this.title = getColumnString(cursor, DatabaseContract.FavoriteColumns.TITLE);
        this.type = getColumnString(cursor, DatabaseContract.FavoriteColumns.CATEGORY);
        this.voteAverage = Double.parseDouble(getColumnString(cursor, DatabaseContract.FavoriteColumns.RATE));
    }

    protected Show(Parcel in) {
        this.id = in.readLong();
        this.posterPath = in.readString();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.language = in.readString();
        this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.overview = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Show> CREATOR = new Parcelable.Creator<Show>() {
        @Override
        public Show createFromParcel(Parcel source) {
            return new Show(source);
        }

        @Override
        public Show[] newArray(int size) {
            return new Show[size];
        }
    };
}
