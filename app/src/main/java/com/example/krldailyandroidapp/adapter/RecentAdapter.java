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
import com.example.krldailyandroidapp.model.RecentModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {

    private Context context;
    private List<RecentModel> recentModelList;

    public RecentAdapter(Context context, List<RecentModel> recentModelList) {
        this.context = context;
        this.recentModelList = recentModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentModel article = recentModelList.get(position);
        holder.titleTextView.setText(article.getArticleTitle());
        holder.categoryTextView.setText(article.getArticleCategory());
        String formattedDate = formatTimestamp(article.getCreatedAt());
        holder.dateTextView.setText(formattedDate);

        Glide.with(context)
                .load(article.getArticleImage())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.imageView);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            // Navigate to RecipeDetailActivity and pass UID
            Intent intent = new Intent(context, ArticleDetailActivity.class);
            intent.putExtra("articleId", article.getUID());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recentModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, categoryTextView, contentTextView, dateTextView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.article_recent_title);
            categoryTextView = itemView.findViewById(R.id.article_recent_category);
            dateTextView = itemView.findViewById(R.id.article_recent_created_at);
            imageView = itemView.findViewById(R.id.article_recent_img);
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
