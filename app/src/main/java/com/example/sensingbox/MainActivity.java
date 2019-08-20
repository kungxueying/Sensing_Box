package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setNewuser(newuser);
        //setNewsensor(sensor);
        //setNewdata(newdata);
        //setFb(fb);
    }

    public void goTo_Login (View view){
        Intent intent = new Intent (this, main_screen.class);
        startActivity(intent);
    }

    public void goTo_Register (View view){
        Intent intent = new Intent (this, Select_Bluetooth.class);
        startActivity(intent);
    }
}
