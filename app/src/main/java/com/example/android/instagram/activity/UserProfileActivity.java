package com.example.android.instagram.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.android.instagram.R;
import com.example.android.instagram.fragments.EditProfileFragment;
import com.example.android.instagram.fragments.ViewProfileFragment;

import static android.view.View.GONE;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static Context contextOfApplication;
    private Button imageFromGallery,userProfile;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

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
                                userProfile.setVisibility(GONE);
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
        userProfile = (Button) findViewById(R.id.user_profile);
        contextOfApplication = getApplicationContext();

        imageFromGallery.setOnClickListener(this);
        userProfile.setOnClickListener(this);
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

        if(v == userProfile){
//            imageFromGallery.setVisibility(GONE);
//            userProfile.setVisibility(GONE);
//            FragmentManager fm = getSupportFragmentManager();
//            EditProfileFragment fr = new EditProfileFragment();
//            fm.beginTransaction().replace(R.id.drawer_layout,fr).commit();
        }
    }
}
