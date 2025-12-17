package com.example.portalberita;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private List<News> newsList;

    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);

        holder.tvTitle.setText(news.getTitle());
        holder.tvDesc.setText(news.getDescription());
        holder.tvDate.setText(news.getDate());

        // Load image from drawable
        String imageName = news.getImage();
        if (imageName != null && !imageName.isEmpty()) {
            int imageResId = context.getResources().getIdentifier(
                    imageName,
                    "drawable",
                    context.getPackageName()
            );

            if (imageResId != 0) {
                holder.ivThumb.setImageResource(imageResId);
            } else {
                // Fallback to placeholder if image not found
                holder.ivThumb.setImageResource(R.drawable.placeholder_news);
            }
        } else {
            holder.ivThumb.setImageResource(R.drawable.placeholder_news);
        }

        // Click listener to open detail
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("news_id", news.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void updateData(List<News> newNewsList) {
        this.newsList = newNewsList;
        notifyDataSetChanged();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumb;
        TextView tvTitle, tvDesc, tvDate;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumb = itemView.findViewById(R.id.ivNewsThumb);
            tvTitle = itemView.findViewById(R.id.tvNewsTitle);
            tvDesc = itemView.findViewById(R.id.tvNewsDesc);
            tvDate = itemView.findViewById(R.id.tvNewsDate);
        }
    }
}