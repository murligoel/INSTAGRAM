package com.example.android.instagram.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.instagram.R;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.interfaces.APIService;
import com.example.android.instagram.model.Auth;
import com.example.android.instagram.model.Result;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogIn;
    private EditText userName,password;
    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogIn = (Button) findViewById(R.id.login_button);

        userName = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        buttonLogIn.setOnClickListener(this);
    }

    private void userLogIn(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up...");
        progressDialog.show();

        APIService service = HttpClientService.getClient().create(APIService.class);

        Result result = new Result();

        result.setUsername( userName.getText().toString().trim());
        result.setPassword( password.getText().toString().trim());



        Call<Auth> call = service.createUser(result);



        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse( Call<Auth> call, Response<Auth> userResponse) {
                progressDialog.dismiss();

                if(userResponse.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), userResponse.body().getToken(), Toast.LENGTH_LONG).show();
                    token = userResponse.body().getToken();
                    Log.e("value",token);
                    //    SharedPreference.getInstance(getApplicationContext()).;
                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "login not correct", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure( Call<Auth> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
            }
        });

    }


            @Override
            public void onClick(View v) {

                if(v == buttonLogIn){
                    userLogIn();
        }

    }
}
