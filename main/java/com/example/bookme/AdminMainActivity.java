package com.example.bookme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminMainActivity extends AppCompatActivity {

    private Button viewBooksButton, viewResourcesButton, viewRoomsButton, viewReportsButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        // Initialize buttons
        viewBooksButton = findViewById(R.id.viewBooksButton);
        viewResourcesButton = findViewById(R.id.viewResourcesButton);
        viewRoomsButton = findViewById(R.id.viewRoomsButton);
        viewReportsButton = findViewById(R.id.viewReportsButton);
        logoutButton = findViewById(R.id.logoutButton); // Initialize the logout button

        // Set up button listeners to navigate to respective activities
        viewBooksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AdminViewBooksActivity.class);
                startActivity(intent);
            }
        });

        viewResourcesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AdminViewResourcesActivity.class);
                startActivity(intent);
            }
        });

        viewRoomsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AdminViewRoomsActivity.class);
                startActivity(intent);
            }
        });

        viewReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AdminViewReportsActivity.class);
                startActivity(intent);
            }
        });

        // Logout button listener
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to LoginActivity
                Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Finish this activity to prevent going back on logout
            }
        });
    }
}
