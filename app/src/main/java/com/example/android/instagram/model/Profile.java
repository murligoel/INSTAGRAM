package com.example.android.instagram.model;

import android.net.Uri;

public class Profile {
    private String bio;
    private String phone_no;
    private String image;
    private String username;

    public Profile(String username,String bio) {
        this.username = username;
        this.bio = bio;
    }



//    public void setBio(String bio) {
//        this.bio = bio;
//    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getBio() {
        return bio;
    }

    public String getUsername() {
        return username;
    }

//    public void setUsername(String username) {
//        this.username = username;
//    }
}
