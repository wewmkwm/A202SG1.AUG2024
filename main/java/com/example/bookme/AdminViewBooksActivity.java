package com.example.bookme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminViewBooksActivity extends AppCompatActivity {

    private ListView booksListView;
    private ProgressBar progressBar;
    private Button uploadBooksButton;
    private Button refreshBooksButton; // Add reference for refresh button

    private StorageReference storageReference;
    private ArrayList<String> bookNamesList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_books);

        // Initialize Firebase Storage reference
        storageReference = FirebaseStorage.getInstance().getReference("Books");

        // Initialize views
        booksListView = findViewById(R.id.booksListView);
        progressBar = findViewById(R.id.progressBar);
        uploadBooksButton = findViewById(R.id.uploadBooksButton);
        refreshBooksButton = findViewById(R.id.refreshBooksButton); // Initialize refresh button

        progressBar.setVisibility(View.VISIBLE); // Show progress bar during data load

        // Initialize list and adapter
        bookNamesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookNamesList);
        booksListView.setAdapter(adapter);

        // Fetch and display the list of books
        fetchUploadedBooks();

        // Handle clicking on a book to download/view it
        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = bookNamesList.get(position);
                downloadBook(fileName);
            }
        });

        // Navigate to AdminUploadBookActivity when button is clicked
        uploadBooksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminViewBooksActivity.this, AdminUploadBookActivity.class);
                startActivity(intent);
            }
        });

        // Refresh the book list when refresh button is clicked
        refreshBooksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchUploadedBooks(); // Call method to fetch books again
            }
        });
    }

    // Method to fetch the list of uploaded books from Firebase Storage
    private void fetchUploadedBooks() {
        progressBar.setVisibility(View.VISIBLE); // Show progress bar during data load
        bookNamesList.clear(); // Clear the existing list to prevent duplicates
        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    // Add each file name to the list
                    bookNamesList.add(item.getName());
                }
                adapter.notifyDataSetChanged(); // Refresh the list view
                progressBar.setVisibility(View.INVISIBLE); // Hide progress bar when done
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE); // Hide progress bar if error occurs
                Toast.makeText(AdminViewBooksActivity.this, "Failed to load books: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to download or view the selected book
    private void downloadBook(String fileName) {
        storageReference.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Open the PDF in a browser or external PDF viewer
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setDataAndType(uri, "application/pdf");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminViewBooksActivity.this, "Failed to download book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
