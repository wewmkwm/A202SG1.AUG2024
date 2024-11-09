// File: ReportAdapter.java
package com.example.bookme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

public class ReportAdapter extends ArrayAdapter<Map<String, String>> {

    public ReportAdapter(Context context, ArrayList<Map<String, String>> reportList) {
        super(context, 0, reportList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Reuse or inflate the item layout
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_report, parent, false);
        }

        // Get the report at the current position
        Map<String, String> report = getItem(position);

        // Get references to TextView and ImageView in the item layout
        TextView textViewRoomID = convertView.findViewById(R.id.textViewRoomID);
        ImageView imageViewReportPhoto = convertView.findViewById(R.id.imageViewReportPhoto);

        // Set the Room ID and load the image using Glide
        if (report != null) {
            textViewRoomID.setText("Room ID: " + report.get("roomID"));
            Glide.with(getContext())
                    .load(report.get("photoUrl"))
                    .into(imageViewReportPhoto);
        }

        return convertView;
    }
}
