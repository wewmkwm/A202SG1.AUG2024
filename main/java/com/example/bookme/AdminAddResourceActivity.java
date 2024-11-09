// File: AdminAddResourceActivity.java
package com.example.bookme;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminAddResourceActivity extends AppCompatActivity {

    private EditText resourcesIDEditText, resourcesNameEditText, resourcesTypeEditText;
    private RadioGroup availabilityRadioGroup;
    private Button addResourceButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_resource);

        // Initialize views
        resourcesIDEditText = findViewById(R.id.resourcesIDEditText);
        resourcesNameEditText = findViewById(R.id.resourcesNameEditText);
        resourcesTypeEditText = findViewById(R.id.resourcesTypeEditText);
        availabilityRadioGroup = findViewById(R.id.availabilityRadioGroup);
        addResourceButton = findViewById(R.id.addResourceButton);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance("https://bookme-9de6f-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Resources");

        // Add resource button functionality
        addResourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewResource();
            }
        });
    }

    private void addNewResource() {
        String resourcesID = resourcesIDEditText.getText().toString();
        String resourcesName = resourcesNameEditText.getText().toString();
        String resourcesType = resourcesTypeEditText.getText().toString();
        String availability = ((RadioButton) findViewById(availabilityRadioGroup.getCheckedRadioButtonId())).getText().toString();

        if (TextUtils.isEmpty(resourcesID) || TextUtils.isEmpty(resourcesName) || TextUtils.isEmpty(resourcesType)) {
            Toast.makeText(AdminAddResourceActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new resource object
        Resource resource = new Resource(resourcesID, resourcesName, resourcesType, availability);

        // Add the new resource to the database
        databaseReference.child(resourcesID).setValue(resource).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdminAddResourceActivity.this, "Resource added successfully", Toast.LENGTH_SHORT).show();
                finish(); // Go back to the previous activity
            } else {
                Toast.makeText(AdminAddResourceActivity.this, "Failed to add resource", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
