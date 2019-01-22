package com.example.android.instagram.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.instagram.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int IMAGE_GALLERY_REQUEST  = 20;
    private Button imageFromGallery;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        imageFromGallery = (Button) findViewById(R.id.images_from_gallery);
//
//        // get a reference to the image view that holds the image that the user will see.
//        img = (ImageView) findViewById(R.id.imgPicture);
//
        imageFromGallery.setOnClickListener(this);
    }


//    public void openGallery(){
//
//        // invoke the image gallery using an implicit intent
//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//
//        // where do we want to find the data?
//        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        String pictureDirectoryPath = pictureDirectory.getPath();
//
//
//        // finally, get a URI representation
//        Uri data = Uri.parse(pictureDirectoryPath);
//
//        // set the data and type. Get all image types.
//        photoPickerIntent.setDataAndType(data, "image/*");
//
//        // we will invoke this activity and get something back from it
//        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        if(resultCode == RESULT_OK){
//            // if we are here everything processed successfully.
//            if(requestCode == IMAGE_GALLERY_REQUEST){
//                // if we are here we are hearing back from the image gallery.
//
//                // the address of the image from sd card
//                Uri imageUri = data.getData();
//
//                // declare a stream to read the image data from the SD card.
//                InputStream inputStream;
//
//                // we are getting an input stream, based on the URI of the image
//                try {
//                    inputStream = getContentResolver().openInputStream(imageUri);
//
//                    // get a bitmap from the stream.
//                    Bitmap image = BitmapFactory.decodeStream(inputStream);
//                    img.setImageBitmap(image);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }

    @Override
    public void onClick(View v) {

        if(v == imageFromGallery){
//            openGallery();
            Intent intent = new Intent(UserProfileActivity.this, FilterActivity.class);
            startActivity(intent);
        }
    }
}
