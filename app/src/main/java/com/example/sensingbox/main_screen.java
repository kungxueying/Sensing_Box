package com.example.sensingbox;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class main_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }

    public void goTo_Sensor_Select (View view){
        Intent intent = new Intent (this, sensor_select.class);
        startActivity(intent);
    }

    public void goTo_User_Info (View view){
        Intent intent = new Intent (this, User_info.class);
        startActivity(intent);
    }

    public void goTo_Cloud_Data (View view){
        //Intent intent = new Intent (this, firebase_upload.class);
        Uri uri = Uri.parse("https://fangi7.github.io/web-cloud_function/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        //startActivity(intent);
        startActivity(intent);
    }

    public void goTo_About (View view){
        Intent intent = new Intent (this, About.class);
        startActivity(intent);
    }

    public void goTo_Upload_Data (View view){
        Intent intent = new Intent (this, Select_Bluetooth.class);
        startActivity(intent);
    }
}
