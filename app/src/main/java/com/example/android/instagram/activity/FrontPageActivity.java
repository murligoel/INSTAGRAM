package com.example.android.instagram.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.android.instagram.R;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.fragments.SignUpFragment;

import static android.view.View.GONE;

public class FrontPageActivity extends AppCompatActivity {

    private Button buttonSignUp, buttonLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);


        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonLogIn = (Button) findViewById(R.id.buttonLogin);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLogIn.setVisibility(GONE);
                buttonSignUp.setVisibility(GONE);
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
                FragmentManager fm = getSupportFragmentManager();
                LoginFragment fr = new LoginFragment();
                fm.beginTransaction().replace(R.id.container,fr).commit();
            }
        });

    }
}


