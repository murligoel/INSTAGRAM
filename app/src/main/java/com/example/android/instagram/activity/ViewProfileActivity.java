package com.example.android.instagram.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.UserHandle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.adapter.PostAdapter;
import com.example.android.instagram.adapter.UserPostAdapter;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.CommentList;
import com.example.android.instagram.model.Post;
import com.example.android.instagram.model.Profile;
import com.example.android.instagram.model.UserPost;
import com.example.android.instagram.model.UserPostList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProfileActivity extends AppCompatActivity {

    private CircleImageView circularProfileImage;
    private TextView userProfileBio,userProfileName;
    private Button editYourProfile;
    private Context mContext;
    private ArrayList<UserPost> mPost = new ArrayList<>() ;
    //    private Post mPost;
    private RecyclerView recyclerView;
    private UserPostAdapter eAdapter;
    private ProgressDialog pDialog;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        circularProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        userProfileBio = (TextView) findViewById(R.id.profile_bio);
        userProfileName = (TextView) findViewById(R.id.profile_name);
        editYourProfile = (Button) findViewById(R.id.edit_your_profile);

        sharedPref =  getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_view_profile);
        //to set the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implemented by activity
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_user_post);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ViewProfileActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        editYourProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        viewProfile();
    }

    private void viewProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Profile");
        progressDialog.show();

        String token = sharedPref.getString("usertoken","");
        String userId = sharedPref.getString("userid","");


        APIService service = HttpClientService.getClient().create(APIService.class);
        Call<Profile> call = service.viewProfile(userId,"JWT " +token);

        mContext = getApplicationContext();

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse( Call<Profile> call, Response<Profile> userResponse) {
                progressDialog.dismiss();

                if(userResponse.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "success1", Toast.LENGTH_LONG).show();
                    //    SharedPreference.getInstance(getApplicationContext()).;
                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    String user_bio = userResponse.body().getBio();
                    userProfileBio.setText("Bio: " +user_bio);
                    String user_name = userResponse.body().getUsername();
                    userProfileName.setText("Name: "+user_name);
                    String user_image = userResponse.body().getImage();
                    Picasso.with(mContext)
                            .load(user_image)
//                           .placeholder(R.drawable.progress_animation)
                            .fit()
                            .centerCrop()
                            .into(circularProfileImage);
                    creatingUserPost();

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

    private void creatingUserPost() {

        pDialog = new ProgressDialog(ViewProfileActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        String token = sharedPref.getString("usertoken","");

        APIService service = HttpClientService.getClient().create(APIService.class);
        Call<UserPostList> call = service.viewUserPost("JWT " +token);

        call.enqueue(new Callback<UserPostList>() {
            @Override
            public void onResponse(Call<UserPostList> call, Response<UserPostList> response) {
                pDialog.dismiss();

                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "success1", Toast.LENGTH_LONG).show();

                    mPost = response.body().getPost();

                    String[] posts = new String[mPost.size()];

                    eAdapter = new UserPostAdapter(mPost);
                    recyclerView.setAdapter(eAdapter);

                }
                else {
                    Toast.makeText(getApplicationContext(), "error1", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserPostList> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(ViewProfileActivity.this,UserProfileActivity.class));
        finish();

    }
}
