package com.example.android.instagram.Interface;

import com.example.android.instagram.fragments.SignUpFragment;
import com.example.android.instagram.model.AddFriendModel;
import com.example.android.instagram.model.Auth;
import com.example.android.instagram.model.Comment;
import com.example.android.instagram.model.CommentList;
import com.example.android.instagram.model.Like;
import com.example.android.instagram.model.Post;
import com.example.android.instagram.model.PostList;
import com.example.android.instagram.model.Profile;
import com.example.android.instagram.model.User;
import com.example.android.instagram.model.Result;
import com.example.android.instagram.model.UserPostList;


import java.util.ArrayList;
import java.util.jar.Attributes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    //sign up
    @POST("register/")
    Call<User> createUser(@Body User user);

    //login
    @POST("login/")
    Call<Result> createUser(@Body Result result);

    //token fetch
    @POST("auth-jwt-obtain/")
    Call<Auth> fetchToken(@Body Auth auth);


    @Multipart
    @PUT("profile/{id}/")
    Call<ResponseBody> putPost(@Path("id") String id, @Part("bio") RequestBody userName, @Part("phone_no") RequestBody phoneNumber, @Header("Authorization") String authToken, @Part MultipartBody.Part image);

    @GET("profile/{id}/")
    Call<Profile> viewProfile(@Path("id") String id, @Header("Authorization") String authToken);

    @Multipart
    @POST("post/")
    Call<ResponseBody> createPost(@Header("Authorization") String authToken, @Part("caption") RequestBody captionForPost, @Part MultipartBody.Part picture);

    @Multipart
    @POST("post/")
    Call<ResponseBody> createPost1(@Header("Authorization") String authToken,  @Part MultipartBody.Part picture);

    @GET("post/")
    Call<ArrayList<Post>> viewPost(@Header("Authorization") String authToken);

    @GET("logout/")
    Call<ResponseBody> logOut(@Header("Authorzation") String authtoken);

    @GET("like/{id}/")
    Call<Like> createLike(@Path("id") String id, @Header("Authorization") String authToken);

    @Multipart
    @POST("comment/{id}/")
    Call<ResponseBody> commentPost(@Path("id") String id, @Header("Authorization") String authToken,@Part("text") RequestBody text);

    @GET("comment/{id}/")
    Call<CommentList> getComments(@Path("id") String id, @Header("Authorization") String authToken);

    @DELETE("comment_edit/{id}/")
    Call<ResponseBody> commentDelete(@Path("id") String id, @Header("Authorization") String authToken);

    @GET("post/users_Post/")
    Call<UserPostList> viewUserPost(@Header("Authorization") String authToken);

    @GET("add_friend/")
    Call<ArrayList<AddFriendModel>> addFriend(@Query("search") String search, @Header("Authorization") String authToken);

}