package com.example.krldailyandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.krldailyandroidapp.adapter.ArticleAdapter;
import com.example.krldailyandroidapp.adapter.SearchAdapter;
import com.example.krldailyandroidapp.model.Article;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private DatabaseReference guideBookRef;
    private Spinner spinnerCategories;

    private List<Article> articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);
        recyclerView = findViewById(R.id.recyclerView);
        spinnerCategories = findViewById(R.id.spinner_categories);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        articleList = new ArrayList<>();
        adapter = new SearchAdapter(articleList);
        recyclerView.setAdapter(adapter);

        // Firebase reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        guideBookRef = database.getReference("Articles");

        // Load all articles initially
        loadArticles();

        // Search button click listener
        btnSearch.setOnClickListener(v -> performSearch());

        adapter.setOnArticleClickListener(new SearchAdapter.OnArticleClickListener() {
            @Override
            public void onArticleClick(Article article) {
                String articleId = article.getUID();

                if (articleId != null && !articleId.isEmpty()) {
                    Intent intent = new Intent(SearchActivity.this, ArticleDetailActivity.class);
                    intent.putExtra("articleId", articleId);
                    startActivity(intent);
                } else {
                    Toast.makeText(SearchActivity.this, "Article ID is null or empty", Toast.LENGTH_SHORT).show();
                }
            }
        });



        // Text change listener for search EditText
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not used
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterBySearchQuery(s.toString());
            }
        });

        // Setup category spinner
        setupCategorySpinner();

        // Setup bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_search) {
                return true;
            } else {
                return false;
            }
        });
    }

    private void loadArticles() {
        guideBookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                articleList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String UID = postSnapshot.getKey();
                    Article article = postSnapshot.getValue(Article.class);
                    if (article != null) {
                        article.setUID(UID);
                        articleList.add(article);
                    }
                }
                Collections.reverse(articleList);
                adapter.updateArticles(articleList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void performSearch() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        filterBySearchQuery(query);
    }

    private void filterBySearchQuery(String query) {
        List<Article> filteredList = new ArrayList<>();
        for (Article article : articleList) {
            if (article.getArticleTitle().toLowerCase().contains(query)
                    || article.getArticleContent().toLowerCase().contains(query)) {
                filteredList.add(article);
            }
        }
        adapter.updateArticles(filteredList);
    }

    private void filterByCategory(String category) {
        List<Article> filteredList = new ArrayList<>();
        for (Article article : articleList) {
            if (article.getArticleCategory().equalsIgnoreCase(category)) {
                filteredList.add(article);
            }
        }
        adapter.updateArticles(filteredList);
    }

    private void setupCategorySpinner() {
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(categoryAdapter);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
                if (!category.equals("All")) {
                    filterByCategory(category);
                } else {
                    // Update articles with the full guideBookList
                    adapter.updateArticles(articleList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not used
            }
        });
    }
}
