package com.example.android.instagram.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.User;

import retrofit2.Callback;
import retrofit2.Response;


public class SignUpFragment extends Fragment implements View.OnClickListener{

    private TextView login;
    private Button buttonSignUp;
    private EditText userName, password, email, firstName, lastName;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_sign_up, container, false);
        buttonSignUp = (Button) v.findViewById(R.id.signup_button);

        userName = (EditText) v.findViewById(R.id.signup_username);
        password = (EditText) v.findViewById(R.id.signup_password);
        email = (EditText) v.findViewById(R.id.signup_email);
        firstName = (EditText) v.findViewById(R.id.signup_firstname);
        lastName = (EditText) v.findViewById(R.id.signup_lastname);
        buttonSignUp.setOnClickListener(this);

        login = (TextView) v.findViewById(R.id.go_to_login);

        login.setOnClickListener(this);

        return v;
    }

    private void userSignUp(){

        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                "Signing Up",
                "Please wait...",
                true);

        APIService service = HttpClientService.getClient().create(APIService.class);

        final User user = new User();

        user.setUsername( userName.getText().toString().trim());
        user.setPassword( password.getText().toString().trim());
        user.setEmail(email.getText().toString().trim());
        user.setFirst_name(firstName.getText().toString().trim());;
        user.setLast_name(lastName.getText().toString().trim());

        retrofit2.Call<User> call = service.createUser(user);



        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> userResponse) {
                progressDialog.dismiss();
                if(userResponse.isSuccessful()) {
                    Toast.makeText(getActivity(), "successful", Toast.LENGTH_LONG).show();
                    // SharedPreference.getInstance(getApplicationContext()).userLogin(user);
                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                else{
                    Toast.makeText(getActivity(), "error1", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View v) {

        if(v == buttonSignUp){
              userSignUp();
        }
        else if (v == login) {

            Intent i = new Intent(getActivity(), LoginFragment.class);
            startActivity(i);
        }
    }
    }

