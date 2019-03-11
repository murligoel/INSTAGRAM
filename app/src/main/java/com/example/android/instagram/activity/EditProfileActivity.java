package com.example.android.instagram.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.fragments.ViewProfileFragment;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.Profile;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    public static final int IMAGE_GALLERY_REQUEST  = 20;
    private Button buttonProfile,pickProfileImage;
    private EditText bio, phoneNumber;
    private CircleImageView image;
    Uri image_uri;
    private ProgressDialog progressDialog;
    private Context mContext;
    MultipartBody.Part imagenull;
    RequestBody userName,userPhone;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        buttonProfile = (Button) findViewById(R.id.profile_button);
        image = (CircleImageView) findViewById(R.id.profile_image);
        pickProfileImage = (Button) findViewById(R.id.pick_profile_image_from_gallery);

        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit_profile);
        //to set the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implemented by activity
                onBackPressed();
            }
        });


        bio = (EditText) findViewById(R.id.profile_username);

        phoneNumber = (EditText) findViewById(R.id.profile_phone_number);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        pickProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        viewProfile();
    }

    private void viewProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Profile");
        progressDialog.show();


        APIService service = HttpClientService.getClient().create(APIService.class);

        String token = sharedPref.getString("usertoken","");
        String id = sharedPref.getString("userid","");

        Call<Profile> call = service.viewProfile(id,"JWT " +token);

        mContext = getApplicationContext();

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse( Call<Profile> call, Response<Profile> userResponse) {
                progressDialog.dismiss();

                if(userResponse.isSuccessful()) {
//                    Toast.makeText(getApplicationContext(), "success1", Toast.LENGTH_LONG).show();
                    //    SharedPreference.getInstance(getApplicationContext()).;
                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    String user_bio = userResponse.body().getBio();
                    bio.setText(user_bio);
                    String user_number = userResponse.body().getPhone_no();
                    phoneNumber.setText(user_number);
                    String user_image = userResponse.body().getImage();
                    Picasso.with(mContext)
                            .load(user_image)
//                           .placeholder(R.drawable.progress_animation)
                            .fit()
                            .centerCrop()
                            .into(image);

                }
                else{
                    Toast.makeText(getApplicationContext(), "profile not created", Toast.LENGTH_LONG).show();
                }

            }


            @Override
            public void onFailure( Call<Profile> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void openGallery(){

        // invoke the image gallery using an implicit intent
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        // where do we want to find the data?
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();




        // finally, get a URI representation
        Uri data = Uri.parse(pictureDirectoryPath);


        // set the data and type. Get all image types.
        photoPickerIntent.setDataAndType(data, "image/*");

        // we will invoke this activity and get something back from it
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            // if we are here everything processed successfully.
            if(requestCode == IMAGE_GALLERY_REQUEST){
                // if we are here we are hearing back from the image gallery.

                // the address of the image from sd card
                Uri imageUri = data.getData();
                image_uri = imageUri;

                // declare a stream to read the image data from the SD card.
                InputStream inputStream;

                // we are getting an input stream, based on the URI of the image
                try {
                    Context applicationContext = UserProfileActivity.getContextOfApplication();
                    inputStream =applicationContext.getContentResolver().openInputStream(imageUri);

                    // get a bitmap from the stream.
                    Bitmap img = BitmapFactory.decodeStream(inputStream);
                    image.setImageBitmap(img);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
//                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }
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

    private void updateProfile(){
        progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String token = sharedPref.getString("usertoken","");
        String id = sharedPref.getString("userid","");

        String user_name = bio.getText().toString().trim();
        String phone_number = phoneNumber.getText().toString().trim();

        APIService service = HttpClientService.getClient().create(APIService.class);


        if(image_uri!=null) {
            File file = new File(String.valueOf(getRealPathFromURI(image_uri)));
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            imagenull = body;




            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), user_name);
            userName = name;
            RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), phone_number);
            userPhone = phone;
            Call<ResponseBody> call = service.putPost(id, name, phone, "JWT " + token, body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> userResponse) {
                    progressDialog.dismiss();
                    if (userResponse.isSuccessful()) {
//                        Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_LONG).show();
                        // SharedPreference.getInstance(getApplicationContext()).userLogin(user);
                        // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                    FragmentManager fm = getSupportFragmentManager();
//                    ViewProfileFragment fr = new ViewProfileFragment();
//                    fm.beginTransaction().replace(R.id.container_edit_profile,fr).commit();
                        startActivity(new Intent(EditProfileActivity.this,ViewProfileActivity.class));
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
        else{
//            progressDialog.dismiss();
//            Toast.makeText(getApplicationContext(), "image required", Toast.LENGTH_SHORT).show();

            Call<ResponseBody> call = service.putPost(LoginFragment.get_user_id(),userName,userPhone,"JWT " + token, imagenull);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> userResponse) {
                    progressDialog.dismiss();
                    if (userResponse.isSuccessful()) {
//                        Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_LONG).show();
                        // SharedPreference.getInstance(getApplicationContext()).userLogin(user);
                        // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                    FragmentManager fm = getSupportFragmentManager();
//                    ViewProfileFragment fr = new ViewProfileFragment();
//                    fm.beginTransaction().replace(R.id.container_edit_profile,fr).commit();
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
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(EditProfileActivity.this,ViewProfileActivity.class));
        finish();

    }

}
