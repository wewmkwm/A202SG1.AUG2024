package com.example.bookme;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private TextView textViewName, textViewEmail, textViewPassword;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPassword = findViewById(R.id.textViewPassword);

        String userId = getIntent().getStringExtra("userID");

        // Reference to the Users node in Firebase
        databaseUsers = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        // Fetch user data from Firebase
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String password = snapshot.child("password").getValue(String.class);

                textViewName.setText("Name: " + name);
                textViewEmail.setText("Email: " + email);
                textViewPassword.setText("Password: " + password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors
            }
        });
    }
}
