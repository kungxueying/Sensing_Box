package com.example.sensingbox;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;



public class main_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }

    public void goTo_Sensor_Select (View view){
        try{
            //OutputStream mmOutStream = new DataOutputStream(Select_Bluetooth.mBTSocket.getOutputStream());
            //Select_Bluetooth.mmOutStream.write("2,0\n".getBytes());

        }catch (Exception e){
            Log.d("WTF", "QWQQQQQ");
        }

        sensor_set m = (sensor_set) getApplication();
        sensor p = m.getSensor(2);
        Log.e("3333333333",p.getSensorName());

        Intent intent = new Intent (this, sensor_select.class);
        startActivity(intent);
    }

    public void goTo_User_Info (View view){
        Intent intent = new Intent (this, User_info.class);
        startActivity(intent);
    }

    public void goTo_Cloud_Data (View view){
        Intent intent = new Intent (this,  firebase_upload.class);
        startActivity(intent);
    }

    public void goTo_About (View view){
        Intent intent = new Intent (this, About.class);
        startActivity(intent);
    }

    public void goTo_Upload_Data (View view){

        try{
            //OutputStream mmOutStream = new DataOutputStream(Select_Bluetooth.mBTSocket.getOutputStream());
            Select_Bluetooth.mmOutStream.write("6\n".getBytes());
        }catch (Exception e){
            Log.d("WTF", "QWQQQQQ");
        }
        Intent intent = new Intent (this, Uploading.class);
        startActivity(intent);
    }
}
