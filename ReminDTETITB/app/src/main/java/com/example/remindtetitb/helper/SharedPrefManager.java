package com.example.remindtetitb.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    private static final String PREF_NAME = "remind-onboarding";

    private static final String IS_FIRST_LAUNCH = "is_first_launch";
    private static final String FILTER_INFO = "filter_info";

    public SharedPrefManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setIsFirstLaunch(boolean isFirstLaunch){
        editor.putBoolean(IS_FIRST_LAUNCH, isFirstLaunch);
        editor.commit();
    }

    public void setFilterInfo(String filterInfo){
        editor.putString(FILTER_INFO, filterInfo);
        editor.commit();
    }

    public boolean isFirstLaunch(){
        return sharedPreferences.getBoolean(IS_FIRST_LAUNCH, true);
    }

    public String filterInfo(){
        return sharedPreferences.getString(FILTER_INFO, "all");
    }
}
