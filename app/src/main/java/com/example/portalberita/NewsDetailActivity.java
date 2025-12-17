package com.example.portalberita;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewsDetailActivity extends AppCompatActivity {

    private ImageView btnBack, ivNewsImage;
    private TextView tvNewsTitle, tvNewsDate, tvNewsContent;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        // Initialize
        dbHelper = new DatabaseHelper(this);

        // Find views
        btnBack = findViewById(R.id.btnBack);
        ivNewsImage = findViewById(R.id.ivNewsImage);
        tvNewsTitle = findViewById(R.id.tvNewsTitle);
        tvNewsDate = findViewById(R.id.tvNewsDate);
        tvNewsContent = findViewById(R.id.tvNewsContent);

        // Get news ID from intent
        int newsId = getIntent().getIntExtra("news_id", -1);

        if (newsId != -1) {
            loadNewsDetail(newsId);
        }

        // Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadNewsDetail(int newsId) {
        News news = dbHelper.getNewsById(newsId);

        if (news != null) {
            tvNewsTitle.setText(news.getTitle());
            tvNewsDate.setText(news.getDate());
            tvNewsContent.setText(news.getContent());

            // Load image from drawable
            String imageName = news.getImage();
            if (imageName != null && !imageName.isEmpty()) {
                int imageResId = getResources().getIdentifier(
                        imageName,
                        "drawable",
                        getPackageName()
                );

                if (imageResId != 0) {
                    ivNewsImage.setImageResource(imageResId);
                } else {
                    // Fallback to placeholder if image not found
                    ivNewsImage.setImageResource(R.drawable.placeholder_news);
                }
            } else {
                ivNewsImage.setImageResource(R.drawable.placeholder_news);
            }
        }
    }
}