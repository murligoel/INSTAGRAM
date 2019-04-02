package com.example.android.instagram.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Post {

    private String id;
    private String title;
    private String caption;
    private String picture;
    private String files;
    private String date_created;
    private String user;
    private String name;
    private String like;
    private JSONObject p;
    private String image;

    public Post(String caption, String picture, String date_created, String like, JSONObject p,String image){
//        this.id = id;
//        this.title = title;
        this.caption = caption;
        this.picture = picture;
//        this.files = files;
        this.date_created = date_created;
        this.like = like;
        this.p = p;
        this.image = image;
//        this.user = user;
    }

    public String getId() {
        return id;
    }

//    public void setId(String id) {
//        this.id = id;
//    }

    public String getTitle() {
        return title;
    }

//    public void setTitle(String title) {
//        this.title = title;
//    }

    public String getCaption() {
        return caption;
    }

//    public void setCaption(String caption) {
//        this.caption = caption;
//    }

    public String getPicture() {
        return picture;
    }

//    public void setPicture(String picture) {
//        this.picture = picture;
//    }

    public String getFiles() {
        return files;
    }

//    public void setFiles(String files) {
//        this.files = files;
//    }

    public String getDate_created() {
        return date_created;
    }

//    public void setDate_created(String date_created) {
//        this.date_created = date_created;
//    }

    public String getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }




    public String getImage() {
        return image;
    }

    public JSONObject getP() {
        return p;
    }

//    public void setUser(String user) {
//        this.user = user;
//    }
}
