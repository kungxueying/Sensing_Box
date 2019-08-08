package com.example.sensingbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

public class sensor_select extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_select);

    }

    public void goTo_Sensor_Manage (View view){
        Intent intent = new Intent (this, sensor_manage.class);
        startActivity(intent);
    }

}
