package com.example.remindtetitb.apirequest;

import android.support.annotation.NonNull;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;

import static android.support.constraint.Constraints.TAG;

public abstract class CallbackWithRetry<T> implements Callback<T> {
    private static final int TOTAL_RETRIES = 3;
    private final Call<T> call;
    private int retryCounter = 0;

    public CallbackWithRetry(Call<T> call) {
        this.call = call;
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        Log.d(TAG, "onFailure: " + t.getMessage());
        if (retryCounter++ < TOTAL_RETRIES) {
            retry();
        }
    }

    private void retry(){
        call.clone().enqueue(this);
    }
}
