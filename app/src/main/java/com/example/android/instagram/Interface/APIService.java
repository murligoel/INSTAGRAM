package com.example.android.instagram.Interface;

import com.example.android.instagram.model.Auth;
import com.example.android.instagram.model.Profile;
import com.example.android.instagram.model.User;
import com.example.android.instagram.model.Result;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

    //sign up
    @POST("api/register/")
    Call<User> createUser(@Body User user);

    //login
    @POST("api/login/")
    Call<Result> createUser(@Body Result result);

    //token fetch
    @POST("auth-jwt-obtain/")
    Call<Auth> fetchToken(@Body Auth auth);


//    @POST("auth-jwt-verify/")
//    //Call<ResponseBody> getAuth(@Header("Authorization") String authToken);
//    Call<ResponseBody> getAuth(@Header("Authorization") String authToken);

    @PUT("profile/{id}")
    Call<Profile> putPost(@Path("id") String id, @Body Profile profile, @Header("Authorization") String authToken);

}