package com.example.krldailyandroidapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.krldailyandroidapp.R;
import com.example.krldailyandroidapp.model.Article;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>{
    private List<Article> articleList;
    private List<Article> filteredList;
    private OnArticleClickListener onArticleClickListener;

    public ArticleAdapter(List<Article> articleList) {
        this.articleList = articleList;
        this.filteredList = new ArrayList<>(articleList); // Make a copy for filtering
    }

    public interface OnArticleClickListener {
        void onArticleClick(Article article);
    }

    public void setOnArticleClickListener(OnArticleClickListener listener) {
        this.onArticleClickListener = listener;
    }

    @NonNull
    @Override
    public ArticleAdapter.ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleAdapter.ArticleViewHolder holder, int position) {
        Article guideBook = filteredList.get(position);
        holder.titleTextView.setText(guideBook.getArticleTitle());
        holder.categoryTextView.setText(guideBook.getArticleCategory());
//        holder.contentTextView.setText(guideBook.getArticleContent());

        // Format createdAt
        String formattedDate = formatTimestamp(guideBook.getCreatedAt());
        holder.dateTextView.setText(formattedDate);

        // Set UID
//        holder.uidTextView.setText("UID: " + guideBook.getUID());

        // Use Glide to load the image from URL
        Glide.with(holder.itemView.getContext())
                .load(guideBook.getArticleImage())
                .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image if not loaded yet
                .into(holder.imageView);

        // Set click listener on the whole card
        holder.itemView.setOnClickListener(v -> {
            if (onArticleClickListener != null) {
                onArticleClickListener.onArticleClick(guideBook);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, categoryTextView, contentTextView, dateTextView, uidTextView;
        ImageView imageView;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.article_recent_title);
            categoryTextView = itemView.findViewById(R.id.article_recent_category);
//            contentTextView = itemView.findViewById(R.id.article_content);
            dateTextView = itemView.findViewById(R.id.article_recent_created_at);
            imageView = itemView.findViewById(R.id.article_recent_img);
        }
    }

    public void updateArticles(List<Article> newArticle) {
        filteredList.clear();
        filteredList.addAll(newArticle);
        notifyDataSetChanged();
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
