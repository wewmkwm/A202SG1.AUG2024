package com.example.bookme;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RoomDetailActivity extends AppCompatActivity {

    private TextView roomNameTextView;
    private TextView roomCapacityTextView;
    private TextView roomLocationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        // Initialize TextViews
        roomNameTextView = findViewById(R.id.roomNameTextView);
        roomCapacityTextView = findViewById(R.id.roomCapacityTextView);
        roomLocationTextView = findViewById(R.id.roomLocationTextView);

        // Get the data passed from the previous activity
        String roomInfo = getIntent().getStringExtra("roomInfo");
        if (roomInfo != null) {
            String[] roomDetails = roomInfo.split(" \\(Capacity: |, Location: ");
            roomNameTextView.setText(roomDetails[0]);
            roomCapacityTextView.setText("Capacity: " + roomDetails[1]);
            roomLocationTextView.setText("Location: " + roomDetails[2].replace(")", ""));
        }
    }
}
