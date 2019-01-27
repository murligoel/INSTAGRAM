package com.example.android.instagram.controller;

import android.app.Application;

import com.example.android.instagram.model.Result;
import com.example.android.instagram.model.User;

public class Controller extends Application{

    private User user = new User();
    private Result result = new Result();

    public User getUser(){
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
