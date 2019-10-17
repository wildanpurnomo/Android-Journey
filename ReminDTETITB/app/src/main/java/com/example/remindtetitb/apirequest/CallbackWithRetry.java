package com.example.remindtetitb.apirequest;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public abstract class CallbackWithRetry<T> implements Callback<T> {
    private static final int TOTAL_RETRIES = 3;
    private final Call<T> call;
    private int retryCounter = 0;

    public CallbackWithRetry(Call<T> call) {
        this.call = call;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if(!APIHelper.isCallSuccess(response)){
            if(retryCounter++ < TOTAL_RETRIES){
                retry();
            } else{
                onFinalResponse(call, response);
            }
        } else{
            onFinalResponse(call, response);
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        if (retryCounter++ < TOTAL_RETRIES) {
            Log.d(TAG, "Retrying..." );
            retry();
        } else{
            onFinalFailure(call, t);
        }
    }

    public void onFinalResponse(Call<T> call, Response<T> response){

    }

    public void onFinalFailure(Call<T> call, Throwable t){

    }

    private void retry(){
        call.clone().enqueue(this);
    }
}
