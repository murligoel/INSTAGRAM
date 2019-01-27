package com.example.android.instagram.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.activity.UserProfileActivity;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.Profile;
import com.example.android.instagram.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    public static final int IMAGE_GALLERY_REQUEST  = 20;
    private Button buttonProfile,pickProfileImage;
    private EditText userName, email, firstName, lastName, bio, phoneNumber;
    private ImageView image;
    private static Uri image_uri;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);

        buttonProfile = (Button) v.findViewById(R.id.profile_button);
        image = (ImageView) v.findViewById(R.id.profile_image);
        pickProfileImage = (Button) v.findViewById(R.id.pick_profile_image_from_gallery);

        userName = (EditText) v.findViewById(R.id.profile_username);
        email = (EditText) v.findViewById(R.id.profile_email);
        firstName = (EditText) v.findViewById(R.id.profile_first_name);
        lastName = (EditText) v.findViewById(R.id.profile_last_name);
        bio = (EditText) v.findViewById(R.id.profile_bio);
        phoneNumber = (EditText) v.findViewById(R.id.profile_phone_number);
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

        return v;
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
                    Toast.makeText(getActivity(), "Unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void updateProfile(){
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                "Updating",
                "Please wait...",
                true);


        String user_name = userName.getText().toString().trim();
        String email_id = email.getText().toString().trim();
        String first_name = firstName.getText().toString().trim();
        String last_name = lastName.getText().toString().trim();
        String about_bio = bio.getText().toString().trim();
        String phone_number = phoneNumber.getText().toString().trim();


        APIService service = HttpClientService.getClient().create(APIService.class);

        Profile profile = new Profile();

        profile.setUsername(user_name);
        profile.setEmail(email_id);
        profile.setFirst_name(first_name);
        profile.setLast_name(last_name);
        profile.setBio(about_bio);
        profile.setPhone_number(phone_number);
        profile.setImage_uri(image_uri);


        Call<Profile> call = service.putPost(LoginFragment.get_user_id(),profile,"JWT " +LoginFragment.get_Token());

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> userResponse) {
                progressDialog.dismiss();
                if(userResponse.isSuccessful()) {
                    Toast.makeText(getActivity(), "successful", Toast.LENGTH_LONG).show();
                    // SharedPreference.getInstance(getApplicationContext()).userLogin(user);
                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                else{
                    Toast.makeText(getActivity(), "error1", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
            }
        });
    }




}
