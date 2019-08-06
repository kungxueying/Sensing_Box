package com.example.sensingbox;

import android.os.Bundle;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
public class test_firebase extends AppCompatActivity  {
    DS_user newuser = new DS_user();
    DS_dataset newdata = new DS_dataset();
    DS_sensorall sensor = new DS_sensorall();
    firebase_upload fb = new firebase_upload();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cdb_uploadtest);
        setNewuser(newuser);
        setNewsensor(sensor);
        setNewdata(newdata);
        setFb(fb);
        System.out.println(newuser);
        System.out.println("done");
    }


    public void setNewuser(DS_user newuser) {
        this.newuser = newuser;
        newuser.id = "111";
        newuser.name = "Amy";
        newuser.email = "amy111@gmail.com";
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
