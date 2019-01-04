package com.example.android.instagram.interfaces;

import com.example.android.instagram.model.Auth;
import com.example.android.instagram.model.User;
import com.example.android.instagram.model.Result;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIService {

    @POST("auth/register/")
    Call<User> createUser(@Body User user);

    @POST("/login")
    Call<Auth> createUser(@Body Result result);

    @GET("user/classroom/")
    Call<ResponseBody> getAuth(@Header("Authorization") String authToken);
}
