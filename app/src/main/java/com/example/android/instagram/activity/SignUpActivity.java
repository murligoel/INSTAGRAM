package com.example.android.instagram.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.instagram.R;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.interfaces.APIService;
import com.example.android.instagram.model.User;

import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView login;
    private Button buttonSignUp;
    private EditText userName, password, email, firstName, lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        buttonSignUp = (Button) findViewById(R.id.signup_button);

        userName = (EditText) findViewById(R.id.signup_username);
        password = (EditText) findViewById(R.id.signup_password);
        email = (EditText) findViewById(R.id.signup_email);
        firstName = (EditText) findViewById(R.id.signup_firstname);
        lastName = (EditText) findViewById(R.id.signup_lastname);
        buttonSignUp.setOnClickListener(this);

        login = (TextView) findViewById(R.id.go_to_login);

        login.setOnClickListener(this);
    }

    private void userSignUp(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up...");
        progressDialog.show();

        String user_name = userName.getText().toString().trim();
        String user_password = password.getText().toString().trim();
        String email_id = email.getText().toString().trim();
        String first_name = firstName.getText().toString().trim();
        String last_name = lastName.getText().toString().trim();

        if(user_name.isEmpty()){
            progressDialog.dismiss();
            userName.setError("Username should not be empty");
            userName.requestFocus();
            return;
        }

        if(user_password.isEmpty()){
            progressDialog.dismiss();
            password.setError("Password should not be empty");
            password.requestFocus();
            return;
        }

        if(email_id.isEmpty()){
            progressDialog.dismiss();
            email.setError("Email should not be empty");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email_id).matches()) {
            progressDialog.dismiss();
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }

        if(first_name.isEmpty()){
            progressDialog.dismiss();
            firstName.setError("First Name should not be empty");
            firstName.requestFocus();
            return;
        }

        if(last_name.isEmpty()){
            progressDialog.dismiss();
            lastName.setError("Last Name should not be empty");
            lastName.requestFocus();
            return;
        }


        APIService service = HttpClientService.getClient().create(APIService.class);

        final User user = new User();

        user.setUsername(user_name);
        user.setPassword( user_password);
        user.setEmail(email_id);
        user.setFirst_name(first_name);
        user.setLast_name(last_name);

        retrofit2.Call<User> call = service.createUser(user);



        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> userResponse) {
                progressDialog.dismiss();
                if(userResponse.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_LONG).show();
                    // SharedPreference.getInstance(getApplicationContext()).userLogin(user);
                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "error1", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View v) {

        if(v == buttonSignUp){
              userSignUp();
        }
        else if (v == login) {

            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
