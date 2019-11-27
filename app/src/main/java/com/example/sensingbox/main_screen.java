package com.example.sensingbox;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class main_screen extends AppCompatActivity {

    private TextView welcome_name;
    sensor user_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        sensor_set m = (sensor_set) getApplication();
        user_info = m.getSensor(Integer.valueOf(0));//save user name in sensor[0];

        if(user_info.getSensorName().equals("Click to add sensor"))
            user_info.setSensorName("Adam");
        welcome_name = (TextView) findViewById(R.id.Welcome);
        welcome_name.setText("Welcome, "+user_info.getSensorName());

        try{
            Select_Bluetooth.mmOutStream.write("2,0\n".getBytes());
        }catch (Exception e){
            Log.d("BT", "BT error!");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void goTo_Sensor_Select (View view){
        sensor_set m = (sensor_set) getApplication();

        Intent intent = new Intent (this, select_sensor_v2.class);
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
        startActivity(intent);
    }

    public void goTo_About (View view){
        Intent intent = new Intent (this, About.class);
        startActivity(intent);
    }

    public void goTo_Upload_Data (View view){

        try{
            Select_Bluetooth.mmOutStream.write("6\n".getBytes());
        }catch (Exception e){
            Log.d("BT", "upload data");
        }
        Intent intent = new Intent (this, Uploading.class);
        startActivity(intent);
    }
}
