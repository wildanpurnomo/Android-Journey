package com.example.simpleconsumerapp.helper;

import android.database.Cursor;

import com.example.simpleconsumerapp.Model.Show;

import java.util.ArrayList;

import static com.example.simpleconsumerapp.db.DatabaseContract.FavoriteColumns.CATEGORY;
import static com.example.simpleconsumerapp.db.DatabaseContract.FavoriteColumns.IDTMDB;
import static com.example.simpleconsumerapp.db.DatabaseContract.FavoriteColumns.IMGURL;
import static com.example.simpleconsumerapp.db.DatabaseContract.FavoriteColumns.RATE;
import static com.example.simpleconsumerapp.db.DatabaseContract.FavoriteColumns.TITLE;

public class MappingHelper {
    public static ArrayList<Show> mapCursorToArrayList(Cursor cursor) {
        ArrayList<Show> shows = new ArrayList<>();

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(IDTMDB));
            String posterPath = cursor.getString(cursor.getColumnIndexOrThrow(IMGURL));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY));
            double voteAverage = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(RATE)));

            shows.add(new Show(id, posterPath, title, type, voteAverage));
        }

        return shows;
    }
}
