package com.example.android.instagram.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.instagram.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        login = (TextView) findViewById(R.id.go_to_login);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == login) {

            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
