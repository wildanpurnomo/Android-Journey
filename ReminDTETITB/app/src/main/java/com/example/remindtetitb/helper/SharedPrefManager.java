package com.example.remindtetitb.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "remind-onboarding";
    private static final String IS_FIRST_LAUNCH = "is_first_launch";
    private static final String FILTER_INFO = "filter_info";

    public SharedPrefManager(Context context) {
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

    public void setAlarmSchedule(String infoID, String datetime){
        editor.putString(infoID, datetime);
        editor.commit();
    }

    public void deleteAlarmSchedule(String infoID){
        editor.remove(infoID);
        editor.commit();
    }

    public String getAlarmSchedule(String infoID){
        return sharedPreferences.getString(infoID, null);
    }


}
