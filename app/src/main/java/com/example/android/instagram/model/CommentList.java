package com.example.android.instagram.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CommentList {

    @SerializedName("detail")
    @Expose
    private ArrayList<Comment> comment = null;


    public ArrayList<Comment> getComment() {
        return comment;
    }

    public void setComment(ArrayList<Comment> comment) {
        this.comment = comment;
    }
}
