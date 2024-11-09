// File: AdminViewRoomsActivity.java
package com.example.bookme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminViewRoomsActivity extends AppCompatActivity {

    private EditText searchBar;
    private ListView listViewRooms;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> roomList;
    private ArrayList<String> filteredList;
    private DatabaseReference databaseReference;
    private Button addRoomButton;
    private ArrayList<String> roomIds; // To store room IDs for each room

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_rooms);

        // Initialize views
        searchBar = findViewById(R.id.searchBar);
        listViewRooms = findViewById(R.id.listViewRooms);
        addRoomButton = findViewById(R.id.addRoomButton);
        roomList = new ArrayList<>();
        filteredList = new ArrayList<>();
        roomIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredList);
        listViewRooms.setAdapter(adapter);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance("https://bookme-9de6f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("StudyRooms");

        // Fetch rooms from Firebase and display in ListView
        fetchRoomsFromDatabase();

        // Add search bar text listener
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterRooms(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Add Room button functionality
        addRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminViewRoomsActivity.this, AdminAddRoomActivity.class);
                startActivity(intent);
            }
        });

        // Set up item click listener for ListView to navigate to AdminEditRoomActivity
        listViewRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedRoomId = roomIds.get(position);
                Intent intent = new Intent(AdminViewRoomsActivity.this, AdminEditRoomActivity.class);
                intent.putExtra("roomID", selectedRoomId);
                startActivity(intent);
            }
        });
    }

    private void fetchRoomsFromDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomList.clear();
                filteredList.clear();
                roomIds.clear();

                // Iterate through all rooms in the database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> roomData = (Map<String, Object>) snapshot.getValue();
                    String roomID = snapshot.getKey();  // Get the room ID from the snapshot key

                    if (roomData != null) {
                        String roomName = (String) roomData.get("roomName");
                        String capacity = String.valueOf(roomData.get("capacity"));
                        String location = (String) roomData.get("location");

                        // Create a string to display the room info
                        String roomInfo = "ID: " + roomID + "\nName: " + roomName + "\nCapacity: " + capacity + "\nLocation: " + location;
                        roomList.add(roomInfo);
                        roomIds.add(roomID); // Add the room ID to the list
                    }
                }

                // Initially display all rooms
                filteredList.addAll(roomList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminViewRoomsActivity.this, "Failed to load rooms", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Filter rooms based on search query
    private void filterRooms(String query) {
        filteredList.clear();
        for (String room : roomList) {
            if (room.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(room);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
