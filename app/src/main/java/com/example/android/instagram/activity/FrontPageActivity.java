package com.example.android.instagram.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.android.instagram.R;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.fragments.SignUpFragment;
import com.example.android.instagram.activity.FilterActivity;

import static android.view.View.GONE;

public class FrontPageActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonSignUp, buttonLogIn, buttonFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_page);


        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonLogIn = (Button) findViewById(R.id.buttonLogin);
        buttonFilter = (Button) findViewById(R.id.buttonFilter);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLogIn.setVisibility(GONE);
                buttonSignUp.setVisibility(GONE);
                buttonFilter.setVisibility(GONE);
                FragmentManager fm = getSupportFragmentManager();
                SignUpFragment fr = new SignUpFragment();
                fm.beginTransaction().replace(R.id.container,fr).commit();
            }
        });
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLogIn.setVisibility(GONE);
                buttonSignUp.setVisibility(GONE);
                buttonFilter.setVisibility(GONE);
                FragmentManager fm = getSupportFragmentManager();
                LoginFragment fr = new LoginFragment();
                fm.beginTransaction().replace(R.id.container,fr).commit();
            }
        });

        buttonFilter.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v == buttonFilter) {
            Intent intent = new Intent(FrontPageActivity.this, FilterActivity.class);
            startActivity(intent);
        }

    }
}


