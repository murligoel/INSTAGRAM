package com.example.android.instagram.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.Result;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String name = sharedPref.getString("username","");
        String pw = sharedPref.getString("password","");

        if(name.length() > 0 && pw.length() > 0){

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            APIService service = HttpClientService.getClient().create(APIService.class);

            Result result = new Result();

//        controller.setResult(result);

            result.setUsername(name);
            result.setPassword(pw);



//        Call<Result> call = service.createUser(controller.getResult());
            Call<Result> call = service.createUser(result);



            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse( Call<Result> call, Response<Result> userResponse) {
                    progressDialog.dismiss();

                    if(userResponse.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "login success", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SplashScreenActivity.this, UserProfileActivity.class);
                        startActivity(intent);
//                        userId = userResponse.body().getUserId();
//                        Log.e("value",userId+"");
//                        getToken();
                        //    SharedPreference.getInstance(getApplicationContext()).;
                        // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "login not correct", Toast.LENGTH_LONG).show();
                    }

                }



                @Override
                public void onFailure( Call<Result> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            new Timer().schedule(new TimerTask() {
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, FrontPageActivity.class));
                }
            }, 3000);
        }

    }
}
