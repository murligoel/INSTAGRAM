package com.example.android.instagram.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.instagram.Adapter.PostAdapter;
import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.fragments.EditProfileFragment;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.fragments.ViewProfileFragment;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.Post;
import com.example.android.instagram.model.PostList;
import com.example.android.instagram.model.Profile;
import com.example.android.instagram.model.User;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static Context contextOfApplication;
    private Button imageFromGallery;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ArrayList<Post> mPost = new ArrayList<>() ;
//    private Post mPost;
    private RecyclerView recyclerView;
    private PostAdapter eAdapter;
    private ProgressDialog pDialog;


    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserProfileActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(UserProfileActivity.this, mDrawerLayout, R.string.Open, R.string.Close);
        mDrawerLayout.setDrawerListener(drawerToggle);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        switch (id){
                            case R.id.nav_profile:
                                imageFromGallery.setVisibility(GONE);
                                FragmentManager fm = getSupportFragmentManager();
                               ViewProfileFragment fr = new ViewProfileFragment();
                                fm.beginTransaction().replace(R.id.drawer_layout,fr).commit();
                                mDrawerLayout.closeDrawers();
                                return true;
                        }

                        return true;
                    }
                });


        imageFromGallery = (Button) findViewById(R.id.images_from_gallery);
        contextOfApplication = getApplicationContext();

        imageFromGallery.setOnClickListener(this);
       create_Post();
    }

    private void create_Post() {
        pDialog = new ProgressDialog(UserProfileActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        APIService service = HttpClientService.getClient().create(APIService.class);
        Call<ArrayList<Post>> call = service.viewPost("JWT " +LoginFragment.get_Token());

        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                pDialog.dismiss();

                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "success1", Toast.LENGTH_LONG).show();

                    mPost = response.body();

                    String[] posts = new String[mPost.size()];


                   /* for(int i = 0 ; i < mPost.size() ; i++ ){
                        String text_caption = mPost.get(i).getCaption();
                        String text_date = mPost.get(i).getDate_created();
                        String image_url = mPost.get(i).getPicture();
                        mPost.add(new Post(text_caption,image_url,text_date));
                    }*/


//                    PostList postList = response.body();
//                    JSONResponse jsonResponse = response.body();
//                    data = new ArrayList<>(Arrays.asList(jsonResponse.getAndroid()));
//                    mPost = new ArrayList<>(Arrays.asList(postList.getPost()));
//                    mPost = response.body().getPost();
                    eAdapter = new PostAdapter(mPost);
                    recyclerView.setAdapter(eAdapter);

                }
                else {
                    Toast.makeText(getApplicationContext(), "error1", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            //return true the click event will be consumed
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if(v == imageFromGallery){
            Intent intent = new Intent(UserProfileActivity.this, FilterActivity.class);
            startActivity(intent);
        }

    }
}
