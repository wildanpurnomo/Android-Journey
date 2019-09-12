package com.example.eiga.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.example.eiga";
    private static final String SCHEME = "content";

    public static final class FavoriteColumns implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String IDTMDB = "idTMDB";
        public static final String IMGURL = "imgurl";
        public static final String TITLE = "title";
        public static final String CATEGORY = "category";
        public static final String RATE = "rate";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
