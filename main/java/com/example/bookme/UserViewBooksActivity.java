package com.example.bookme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UserViewBooksActivity extends AppCompatActivity {

    private ListView booksListView;
    private ProgressBar progressBar;
    private SearchView searchView;

    private StorageReference storageReference;
    private ArrayList<String> bookNamesList;
    private ArrayAdapter<String> adapter;

    private ArrayList<String> filteredBookNamesList; // To store the filtered results

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_books);

        // Initialize Firebase Storage reference
        storageReference = FirebaseStorage.getInstance().getReference("Books");

        // Initialize views
        booksListView = findViewById(R.id.booksListView);
        progressBar = findViewById(R.id.progressBar);
        searchView = findViewById(R.id.searchView);

        progressBar.setVisibility(View.VISIBLE); // Show progress bar during data load

        // Initialize lists and adapter
        bookNamesList = new ArrayList<>();
        filteredBookNamesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredBookNamesList);
        booksListView.setAdapter(adapter);

        // Fetch and display the list of books
        fetchUploadedBooks();

        // Handle clicking on a book to download/view it
        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String fileName = filteredBookNamesList.get(position); // Use filtered list
                downloadBook(fileName);
            }
        });

        // Set up SearchView to filter the book list
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooks(newText);
                return true;
            }
        });
    }

    // Method to fetch the list of uploaded books from Firebase Storage
    private void fetchUploadedBooks() {
        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    // Add each file name to the list
                    bookNamesList.add(item.getName());
                }
                filteredBookNamesList.addAll(bookNamesList); // Copy original list to filtered list
                adapter.notifyDataSetChanged(); // Refresh the list view
                progressBar.setVisibility(View.INVISIBLE); // Hide progress bar when done
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE); // Hide progress bar if error occurs
                Toast.makeText(UserViewBooksActivity.this, "Failed to load books: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to filter the list of books based on search input
    private void filterBooks(String query) {
        filteredBookNamesList.clear();
        if (query.isEmpty()) {
            filteredBookNamesList.addAll(bookNamesList); // If search is empty, show all books
        } else {
            for (String bookName : bookNamesList) {
                if (bookName.toLowerCase().contains(query.toLowerCase())) {
                    filteredBookNamesList.add(bookName);
                }
            }
        }
        adapter.notifyDataSetChanged(); // Refresh list with filtered results
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
                Toast.makeText(UserViewBooksActivity.this, "Failed to download book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
