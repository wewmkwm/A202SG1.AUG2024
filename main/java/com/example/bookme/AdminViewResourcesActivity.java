package com.example.bookme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class AdminViewResourcesActivity extends AppCompatActivity {

    private ListView listViewResources;
    private ArrayList<String> resourcesList;
    private ArrayAdapter<String> adapter;
    private Button addResourceButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_resources);

        listViewResources = findViewById(R.id.listViewResources);
        addResourceButton = findViewById(R.id.addResourceButton);
        resourcesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resourcesList);
        listViewResources.setAdapter(adapter);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance("https://bookme-9de6f-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Resources");

        // Fetch resources from the database and display in ListView
        fetchResourcesFromDatabase();

        // Handle "Add Resource" button click
        addResourceButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminViewResourcesActivity.this, AdminAddResourceActivity.class);
            startActivity(intent);
        });

        // Handle selecting an existing resource from the ListView
        listViewResources.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedResourceID = resourcesList.get(position).split("\n")[0].split(": ")[1];  // Extract resourceID
                Intent intent = new Intent(AdminViewResourcesActivity.this, AdminEditResourceActivity.class);
                intent.putExtra("resourceID", selectedResourceID);
                startActivity(intent);
            }
        });
    }

    private void fetchResourcesFromDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resourcesList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Resource resource = snapshot.getValue(Resource.class);
                    if (resource != null) {
                        String resourceInfo = "ID: " + resource.getResourcesID() +
                                "\nName: " + resource.getResourcesName() +
                                "\nType: " + resource.getResourcesType() +
                                "\nAvailability: " + resource.getAvailability();
                        resourcesList.add(resourceInfo);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminViewResourcesActivity.this, "Failed to load resources", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
