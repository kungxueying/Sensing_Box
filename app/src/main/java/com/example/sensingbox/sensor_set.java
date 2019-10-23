package com.example.sensingbox;

import android.app.Application;

import java.lang.reflect.Array;

public class sensor_set extends Application {
    sensor[] Obj = new sensor[4];

    private sensor sensor_array;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        for(int i=0;i<4;i++)
            Obj[i] = new sensor();
    }
    public sensor getSensor(int idx){
        return Obj[idx];
    }
}
