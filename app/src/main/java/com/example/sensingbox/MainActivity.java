package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    DS_user newuser = new DS_user();
    DS_dataset newdata = new DS_dataset();
    DS_sensorall sensor = new DS_sensorall();
    firebase_upload fb = new firebase_upload();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNewuser(newuser);
        //setNewsensor(sensor);
        //setNewdata(newdata);
        //setFb(fb);
        //fb_register register = new fb_register();
        //register.registerToFB(newuser);
        fb_login login = new fb_login();
        login.logincheck("icedrip7@gmail.com","11122");
        //need goto fb_login to judge correct or not

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
        newdata.time ="201707071150";
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
        fb.insertuser(newuser);
        fb.insertsensorall(sensor);
        fb.insertdata(newdata);
    }

}
