package com.example.simpleconsumerapp;

import android.database.Cursor;

public interface LoadFavoritesCallback {
    void postExecute(Cursor shows);
}
