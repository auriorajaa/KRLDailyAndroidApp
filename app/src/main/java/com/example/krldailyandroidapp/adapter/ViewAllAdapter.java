package com.example.krldailyandroidapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.krldailyandroidapp.ArticleDetailActivity;
import com.example.krldailyandroidapp.R;
import com.example.krldailyandroidapp.model.ViewAllModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder>{
    private Context context;
    private List<ViewAllModel> viewAllModelList;

    public ViewAllAdapter(Context context, List<ViewAllModel> viewAllModelList) {
        this.context = context;
        this.viewAllModelList = viewAllModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewAllModel viewAllModel = viewAllModelList.get(position);

        // Bind data to views
        // Load gambar menggunakan Glide
        String imageUrl = viewAllModel.getArticleImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.mipmap.ic_launcher) // Placeholder jika gambar tidak tersedia
                    .error(R.mipmap.ic_launcher) // Gambar error jika URL tidak valid
                    .into(holder.RecipeImage);
        } else {
            // Jika URL gambar null atau kosong, tampilkan placeholder
            holder.RecipeImage.setImageResource(R.mipmap.ic_launcher);
        }

        holder.RecipeTitle.setText(viewAllModel.getArticleTitle());
        holder.RecipeCategory.setText(viewAllModel.getArticleCategory());

        String formattedDate = formatTimestamp(viewAllModel.getCreatedAt());

        holder.RecipeServings.setText(formattedDate);

        holder.itemView.setOnClickListener(v -> {
            String uid = viewAllModel.getUID();
            if (uid != null) {
                Intent intent = new Intent(context, ArticleDetailActivity.class);
                intent.putExtra("articleId", uid);
                context.startActivity(intent);
            } else {
                // Tangani kasus UID null
                Toast.makeText(context, "Article UID is missing", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return viewAllModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView RecipeImage;
        TextView RecipeTitle, RecipeCategory, RecipeServings, UID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            RecipeImage = itemView.findViewById(R.id.view_all_img);
            RecipeTitle = itemView.findViewById(R.id.view_all_name);
            RecipeCategory = itemView.findViewById(R.id.view_all_category);
            RecipeServings = itemView.findViewById(R.id.view_all_servings);
            UID = itemView.findViewById(R.id.UID); // Initialize UID TextView
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
