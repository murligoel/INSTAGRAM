package com.example.android.instagram.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.controller.Controller;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpFragment extends Fragment  {

//    private Controller controller;
//    private Button buttonSignUp, loginIntent;
//    private EditText userName, password, email, firstName, lastName;
//    private ProgressDialog progressDialog;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
//        buttonSignUp = (Button) v.findViewById(R.id.signup_button);
//
//        userName = (EditText) v.findViewById(R.id.signup_username);
//        password = (EditText) v.findViewById(R.id.signup_password);
//        email = (EditText) v.findViewById(R.id.signup_email);
//        firstName = (EditText) v.findViewById(R.id.signup_firstname);
//        lastName = (EditText) v.findViewById(R.id.signup_lastname);
//        loginIntent = (Button) v.findViewById(R.id.login_intent);
//
//
//        loginIntent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                LoginFragment fr = new LoginFragment();
//                fm.beginTransaction().replace(R.id.container, fr).commit();
//
//            }
//        });
//        buttonSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userSignUp();
//            }
//        });
//        controller = (Controller) getActivity().getApplicationContext();
        return v;
    }


//    User user = null;
//
//    private void userSignUp() {
//
//        progressDialog = new ProgressDialog(this.getContext());
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        String user_name = userName.getText().toString().trim();
//        String user_password = password.getText().toString().trim();
//        String email_id = email.getText().toString().trim();
//        String first_name = firstName.getText().toString().trim();
//        String last_name = lastName.getText().toString().trim();
//
//        if (user_name.isEmpty()) {
//            progressDialog.dismiss();
//            userName.setError("Username should not be empty");
//            userName.requestFocus();
//            return;
//        }
//
//        if (user_password.isEmpty()) {
//            progressDialog.dismiss();
//            password.setError("Password should not be empty");
//            password.requestFocus();
//            return;
//        }
//
//        if (email_id.isEmpty()) {
//            progressDialog.dismiss();
//            email.setError("Email should not be empty");
//            email.requestFocus();
//            return;
//        }
//
//        if (!Patterns.EMAIL_ADDRESS.matcher(email_id).matches()) {
//            progressDialog.dismiss();
//            email.setError("Enter a valid email");
//            email.requestFocus();
//            return;
//        }
//
//        if (first_name.isEmpty()) {
//            progressDialog.dismiss();
//            firstName.setError("First Name should not be empty");
//            firstName.requestFocus();
//            return;
//        }
//
//        if (last_name.isEmpty()) {
//            progressDialog.dismiss();
//            lastName.setError("Last Name should not be empty");
//            lastName.requestFocus();
//            return;
//        }
//
//
//        APIService service = HttpClientService.getClient().create(APIService.class);
//
//        user = new User();
//
//        controller.setUser(user);
//
//        user.setUsername(user_name);
//        user.setPassword(user_password);
//        user.setEmail(email_id);
//        user.setFirst_name(first_name);
//        user.setLast_name(last_name);
//
//        Call<User> call = service.createUser(controller.getUser());
//
//
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> userResponse) {
//                progressDialog.dismiss();
//                if (userResponse.isSuccessful()) {
//                    Toast.makeText(getActivity(), "successful", Toast.LENGTH_LONG).show();
//                    // SharedPreference.getInstance(getApplicationContext()).userLogin(user);
//                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                } else {
//                    Toast.makeText(getActivity(), "error1", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                progressDialog.dismiss();
//                Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }

}


