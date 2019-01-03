package com.example.android.instagram.interfaces;

import com.example.android.instagram.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIService {

    @POST("auth/register/")
    Call<User> createUser(@Body User user);

}
