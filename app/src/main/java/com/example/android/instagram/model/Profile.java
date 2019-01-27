package com.example.android.instagram.model;

import android.net.Uri;

public class Profile {
    private String username;
    private String email;
    private String first_name;
    private String last_name;
    private String bio;
    private String phone_number;
    private String image_uri;

    public Profile() {
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }


    public String getEmail() {
        return email;
    }


    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }


    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String  image_uri) {
        this.image_uri = image_uri;
    }
}
