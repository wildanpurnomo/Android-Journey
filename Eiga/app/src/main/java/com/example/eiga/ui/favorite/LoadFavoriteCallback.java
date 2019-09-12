package com.example.eiga.ui.favorite;

import android.database.Cursor;

public interface LoadFavoriteCallback {
    void preExecute();

    void postExecute(Cursor shows);
}
