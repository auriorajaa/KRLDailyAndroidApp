package com.example.krldailyandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.krldailyandroidapp.adapter.HomeCategoryAdapter;
import com.example.krldailyandroidapp.adapter.RandomAdapter;
import com.example.krldailyandroidapp.adapter.RecentAdapter;
import com.example.krldailyandroidapp.databinding.ActivityMainBinding;
import com.example.krldailyandroidapp.model.Article;
import com.example.krldailyandroidapp.model.HomeCategoryModel;
import com.example.krldailyandroidapp.model.RandomModel;
import com.example.krldailyandroidapp.model.RecentModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    // Initialize RecyclerView
    private RecyclerView recentRecipeRecyclerView, homeCategoryRecyclerView, randomArticleRecyclerView;

    // Category
    private List<HomeCategoryModel> homeCategoryModelList;
    private HomeCategoryAdapter homeCategoryAdapter;

    // Recent Recipe
    private List<RecentModel> recentModelList;
    private RecentAdapter recentAdapter;

    // Random Recipe
    private List<RandomModel> randomModelList;
    private RandomAdapter randomAdapter;

    private List<Article> articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize lists
        homeCategoryModelList = new ArrayList<>();
        recentModelList = new ArrayList<>();
        randomModelList = new ArrayList<>();
        articleList = new ArrayList<>();

        // Initialize RecyclerView for Home Category
        homeCategoryRecyclerView = findViewById(R.id.homeCategoryRecyclerView);
        homeCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        homeCategoryAdapter = new HomeCategoryAdapter(this, homeCategoryModelList);
        homeCategoryRecyclerView.setAdapter(homeCategoryAdapter);

        // Setup bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                return true;
            } else if (itemId == R.id.bottom_search) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else {
                return false;
            }
        });

        // Retrieve home category data from Firebase
        DatabaseReference categoryReference = FirebaseDatabase.getInstance().getReference("Category");
        categoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                homeCategoryModelList.clear();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String name = categorySnapshot.child("Name").getValue(String.class);
                    String image = categorySnapshot.child("Image").getValue(String.class);
                    HomeCategoryModel homeCategoryModel = new HomeCategoryModel(name, image);
                    homeCategoryModelList.add(homeCategoryModel);
                }
                homeCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read data", databaseError.toException());
            }
        });

        // Initialize RecyclerView for Recent Recipes
        recentRecipeRecyclerView = findViewById(R.id.recentArticleRecyclerView);
        recentRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recentAdapter = new RecentAdapter(this, recentModelList);
        recentRecipeRecyclerView.setAdapter(recentAdapter);

        // Initialize RecyclerView for Random Recipes
        randomArticleRecyclerView = findViewById(R.id.randomRecipeRecyclerView);
        randomArticleRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        randomAdapter = new RandomAdapter(this, randomModelList);
        randomArticleRecyclerView.setAdapter(randomAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Articles");

        databaseReference.orderByChild("CreatedAt").limitToLast(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recentModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey(); // Get the UID
                    String title = snapshot.child("ArticleTitle").getValue(String.class);
                    String category = snapshot.child("ArticleCategory").getValue(String.class);
                    String image = snapshot.child("ArticleImage").getValue(String.class);
                    String content = snapshot.child("ArticleContent").getValue(String.class);
                    String createdAt = snapshot.child("CreatedAt").getValue(String.class);

                    // Create RecentModel object with fetched data
                    RecentModel article = new RecentModel(uid, title, category, image, content, createdAt);
                    recentModelList.add(article);
                }
                Collections.reverse(recentModelList);
                recentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read data", databaseError.toException());
            }
        });

        // RANDOM RECIPES
        // Mengambil data dari Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<RandomModel> tempModelList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey(); // Get the UID
                    String title = snapshot.child("ArticleTitle").getValue(String.class);
                    String category = snapshot.child("ArticleCategory").getValue(String.class);
                    String image = snapshot.child("ArticleImage").getValue(String.class);
                    String content = snapshot.child("ArticleContent").getValue(String.class);
                    String createdAt = snapshot.child("CreatedAt").getValue(String.class);

                    Article article1 = snapshot.getValue(Article.class);
                    if (article1 != null) {
                        article1.setUID(uid);
                        articleList.add(article1);
                    }

                    // Create RandomModel object with fetched data
                    RandomModel article = new RandomModel(uid, title, category, content, image, createdAt);
                    tempModelList.add(article);
                }

                // Shuffle the list to randomize
                Collections.shuffle(tempModelList);

                // Clear the existing list and add 6 random items
                randomModelList.clear();
                for (int i = 0; i < Math.min(6, tempModelList.size()); i++) {
                    randomModelList.add(tempModelList.get(i));
                }

                // Notify the adapter about the data change
                randomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read data", databaseError.toException());
            }
        });
    }
}
