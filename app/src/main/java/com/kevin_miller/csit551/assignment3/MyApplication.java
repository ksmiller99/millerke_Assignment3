package com.kevin_miller.csit551.assignment3;

import android.app.Application;

/**
 * Created by Kevin on 4/1/2016.
 */
public class MyApplication extends Application {
    private String username = "";
    private String fullname = "";

    private static MyApplication singleton;

    public MyApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }
}
