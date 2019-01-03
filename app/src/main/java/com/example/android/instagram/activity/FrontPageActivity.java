package com.example.android.instagram.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.instagram.R;
import com.example.android.instagram.activity.LoginActivity;
import com.example.android.instagram.activity.SignUpActivity;

public class FrontPageActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonSignUp, buttonLogIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_page);


        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonLogIn = (Button) findViewById(R.id.buttonLogin);

        buttonSignUp.setOnClickListener(this);
        buttonLogIn.setOnClickListener(this);
    }

    public void onClick(View v){

        if (v == buttonLogIn) {

            startActivity(new Intent(this, LoginActivity.class));

        } else if (v == buttonSignUp) {

            startActivity(new Intent(this, SignUpActivity.class));

        }
    }
}
