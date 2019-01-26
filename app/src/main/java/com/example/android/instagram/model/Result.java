package com.example.android.instagram.model;

public class Result {

    private String username;
    private String password;
    private  String  user_id;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(int userId) {
        this.user_id = user_id;
    }
}
