package com.example.android.instagram.activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.android.instagram.activity.UserProfileActivity;


import okhttp3.ResponseBody;
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

        String user_name = userName.getText().toString().trim();
        String user_password = password.getText().toString().trim();


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

        APIService service = HttpClientService.getClient().create(APIService.class);

        Result result = new Result();

        result.setUsername(user_name);
        result.setPassword(user_password);



        Call<Auth> call = service.createUser(result);



        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse( Call<Auth> call, Response<Auth> userResponse) {
                progressDialog.dismiss();

                if(userResponse.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), userResponse.body().getToken(), Toast.LENGTH_LONG).show();
                    token = userResponse.body().getToken();
                    Log.e("value",token);
                    getAuth();
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

    public void getAuth(){

        APIService service = HttpClientService.getClient().create(APIService.class);

        Call<ResponseBody> call = service.getAuth("JWT " +token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "authentication successfull", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "token is not correct", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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
