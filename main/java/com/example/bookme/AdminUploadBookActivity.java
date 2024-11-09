package com.example.bookme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class AdminUploadBookActivity extends AppCompatActivity {

    private static final int PICK_PDF_FILE = 2;
    private Uri pdfUri;

    private EditText customBookNameEditText;
    private TextView pdfFileNameTextView;
    private Button uploadFileButton;
    private ProgressBar progressBar;

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload_book);

        customBookNameEditText = findViewById(R.id.customBookNameEditText);
        pdfFileNameTextView = findViewById(R.id.pdfFileName);
        uploadFileButton = findViewById(R.id.uploadFileButton);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE); // Initially hide progress bar

        storageReference = FirebaseStorage.getInstance().getReference("Books");

        // Allow admin to select a PDF file
        pdfFileNameTextView.setOnClickListener(v -> selectPdfFile());

        // Upload the PDF file when button is clicked
        uploadFileButton.setOnClickListener(v -> uploadPdfFile());
    }

    private void selectPdfFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData();
            pdfFileNameTextView.setText(Objects.requireNonNull(pdfUri).getLastPathSegment());
        }
    }

    private void uploadPdfFile() {
        if (pdfUri != null) {
            String customBookName = customBookNameEditText.getText().toString().trim();
            if (customBookName.isEmpty()) {
                Toast.makeText(this, "Please enter a book name", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE); // Show progress bar while uploading
            StorageReference fileReference = storageReference.child(customBookName + ".pdf");
            fileReference.putFile(pdfUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AdminUploadBookActivity.this, "PDF uploaded successfully!", Toast.LENGTH_SHORT).show();
                        customBookNameEditText.setText(""); // Clear the edit text
                        pdfFileNameTextView.setText("No file selected"); // Reset file name text
                    })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AdminUploadBookActivity.this, "Failed to upload PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Please select a PDF file", Toast.LENGTH_SHORT).show();
        }
    }
}
