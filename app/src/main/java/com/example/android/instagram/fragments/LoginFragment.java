package com.example.android.instagram.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.instagram.Interface.APIService;
import com.example.android.instagram.R;
import com.example.android.instagram.activity.FrontPageActivity;
import com.example.android.instagram.activity.UserProfileActivity;
import com.example.android.instagram.controller.Controller;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.Auth;
import com.example.android.instagram.model.Result;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private Controller controller;
    private Button buttonLogIn,activityAfterLogin,signupIntent;
    private EditText userName,password;
    private static String token;
    private  static String userId;

    SharedPreferences sharedPref;

    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

       sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        buttonLogIn = (Button) v.findViewById(R.id.login_button);
//        activityAfterLogin = (Button) v.findViewById(R.id.activity_after_login);

        userName = (EditText) v.findViewById(R.id.login_username);
        password = (EditText) v.findViewById(R.id.login_password);
        signupIntent = (Button) v.findViewById(R.id.signup_intent);
        buttonLogIn.setOnClickListener(this);
//        activityAfterLogin.setOnClickListener(this);

        buttonLogIn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                startActivity(intent);
                return false;
            }
        });
        signupIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FrontPageActivity.class);
                startActivity(i);
            }
        });
        controller = (Controller) getActivity().getApplicationContext();
        return v;
    }

        Result result = null;
    private void userLogIn(){

//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Signing Up...");
//        progressDialog.show();
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                "Loging In",
                "Please wait...",
                true);


        String user_name = userName.getText().toString().trim();
        String user_password = password.getText().toString().trim();

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username",user_name);
        editor.putString("password",user_password);
        editor.apply();


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

         result = new Result();

        controller.setResult(result);

        result.setUsername(user_name);
        result.setPassword(user_password);



        Call<Result> call = service.createUser(controller.getResult());
//        Call<Result> call = service.createUser(result);



        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse( Call<Result> call, Response<Result> userResponse) {
                progressDialog.dismiss();

                if(userResponse.isSuccessful()) {
                    Toast.makeText(getActivity(), "login success", Toast.LENGTH_LONG).show();
                    userId = userResponse.body().getUserId();
                    Log.e("value",userId+"");
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("userid",userId);
                    editor.apply();

                    getToken();
                    //    SharedPreference.getInstance(getApplicationContext()).;
                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                else{
                    Toast.makeText(getActivity(), "login not correct", Toast.LENGTH_LONG).show();
                }

            }



            @Override
            public void onFailure( Call<Result> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
            }
        });

    }

    public static String get_user_id() {
        return userId;
    }

    Auth auth = null;
    public void getToken(){

        String user_name = userName.getText().toString().trim();
        String user_password = password.getText().toString().trim();

        APIService service = HttpClientService.getClient().create(APIService.class);

        auth = new Auth();
        controller.setAuth(auth);

        auth.setUsername(user_name);
        auth.setPassword(user_password);

        Call<Auth> call = service.fetchToken(controller.getAuth());


        call.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if (response.isSuccessful()){
//                    Toast.makeText(getApplicationContext(), "authentication successfull", Toast.LENGTH_LONG).show();
//                    Toast.makeText(<>, "Please long press the key", Toast.LENGTH_LONG );
//                    Toast.makeText(getActivity(),response.body().getToken(),Toast.LENGTH_LONG).show();
                    token = response.body().getToken();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("usertoken",token);
                    editor.apply();

                    Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(getActivity(), "token is not correct", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {

                Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
            }
        });
    }
    public static String get_Token() {
        return token;
    }



    @Override
    public void onClick(View v) {

        if(v == buttonLogIn){
            userLogIn();
        }
//        else if(v == activityAfterLogin){
//            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
//            startActivity(intent);
//        }
    }
}