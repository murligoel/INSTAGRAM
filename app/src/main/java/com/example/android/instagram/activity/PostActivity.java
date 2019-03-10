package com.example.android.instagram.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.httpservice.HttpClientService;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity {

    private EditText caption;
    private Button post;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        caption = (EditText) findViewById(R.id.caption_for_user_post);
        post = (Button) findViewById(R.id.user_post_post) ;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user_post);
        //to set the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implemented by activity
                onBackPressed();
            }
        });

        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);


        File fileLocation = new File(String.valueOf(getRealPathFromURI(FilterActivity.image_uri))); //file path, which can be String, or Uri

        if(fileLocation.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(fileLocation.getAbsolutePath());

            ImageView postImage = (ImageView) findViewById(R.id.post_image_view);

            postImage.setImageBitmap(myBitmap);

        }

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });

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

    private void post() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Post....");
        progressDialog.show();

        String post_caption = caption.getText().toString().trim();

        String token = sharedPref.getString("usertoken","");

        APIService service = HttpClientService.getClient().create(APIService.class);

        if(FilterActivity.image_uri != null) {
            File file = new File(String.valueOf(getRealPathFromURI(FilterActivity.image_uri)));
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

            RequestBody caption_post = RequestBody.create(MediaType.parse("text/plain"), post_caption);

            Call<ResponseBody> call = service.createPost("JWT " + token, caption_post, body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> userResponse) {
                    progressDialog.dismiss();
                    if (userResponse.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(PostActivity.this,UserProfileActivity.class));

                        // SharedPreference.getInstance(getApplicationContext()).userLogin(user);
                        // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "error1", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "image required", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(PostActivity.this,FilterActivity.class));
        finish();

    }
}
