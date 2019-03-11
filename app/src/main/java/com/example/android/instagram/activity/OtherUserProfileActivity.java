package com.example.android.instagram.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.adapter.AddFriendAdapter;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.Profile;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherUserProfileActivity extends AppCompatActivity {

    private CircleImageView circleProfileImage;
    private TextView userProfileBio,userProfileName;
    private Context mContext;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_other_user_profile);
        //to set the action bar
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Edit Profile");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implemented by activity
                onBackPressed();
            }
        });

        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        circleProfileImage = (CircleImageView) findViewById(R.id.other_user_profile_image);
        userProfileBio = (TextView) findViewById(R.id.other_user_profile_bio);
        userProfileName = (TextView) findViewById(R.id.other_user_profile_name);

        viewProfile();
    }

    private void viewProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Profile");
        progressDialog.show();

        String token = sharedPref.getString("usertoken","");

        APIService service = HttpClientService.getClient().create(APIService.class);
        Call<Profile> call = service.viewDifferentUserProfile(AddFriendAdapter.user_id,"JWT " +token);

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
                    userProfileBio.setText("Bio: " +user_bio);
                    String user_name = userResponse.body().getUsername();
                    getSupportActionBar().setTitle(user_name);
                    userProfileName.setText("Name: "+user_name);
                    String user_image = userResponse.body().getImage();
                    Picasso.with(mContext)
                            .load(user_image)
//                           .placeholder(R.drawable.progress_animation)
                            .fit()
                            .centerCrop()
                            .into(circleProfileImage);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(OtherUserProfileActivity.this,UserProfileActivity.class));
        finish();

    }
}
