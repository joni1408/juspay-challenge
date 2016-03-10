package com.prempal.juspay_challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView hinduImage = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this).load("https://d1iiooxwdowqwr.cloudfront.net/pub/wpscreenshots/android-logo-512-512.png").into(hinduImage);
        hinduImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ArticleListActivity.class);
                intent.putExtra("title", "The Hindu");
                intent.putExtra("link", "http://www.thehindu.com/news/cities/Delhi/?service=rss");
                startActivity(intent);
            }
        });
    }
}
