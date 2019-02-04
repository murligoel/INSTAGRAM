package com.example.android.instagram.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.android.instagram.R;

public class PostActivity extends AppCompatActivity {

    private ImageView postImage;
    Intent  intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postImage = (ImageView) findViewById(R.id.post_image_view);
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
        postImage.setImageBitmap(bitmap);
    }
}
