package com.example.sensingbox;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.app.Activity;

import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    DS_user newuser = new DS_user();
    DS_dataset newdata = new DS_dataset();
    DS_sensorall sensor = new DS_sensorall();
    firebase_upload fb = new firebase_upload();

    private DB_itemDAO itemDAO;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean getService = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("抓GPS");
        //Toast.makeText(this, "定位服務", Toast.LENGTH_LONG).show();
        //System.out.println("X=" + longitude.intValue() + ", Y=" + latitude.intValue());
        Intent i = new Intent();
        i.setClass(this, gps.class);
        startActivity(i);
        System.out.println("抓GPS done");

        

    }

    public void goTo_Login (View view){
        Intent intent = new Intent (this, main_screen.class);
        startActivity(intent);
    }

    public void goTo_Register (View view){
        Intent intent = new Intent (this, main_screen.class);
        startActivity(intent);
    }

    public void setNewuser(DS_user newuser) {
        this.newuser = newuser;
        newuser.id = "111";
        newuser.name = "Amy";
        newuser.email = "amy123@gmail.com";
        newuser.pwd = "11122";
    }

    public void setNewdata(DS_dataset newdata) {
        this.newdata = newdata;
        newdata.time ="2017070711";
        newdata.boxID ="2";
        newdata.data ="33:";
        newdata.locate ="民雄";
        newdata.sensor ="temperature";
        newdata.userID = "111";
        newdata.x = "20";
        newdata.y = "121";

    }

    public void setNewsensor(DS_sensorall sensor) {
        this.sensor = sensor;
        sensor.sensorName ="temperature";
    }
    public void setFb(firebase_upload fb) {
        this.fb = fb;
        //fb.insertuser(newuser);
        //fb.insertsensorall(sensor);
        fb.insertdata(newdata);
    }

}
