package com.example.simpleconsumerapp.Model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.simpleconsumerapp.db.DatabaseContract;

import static com.example.simpleconsumerapp.db.DatabaseContract.getColumnLong;
import static com.example.simpleconsumerapp.db.DatabaseContract.getColumnString;

public class Show implements Parcelable {
    private long id;

    private String posterPath;

    private String title;

    private Double voteAverage;

    private String type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getType() {
        return type;
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
        dest.writeValue(this.voteAverage);
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
        this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
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
