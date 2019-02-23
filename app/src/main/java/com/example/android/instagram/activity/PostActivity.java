package com.example.android.instagram.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.android.instagram.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PostActivity extends AppCompatActivity {

    private ImageView postImage;
    public static final int PICK_IMAGE = 1;
    Intent  intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


//        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
//        postImage.setImageBitmap(bitmap);

        File fileLocation = new File(String.valueOf(getRealPathFromURI(FilterActivity.image_uri))); //file path, which can be String, or Uri

        if(fileLocation.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(fileLocation.getAbsolutePath());

            postImage = (ImageView) findViewById(R.id.post_image_view);

            postImage.setImageBitmap(myBitmap);

        }

//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("multipart/form-data"), fileLocation);
//
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("picture", fileLocation.getName(), requestFile);
//
////        Picasso.with(this).load(fileLocation).into(postImage);
//        Picasso.with(this)
//                .load(String.valueOf(body))
//                .placeholder(R.drawable.message)
//                .fit()
//                .centerCrop()
//                .into(postImage);
//    }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
