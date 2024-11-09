// File: AdminEditRoomActivity.java
package com.example.bookme;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Map;

public class AdminEditRoomActivity extends AppCompatActivity {

    private EditText editRoomName, editRoomCapacity, editRoomLocation;
    private Button saveChangesButton;
    private DatabaseReference roomRef;
    private String roomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_room);

        // Get the room ID from intent
        roomID = getIntent().getStringExtra("roomID");

        // Initialize views
        editRoomName = findViewById(R.id.editRoomName);
        editRoomCapacity = findViewById(R.id.editRoomCapacity);
        editRoomLocation = findViewById(R.id.editRoomLocation);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        // Firebase reference to the selected room
        roomRef = FirebaseDatabase.getInstance("https://bookme-9de6f-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("StudyRooms").child(roomID);

        // Fetch room details
        loadRoomDetails();

        // Save changes button functionality
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRoomChanges();
            }
        });
    }

    private void loadRoomDetails() {
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> roomData = (Map<String, Object>) dataSnapshot.getValue();
                if (roomData != null) {
                    editRoomName.setText((String) roomData.get("roomName"));
                    editRoomCapacity.setText(String.valueOf(roomData.get("capacity")));
                    editRoomLocation.setText((String) roomData.get("location"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminEditRoomActivity.this, "Failed to load room details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveRoomChanges() {
        String newRoomName = editRoomName.getText().toString().trim();
        String newRoomCapacity = editRoomCapacity.getText().toString().trim();
        String newRoomLocation = editRoomLocation.getText().toString().trim();

        // Create a map of changes to update
        Map<String, Object> updatedRoom = Map.of(
                "roomName", newRoomName,
                "capacity", Integer.parseInt(newRoomCapacity),
                "location", newRoomLocation
        );

        // Update room details in Firebase
        roomRef.updateChildren(updatedRoom).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdminEditRoomActivity.this, "Room updated successfully", Toast.LENGTH_SHORT).show();
                finish(); // Go back to the previous activity
            } else {
                Toast.makeText(AdminEditRoomActivity.this, "Failed to update room", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
