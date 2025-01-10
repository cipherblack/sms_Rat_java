package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private static final String PREF_NAME = "sms_preferences";
    private static final String LAST_TIMESTAMP_KEY = "last_timestamp";

    public static void saveLastTimestamp(Context context, long timestamp) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_TIMESTAMP_KEY, timestamp);
        editor.apply();
    }

    public static long getLastTimestamp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(LAST_TIMESTAMP_KEY, 0);
    }
}
