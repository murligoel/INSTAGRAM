package com.example.android.instagram.Interface;

import com.example.android.instagram.model.Auth;
import com.example.android.instagram.model.User;
import com.example.android.instagram.model.Result;


import java.util.jar.Attributes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIService {

    //sign up
    @POST("api/register/")
    Call<User> createUser(@Body User user);

    //login
//    @POST("api/login/")
    @POST("login/")
    Call<Result> createUser(@Body Result result);

    //token fetch
//    @POST("auth-jwt-obtain/")
    @POST("obtain/")
    Call<Auth> fetchToken(@Body Auth auth);


//    @POST("auth-jwt-verify/")
//    //Call<ResponseBody> getAuth(@Header("Authorization") String authToken);
//    Call<ResponseBody> getAuth(@Header("Authorization") String authToken);


//    @PUT("api/profile/{id}/")
    @Multipart
    @PUT("profile/{id}")
    Call<ResponseBody> putPost(@Path("id") String id, @Part("name") String userName, @Part("phone_number") String phoneNumber, @Header("Authorization") String authToken, @Part MultipartBody.Part image);
//    Call<ResponseBody> putPost(@Path("id") String id, @Part("name") String userName, @Part("phone_number") String phoneNumber, @Header("Authorization") String authToken);
}