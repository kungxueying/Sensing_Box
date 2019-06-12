package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class upload_failed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_failed);
    }

    public void goTo_Upload_Data (View view){
        Intent intent = new Intent (this, sensor_select.class);
        startActivity(intent);
    }

    public void goTo_Main_Screen (View view){
        Intent intent = new Intent (this, main_screen.class);
        startActivity(intent);
    }
}
