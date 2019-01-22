package com.example.android.instagram.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.android.instagram.activity.UserProfileActivity;
import com.example.android.instagram.httpservice.HttpClientService;
import com.example.android.instagram.model.Auth;
import com.example.android.instagram.model.Result;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button buttonLogIn,activityAfterLogin;
    private EditText userName,password;
    private static String token;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        buttonLogIn = (Button) v.findViewById(R.id.login_button);
//        activityAfterLogin = (Button) v.findViewById(R.id.activity_after_login);

        userName = (EditText) v.findViewById(R.id.login_username);
        password = (EditText) v.findViewById(R.id.login_password);
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

        return v;
    }

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
                    Toast.makeText(getActivity(), userResponse.body().getToken(), Toast.LENGTH_LONG).show();
                    token = userResponse.body().getToken();
                    Log.e("value",token);
                    getAuth();
                    //    SharedPreference.getInstance(getApplicationContext()).;
                    // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                else{
                    Toast.makeText(getActivity(), "login not correct", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure( Call<Auth> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
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
//                    Toast.makeText(getApplicationContext(), "authentication successfull", Toast.LENGTH_LONG).show();
//                    Toast.makeText(<>, "Please long press the key", Toast.LENGTH_LONG );
                    Toast.makeText(getActivity(),"authentication successfull",Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getActivity(), "token is not correct", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
            }
        });
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
