package com.example.krldailyandroidapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.krldailyandroidapp.ArticleDetailActivity;
import com.example.krldailyandroidapp.R;
import com.example.krldailyandroidapp.model.RandomModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class RandomAdapter extends RecyclerView.Adapter<RandomAdapter.ViewHolder>{
    private Context context;
    private List<RandomModel> randomModelList;

    public RandomAdapter(Context context, List<RandomModel> randomModelList) {
        this.context = context;
        this.randomModelList = randomModelList;
    }

    @NonNull
    @Override
    public RandomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RandomAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RandomAdapter.ViewHolder holder, int position) {
        RandomModel randomModel = randomModelList.get(position);

        // Bind data to views
        Glide.with(context).load(randomModel.getArticleImage()).into(holder.ArticleImage);
        holder.ArticleTitle.setText(randomModel.getRecipeTitle());
        holder.ArticleCategory.setText(randomModel.getArticleCategory());

        String formattedDate = formatTimestamp(randomModel.getCreatedAt());

        holder.CreatedAt.setText(formattedDate);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            // Navigate to RecipeDetailActivity and pass UID
            Intent intent = new Intent(context, ArticleDetailActivity.class);
            intent.putExtra("articleId", randomModel.getUID());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return randomModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ArticleImage;
        TextView ArticleTitle, ArticleCategory, CreatedAt, UID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ArticleImage = itemView.findViewById(R.id.article_recent_img);
            ArticleTitle = itemView.findViewById(R.id.article_recent_title);
            ArticleCategory = itemView.findViewById(R.id.article_recent_category);
            CreatedAt = itemView.findViewById(R.id.article_recent_created_at);
            UID = itemView.findViewById(R.id.article_recent_uid); // Initialize UID TextView
        }
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
}
