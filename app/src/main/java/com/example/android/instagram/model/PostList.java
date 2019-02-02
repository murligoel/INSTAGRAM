package com.example.android.instagram.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostList {

    @SerializedName(" ")
    @Expose
    private Post[] post;

    public Post[] getPost(){
        return post;
    }

//    public ArrayList<Post> getPost() {
//        return post;
//    }
//
//    public void setPost(ArrayList<Post> post) {
//        this.post = post;
//    }

}
