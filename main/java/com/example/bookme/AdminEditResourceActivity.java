package com.example.bookme;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot; // Add this import
import com.google.firebase.database.DatabaseError; // Add this import
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener; // Add this import

public class AdminEditResourceActivity extends AppCompatActivity {

    private EditText editTextResourceID, editTextResourceName, editTextResourceType;
    private RadioGroup radioGroupAvailability;
    private RadioButton radioYes, radioNo;
    private Button updateButton, deleteButton;
    private DatabaseReference databaseReference;
    private String resourceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_resource);

        // Get resourceID from intent
        resourceID = getIntent().getStringExtra("resourceID");

        // Initialize views
        editTextResourceID = findViewById(R.id.editTextResourceID);
        editTextResourceName = findViewById(R.id.editTextResourceName);
        editTextResourceType = findViewById(R.id.editTextResourceType);
        radioGroupAvailability = findViewById(R.id.radioGroupAvailability);
        radioYes = findViewById(R.id.radioYes);
        radioNo = findViewById(R.id.radioNo);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);  // New delete button

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance("https://bookme-9de6f-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Resources").child(resourceID);

        // Fetch the resource details from the database
        fetchResourceDetails();

        // Handle update button click
        updateButton.setOnClickListener(v -> updateResource());

        // Handle delete button click
        deleteButton.setOnClickListener(v -> deleteResource());
    }

    private void fetchResourceDetails() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Resource resource = dataSnapshot.getValue(Resource.class);
                if (resource != null) {
                    editTextResourceID.setText(resource.getResourcesID());
                    editTextResourceName.setText(resource.getResourcesName());
                    editTextResourceType.setText(resource.getResourcesType());
                    if ("Yes".equals(resource.getAvailability())) {
                        radioYes.setChecked(true);
                    } else {
                        radioNo.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminEditResourceActivity.this, "Failed to load resource details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateResource() {
        String updatedResourceID = editTextResourceID.getText().toString();
        String updatedResourceName = editTextResourceName.getText().toString();
        String updatedResourceType = editTextResourceType.getText().toString();
        String availability = radioGroupAvailability.getCheckedRadioButtonId() == R.id.radioYes ? "Yes" : "No";

        Resource updatedResource = new Resource(updatedResourceID, updatedResourceName, updatedResourceType, availability);
        databaseReference.setValue(updatedResource).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdminEditResourceActivity.this, "Resource updated successfully", Toast.LENGTH_SHORT).show();
                finish();  // Go back to the previous page
            } else {
                Toast.makeText(AdminEditResourceActivity.this, "Failed to update resource", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteResource() {
        databaseReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdminEditResourceActivity.this, "Resource deleted successfully", Toast.LENGTH_SHORT).show();
                finish();  // Go back to the previous page
            } else {
                Toast.makeText(AdminEditResourceActivity.this, "Failed to delete resource", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
