package com.example.bookme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserMainActivity extends AppCompatActivity {

    private ImageButton buttonStudyRoom, buttonReport, buttonResources, buttonLibrary, buttonProfile;
    private Button buttonLogout;
    private TextView textViewWelcome;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        buttonStudyRoom = findViewById(R.id.button_study_room);
        buttonReport = findViewById(R.id.button_report); // Initialize buttonReport
        buttonResources = findViewById(R.id.button_resources);
        buttonLibrary = findViewById(R.id.button_library);
        buttonProfile = findViewById(R.id.button_profile);
        buttonLogout = findViewById(R.id.button_logout);
        textViewWelcome = findViewById(R.id.textViewWelcome);

        String userId = getIntent().getStringExtra("userID");

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users");

        if (userId != null) {
            databaseUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userName = snapshot.child("name").getValue(String.class);
                    if (userName != null) {
                        textViewWelcome.setText("Welcome back, " + userName);
                    } else {
                        textViewWelcome.setText("Welcome back!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    textViewWelcome.setText("Welcome back!");
                }
            });
        }

        buttonStudyRoom.setOnClickListener(v -> {
            Intent intent = new Intent(UserMainActivity.this, UserViewStudyRoomsActivity.class);
            startActivity(intent);
        });

        buttonReport.setOnClickListener(v -> {
            Intent intent = new Intent(UserMainActivity.this, ReportRoomActivity.class);
            startActivity(intent);
        });

        buttonResources.setOnClickListener(v -> {
            Intent intent = new Intent(UserMainActivity.this, UserViewResourcesActivity.class);
            startActivity(intent);
        });

        buttonLibrary.setOnClickListener(v -> {
            Intent intent = new Intent(UserMainActivity.this, UserViewBooksActivity.class);
            startActivity(intent);
        });

        buttonProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UserMainActivity.this, UserProfileActivity.class);
            intent.putExtra("userID", userId);
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(UserMainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
