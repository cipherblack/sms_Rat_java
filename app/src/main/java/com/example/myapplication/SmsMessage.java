package com.example.myapplication;

public class SmsMessage {
    private String sender;
    private String body;
    private long timestamp;

    public SmsMessage(String sender, String body, long timestamp) {
        this.sender = sender;
        this.body = body;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getBody() {
        return body;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
