package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsViewHolder> {

    private ArrayList<SmsMessage> smsList;

    public SmsAdapter(ArrayList<SmsMessage> smsList) {
        this.smsList = smsList;
    }

    @NonNull
    @Override
    public SmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms, parent, false);
        return new SmsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmsViewHolder holder, int position) {
        SmsMessage sms = smsList.get(position);
        holder.senderTextView.setText(sms.getSender());
        holder.bodyTextView.setText(sms.getBody());
        holder.timestampTextView.setText(String.valueOf(sms.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public static class SmsViewHolder extends RecyclerView.ViewHolder {
        TextView senderTextView;
        TextView bodyTextView;
        TextView timestampTextView;

        public SmsViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            bodyTextView = itemView.findViewById(R.id.bodyTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }
    }
}
