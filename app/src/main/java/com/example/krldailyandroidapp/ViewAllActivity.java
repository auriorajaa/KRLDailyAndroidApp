package com.example.krldailyandroidapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.krldailyandroidapp.adapter.ViewAllAdapter;
import com.example.krldailyandroidapp.model.ViewAllModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ViewAllActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ViewAllAdapter viewAllAdapter;
    private List<ViewAllModel> viewAllModelList;

    private TextView categoryName;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        categoryName = findViewById(R.id.category_name);

        // Dapatkan kategori yang dipilih dari intent
        selectedCategory = getIntent().getStringExtra("Category");
        categoryName.setText(selectedCategory);

        recyclerView = findViewById(R.id.view_all);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        viewAllModelList = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(this, viewAllModelList);
        recyclerView.setAdapter(viewAllAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Articles");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewAllModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey(); // Dapatkan UID

                    String title = snapshot.child("ArticleTitle").getValue(String.class);
                    String category = snapshot.child("ArticleCategory").getValue(String.class);
                    String createdAt = snapshot.child("CreatedAt").getValue(String.class);
                    String image = snapshot.child("ArticleImage").getValue(String.class);

                    // Format the createdAt timestamp
                    createdAt = formatTimestamp(createdAt);

                    // Buat objek ViewAllModel dengan data yang didapatkan
                    ViewAllModel recipe = new ViewAllModel(uid, image, title, category, createdAt);

                    // Tambahkan resep jika kategori sesuai
                    if (selectedCategory.equals(category)) {
                        viewAllModelList.add(recipe);
                    }
                }
                viewAllAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ViewAllActivity", "Error: " + databaseError.getMessage());
            }
        });
    }

    private void loadRecipes(String category) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Recipes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewAllModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ViewAllModel recipe = snapshot.getValue(ViewAllModel.class);
                    if (recipe != null && recipe.getArticleCategory().equals(category)) {
                        viewAllModelList.add(recipe);
                    }
                }
                viewAllAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ViewAllActivity", "Error: " + databaseError.getMessage());
            }
        });
    }

    public String formatTimestamp(String timestamp) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(timestamp);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            return dateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return timestamp; // Return original timestamp if parsing fails
        }
    }

    // Method to handle back button click
    public void onBackButtonClick(View view) {
        onBackPressed(); // Navigate back to previous activity
    }
}
