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

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    static int flag[]=new int[4];
    static int recent;
    static String model="Click to add sensor";

    static sensor sensor1=new sensor();
    static sensor sensor2=new sensor();
    static sensor sensor3=new sensor();
    static sensor sensor4=new sensor();
    sensor temp_sensor=new sensor();

/*
    static String sensor_code1="Click to add sensor";
    static String sensor_code2="Click to add sensor";
    static String sensor_code3="Click to add sensor";
    static String sensor_code4="Click to add sensor";
    static String sensor_name1="Click to add sensor";
    static String sensor_name2="Click to add sensor";
    static String sensor_name3="Click to add sensor";
    static String sensor_name4="Click to add sensor";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_select);
        button1 = (Button)findViewById(R.id.button6);
        button2 = (Button)findViewById(R.id.button7);
        button3 = (Button)findViewById(R.id.button8);
        button4 = (Button)findViewById(R.id.button9);

        Intent intent=getIntent();
        temp_sensor=(sensor)intent.getSerializableExtra("data");

        if(recent==1) {
                sensor1=temp_sensor;
                if (model.equals(sensor1.getSensorCode())){//delete sensor
                    flag[0]=0;
                    sensor1.setSensorName(model);//click to add sensor
                }
                else {//add or edit sensor
                    flag[0]=1;
                }

                button1.setText(sensor1.getSensorName());
                button2.setText(sensor2.getSensorName());
                button3.setText(sensor3.getSensorName());
                button4.setText(sensor4.getSensorName());
        }
        else if(recent==2){
            sensor2=temp_sensor;
            if (model.equals(sensor2.getSensorCode())){//delete sensor
                flag[1]=0;
                sensor2.setSensorName(model);//click to add sensor
            }
            else {//add or edit sensor
                flag[1]=1;
            }

            button1.setText(sensor1.getSensorName());
            button2.setText(sensor2.getSensorName());
            button3.setText(sensor3.getSensorName());
            button4.setText(sensor4.getSensorName());
        }
        else if(recent==3){
            sensor3=temp_sensor;
            if (model.equals(sensor3.getSensorCode())){//delete sensor
                flag[2]=0;
                sensor3.setSensorName(model);//click to add sensor
            }
            else {//add or edit sensor
                flag[2]=1;
            }

            button1.setText(sensor1.getSensorName());
            button2.setText(sensor2.getSensorName());
            button3.setText(sensor3.getSensorName());
            button4.setText(sensor4.getSensorName());
        }
        else if(recent==4){
            sensor4=temp_sensor;
            if (model.equals(sensor4.getSensorCode())){//delete sensor
                flag[3]=0;
                sensor4.setSensorName(model);//click to add sensor
            }
            else {//add or edit sensor
                flag[3]=1;
            }

            button1.setText(sensor1.getSensorName());
            button2.setText(sensor2.getSensorName());
            button3.setText(sensor3.getSensorName());
            button4.setText(sensor4.getSensorName());
        }
    }

    public void button1 (View view){
        if(flag[0]==0){
        flag[0]=1;recent=1;
        Intent intent = new Intent (this, qr_code_scanner.class);
        startActivity(intent);}
        else if (flag[0]==1){
            recent=1;
            Intent intent2 = new Intent (this, edit_sensor.class);
            intent2.putExtra("data",sensor1);
            startActivity(intent2);
        }
    }

    public void button2 (View view){
        if(flag[1]==0){
        flag[1]=1;recent=2;
        Intent intent = new Intent (this, qr_code_scanner.class);
        startActivity(intent);}
        else if (flag[1]==1){
            recent=2;
            Intent intent2 = new Intent (this, edit_sensor.class);
            intent2.putExtra("data",sensor2);
            startActivity(intent2);}
    }

    public void button3 (View view){
        if(flag[2]==0){
        flag[2]=1;recent=3;
        Intent intent = new Intent (this, qr_code_scanner.class);
        startActivity(intent);}
        else if (flag[2]==1){
            recent=3;
            Intent intent2 = new Intent (this, edit_sensor.class);
            intent2.putExtra("data",sensor3);
            startActivity(intent2);}
    }

    public void button4 (View view){
        if(flag[3]==0){
        flag[3]=1;recent=4;
        Intent intent = new Intent (this, qr_code_scanner.class);
        startActivity(intent);}
         else if (flag[3]==1){
            recent=4;
            Intent intent2 = new Intent (this, edit_sensor.class);
            intent2.putExtra("data",sensor4);
            startActivity(intent2);}
    }
}