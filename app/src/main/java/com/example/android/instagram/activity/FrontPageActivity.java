package com.example.android.instagram.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;


import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.controller.Controller;
import com.example.android.instagram.fragments.EditImageFragment;
import com.example.android.instagram.fragments.FilterListFragment;
import com.example.android.instagram.fragments.LoginFragment;
import com.example.android.instagram.fragments.SignUpFragment;
import com.example.android.instagram.fragments.ViewProfileFragment;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.Result;
import com.example.android.instagram.model.User;

import android.support.v4.view.ViewPager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
//import static com.example.android.instagram.fragments.LoginFragment.Name;
//import static com.example.android.instagram.fragments.LoginFragment.Password;

public class FrontPageActivity extends AppCompatActivity {

//    SharedPreferences sharedPreferences;
    private Controller controller;
    private Button buttonSignUp, loginIntent;
    private EditText userName, password, email, firstName, lastName;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        buttonSignUp = (Button) findViewById(R.id.signup_button);

        userName = (EditText) findViewById(R.id.signup_username);
        password = (EditText) findViewById(R.id.signup_password);
        email = (EditText) findViewById(R.id.signup_email);
        firstName = (EditText) findViewById(R.id.signup_firstname);
        lastName = (EditText) findViewById(R.id.signup_lastname);
        loginIntent = (Button) findViewById(R.id.login_intent);


        loginIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSignUp.setVisibility(GONE);
                loginIntent.setVisibility(GONE);
                FragmentManager fm = getSupportFragmentManager();
                LoginFragment fr = new LoginFragment();
                fm.beginTransaction().replace(R.id.container,fr).commit();
            }
        });
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignUp();
            }
        });
        controller = (Controller) getApplicationContext();

//        if(Name.length() > 0 && Password.length() > 0){
//            APIService service = HttpClientService.getClient().create(APIService.class);
//
//
//
//            sharedPreferences = getSharedPreferences(LoginFragment.mypreference, Context.MODE_PRIVATE);
//
//            Result result = new Result();
//
//            if (sharedPreferences.contains(Name)) {
//                userName.setText(sharedPreferences.getString(Name, ""));
//            }
//            if (sharedPreferences.contains(Password)) {
//                password.setText(sharedPreferences.getString(Password, ""));
//
//            }
//
//            result.setUsername(Name);
//            result.setPassword(Password);
//
//            Call<Result> call = service.createUser(result);
//
//
//
//            call.enqueue(new Callback<Result>() {
//                @Override
//                public void onResponse( Call<Result> call, Response<Result> userResponse) {
////                    progressDialog.dismiss();
//
//                    if(userResponse.isSuccessful()) {
//                        Toast.makeText(getApplicationContext(), "login success", Toast.LENGTH_LONG).show();
////                        userId = userResponse.body().getUserId();
////                        Log.e("value",userId+"");
////                        getToken();
//                        //    SharedPreference.getInstance(getApplicationContext()).;
//                        // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                    }
//                    else{
//                        Toast.makeText(getApplicationContext(), "login not correct", Toast.LENGTH_LONG).show();
//                    }
//
//                }
//
//
//
//                @Override
//                public void onFailure( Call<Result> call, Throwable t) {
////                    progressDialog.dismiss();
//                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
//                }
//            });
//
//
//        }
    }

    User user = null;

    private void userSignUp() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String user_name = userName.getText().toString().trim();
        String user_password = password.getText().toString().trim();
        String email_id = email.getText().toString().trim();
        String first_name = firstName.getText().toString().trim();
        String last_name = lastName.getText().toString().trim();

        if (user_name.isEmpty()) {
            progressDialog.dismiss();
            userName.setError("Username should not be empty");
            userName.requestFocus();
            return;
        }

        if (user_password.isEmpty()) {
            progressDialog.dismiss();
            password.setError("Password should not be empty");
            password.requestFocus();
            return;
        }

        if (email_id.isEmpty()) {
            progressDialog.dismiss();
            email.setError("Email should not be empty");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email_id).matches()) {
            progressDialog.dismiss();
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }

        if (first_name.isEmpty()) {
            progressDialog.dismiss();
            firstName.setError("First Name should not be empty");
            firstName.requestFocus();
            return;
        }

        if (last_name.isEmpty()) {
            progressDialog.dismiss();
            lastName.setError("Last Name should not be empty");
            lastName.requestFocus();
            return;
        }


        APIService service = HttpClientService.getClient().create(APIService.class);

        user = new User();

        controller.setUser(user);

        user.setUsername(user_name);
        user.setPassword(user_password);
        user.setEmail(email_id);
        user.setFirst_name(first_name);
        user.setLast_name(last_name);

        Call<User> call = service.createUser(controller.getUser());


        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> userResponse) {
                progressDialog.dismiss();
                if (userResponse.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Verify Your Email", Toast.LENGTH_LONG).show();
                    // SharedPreference.getInstance(getApplicationContext()).userLogin(user);
                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Response error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
            }
        });

    }

}


