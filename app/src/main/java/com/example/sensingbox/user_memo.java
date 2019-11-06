package com.example.sensingbox;

import android.app.Application;

public class user_memo extends Application {

    String email;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        email = "null";
    }
    public String getEmail(){
        return email;
    }
}
