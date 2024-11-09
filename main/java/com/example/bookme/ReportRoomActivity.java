// File: ReportRoomActivity.java
package com.example.bookme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportRoomActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Uri imageUri;
    private Spinner roomSpinner;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_room);

        // Initialize Firebase references
        databaseReference = FirebaseDatabase.getInstance().getReference("Reports");
        storageReference = FirebaseStorage.getInstance().getReference("report_images");

        imageView = findViewById(R.id.imageView);
        roomSpinner = findViewById(R.id.roomSpinner);
        Button uploadButton = findViewById(R.id.uploadButton);

        populateRoomSpinner();

        // Image view click listener to open image chooser
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // Upload button click listener
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null && roomSpinner.getSelectedItem() != null) {
                    uploadImage();
                } else {
                    Toast.makeText(ReportRoomActivity.this, "Please select an image and a room", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to open the image chooser
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    // Method to populate the room spinner
    private void populateRoomSpinner() {
        ArrayList<String> roomList = new ArrayList<>();
        // Sample room list, ideally fetched from your Firebase database
        roomList.add("Bee - Lvl2");
        roomList.add("Fly - Lvl1");
        roomList.add("Play - Lvl6");
        roomList.add("Apple - Level1");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roomList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomSpinner.setAdapter(adapter);
    }

    // Method to upload the image to Firebase Storage
    private void uploadImage() {
        String roomID = roomSpinner.getSelectedItem().toString();
        final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String photoUrl = uri.toString();
                                saveReportToDatabase(photoUrl, roomID);
                                Toast.makeText(ReportRoomActivity.this, "Report uploaded successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ReportRoomActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to save report details to Firebase Realtime Database
    private void saveReportToDatabase(String photoUrl, String roomID) {
        String reportID = databaseReference.push().getKey();
        Map<String, String> reportData = new HashMap<>();
        reportData.put("photoUrl", photoUrl);
        reportData.put("roomID", roomID);
        reportData.put("reportID", reportID);

        databaseReference.child(reportID).setValue(reportData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ReportRoomActivity.this, "Report saved successfully", Toast.LENGTH_SHORT).show();
                        // Optionally clear the fields
                        imageView.setImageResource(R.drawable.placeholder); // Reset to a placeholder image
                        imageUri = null;
                        roomSpinner.setSelection(0);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ReportRoomActivity.this, "Failed to save report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
