package com.example.sensingbox;

import android.support.v7.app.AppCompatActivity;

public class fb_register extends AppCompatActivity {

    public void registerToFB(DS_user newuser) {

        firebase_upload fb = new firebase_upload();
        fb.insertuser(newuser);
    }
}
