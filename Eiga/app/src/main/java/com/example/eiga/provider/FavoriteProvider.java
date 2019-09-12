package com.example.eiga.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.eiga.db.FavoriteHelper;
import com.example.eiga.favoritewidget.FavoriteWidget;
import com.example.eiga.ui.favorite.FavoriteDisplayer;

import static com.example.eiga.db.DatabaseContract.AUTHORITY;
import static com.example.eiga.db.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.example.eiga.db.DatabaseContract.FavoriteColumns.TABLE_NAME;

public class FavoriteProvider extends ContentProvider {
    private static final int FAV = 1;
    private static final int FAV_ID = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FavoriteHelper favoriteHelper;

    static {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", FAV_ID);
    }

    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        favoriteHelper.open();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case FAV:
                cursor = favoriteHelper.queryProvider();
                break;

            case FAV_ID:
                cursor = favoriteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;

            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        favoriteHelper.open();
        long inserted;
        if (uriMatcher.match(uri) == FAV) {
            inserted = favoriteHelper.insertProvider(values);
        } else {
            inserted = 0;
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(CONTENT_URI, new FavoriteDisplayer.DataObserver(new Handler(), getContext()));
            FavoriteWidget.refreshWidget(getContext());
        }


        return Uri.parse(CONTENT_URI + "/" + inserted);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        favoriteHelper.open();
        int deleted;
        if (uriMatcher.match(uri) == FAV_ID) {
            deleted = favoriteHelper.deleteProvider(uri.getLastPathSegment());
        } else {
            deleted = 0;
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(CONTENT_URI, new FavoriteDisplayer.DataObserver(new Handler(), getContext()));
            FavoriteWidget.refreshWidget(getContext());
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
