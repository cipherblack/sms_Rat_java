package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
                        String sender = message.getOriginatingAddress();
                        String body = message.getMessageBody();
                        long timestamp = message.getTimestampMillis();

                        Log.d(TAG, "New SMS received from: " + sender + " - " + body);
                        try {
                            body = new String(body.getBytes(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        Intent serviceIntent = new Intent(context, SmsService.class);
                        serviceIntent.putExtra("sender", sender);
                        serviceIntent.putExtra("body", body);
                        serviceIntent.putExtra("timestamp", timestamp);
                        context.startService(serviceIntent);
                    }
                }
            }
        }
    }
}
