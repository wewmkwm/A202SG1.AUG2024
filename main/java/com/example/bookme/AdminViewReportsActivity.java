// File: AdminViewReportsActivity.java
package com.example.bookme;

import android.os.Bundle;
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
import java.util.HashMap;
import java.util.Map;

public class AdminViewReportsActivity extends AppCompatActivity {

    private ListView listViewReports;
    private ReportAdapter reportAdapter;
    private ArrayList<Map<String, String>> reportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_reports);

        listViewReports = findViewById(R.id.listViewReports);
        reportList = new ArrayList<>();
        reportAdapter = new ReportAdapter(this, reportList);
        listViewReports.setAdapter(reportAdapter);

        loadReports();
    }

    private void loadReports() {
        DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference("Reports");

        reportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportList.clear();
                for (DataSnapshot reportSnapshot : snapshot.getChildren()) {
                    Map<String, String> report = new HashMap<>();
                    report.put("roomID", reportSnapshot.child("roomID").getValue(String.class));
                    report.put("photoUrl", reportSnapshot.child("photoUrl").getValue(String.class));
                    reportList.add(report);
                }
                reportAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminViewReportsActivity.this, "Failed to load reports", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
