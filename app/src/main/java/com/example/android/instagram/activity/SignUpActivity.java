package com.example.android.instagram.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
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
