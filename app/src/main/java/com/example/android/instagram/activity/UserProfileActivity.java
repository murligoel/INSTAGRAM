package com.example.android.instagram.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.instagram.R;
import com.example.android.instagram.fragments.ProfileFragment;
import com.example.android.instagram.fragments.SignUpFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.view.View.GONE;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static Context contextOfApplication;
    private Button imageFromGallery,userProfile;
    private DrawerLayout mDrawerLayout;

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

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


        imageFromGallery = (Button) findViewById(R.id.images_from_gallery);
        userProfile = (Button) findViewById(R.id.user_profile);
        contextOfApplication = getApplicationContext();

        imageFromGallery.setOnClickListener(this);
        userProfile.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

//            case R.id.nav_profile:
//
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {

        if(v == imageFromGallery){
//            openGallery();
            Intent intent = new Intent(UserProfileActivity.this, FilterActivity.class);
            startActivity(intent);
        }

        if(v == userProfile){
            imageFromGallery.setVisibility(GONE);
            userProfile.setVisibility(GONE);
            FragmentManager fm = getSupportFragmentManager();
            ProfileFragment fr = new ProfileFragment();
            fm.beginTransaction().replace(R.id.drawer_layout,fr).commit();
        }
    }
}
