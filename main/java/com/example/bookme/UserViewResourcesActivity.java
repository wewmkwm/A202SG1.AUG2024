package com.example.bookme;

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

public class UserViewResourcesActivity extends AppCompatActivity {

    private ListView resourcesListView;
    private EditText searchBar;
    private ProgressBar progressBar;

    private DatabaseReference databaseReference;
    private ArrayList<String> resourcesList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_resources);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Resources");

        // Initialize views
        resourcesListView = findViewById(R.id.resourcesListView);
        searchBar = findViewById(R.id.searchBar);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE); // Show progress bar during data load

        // Initialize list and adapter
        resourcesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resourcesList);
        resourcesListView.setAdapter(adapter);

        // Fetch and display the list of resources
        fetchResources();

        // Handle clicking on a resource to show availability
        resourcesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String resource = resourcesList.get(position);
                Toast.makeText(UserViewResourcesActivity.this, "Selected Resource: " + resource, Toast.LENGTH_SHORT).show();
            }
        });

        // Add a TextWatcher to the search bar to filter the list
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s); // Filter the list based on user input
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed here
            }
        });
    }

    // Method to fetch the list of uploaded resources from Firebase Realtime Database
    private void fetchResources() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resourcesList.clear(); // Clear the list before fetching new data
                for (DataSnapshot resourceSnapshot : dataSnapshot.getChildren()) {
                    String resourceName = resourceSnapshot.child("resourcesName").getValue(String.class); // Get resource name
                    String resourceType = resourceSnapshot.child("resourcesType").getValue(String.class); // Get resource type
                    String availability = resourceSnapshot.child("availability").getValue(String.class); // Get resource availability

                    // Format resource information
                    String resourceInfo = resourceName + " (" + resourceType + ") - Availability: " + availability;
                    resourcesList.add(resourceInfo);
                }
                adapter.notifyDataSetChanged(); // Refresh the list view
                progressBar.setVisibility(View.INVISIBLE); // Hide progress bar when done
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE); // Hide progress bar if error occurs
                Toast.makeText(UserViewResourcesActivity.this, "Failed to load resources: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
