package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Uploading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_uploading);
    }

    public void goTo_upload_success (View view){
        Intent intent = new Intent (this, upload_success.class);
        startActivity(intent);
    }
}
