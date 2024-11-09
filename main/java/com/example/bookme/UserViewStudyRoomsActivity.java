package com.example.bookme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserViewStudyRoomsActivity extends AppCompatActivity {

    private ListView studyRoomsListView;
    private ProgressBar progressBar;
    private EditText searchEditText; // Declare the search bar

    private DatabaseReference databaseReference;
    private ArrayList<String> studyRoomsList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_study_rooms);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("StudyRooms");

        // Initialize views
        studyRoomsListView = findViewById(R.id.studyRoomsListView);
        progressBar = findViewById(R.id.progressBar);
        searchEditText = findViewById(R.id.searchEditText); // Initialize search bar

        progressBar.setVisibility(View.VISIBLE); // Show progress bar during data load

        // Initialize list and adapter
        studyRoomsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studyRoomsList);
        studyRoomsListView.setAdapter(adapter);

        // Fetch and display the list of study rooms
        fetchStudyRooms();

        // Handle clicking on a study room to show its details
        studyRoomsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String room = studyRoomsList.get(position);

                // Create an Intent to start RoomDetailActivity
                Intent intent = new Intent(UserViewStudyRoomsActivity.this, RoomDetailActivity.class);
                intent.putExtra("roomInfo", room); // Pass the selected room details
                startActivity(intent); // Start the new activity
            }
        });

        // Add TextWatcher to filter the list based on search query
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the adapter when text changes
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not used
            }
        });
    }

    // Method to fetch the list of uploaded study rooms from Firebase Realtime Database
    private void fetchStudyRooms() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studyRoomsList.clear(); // Clear the list before fetching new data
                for (DataSnapshot roomSnapshot : dataSnapshot.getChildren()) {
                    String roomName = roomSnapshot.child("roomName").getValue(String.class); // Get room name
                    Long capacityLong = roomSnapshot.child("capacity").getValue(Long.class); // Get room capacity as Long
                    String location = roomSnapshot.child("location").getValue(String.class); // Get room location

                    // Check for null capacity and convert it to String if not null
                    String capacity = capacityLong != null ? capacityLong.toString() : "N/A"; // Default to "N/A" if null

                    // Format room information
                    String roomInfo = roomName + " (Capacity: " + capacity + ", Location: " + location + ")";
                    studyRoomsList.add(roomInfo);
                }
                adapter.notifyDataSetChanged(); // Refresh the list view
                progressBar.setVisibility(View.INVISIBLE); // Hide progress bar when done
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE); // Hide progress bar if error occurs
                Toast.makeText(UserViewStudyRoomsActivity.this, "Failed to load study rooms: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
