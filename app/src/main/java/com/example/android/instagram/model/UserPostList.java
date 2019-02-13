package com.example.android.instagram.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserPostList {


    @SerializedName("detail")
    @Expose
    private ArrayList<UserPost> post = null;


    public ArrayList<UserPost> getPost() {
        return post;
    }

    public void setPost(ArrayList<UserPost> post) {
        this.post = post;
    }
}
