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

    sensor sensor1;
    sensor sensor2;
    sensor sensor3;
    sensor sensor4;

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

        sensor_set m = (sensor_set) getApplication();
        sensor1 = m.getSensor(1);
        sensor2 = m.getSensor(2);
        sensor3 = m.getSensor(3);
        sensor4 = m.getSensor(4);

        button1.setText(sensor1.getSensorName());
        button2.setText(sensor2.getSensorName());
        button3.setText(sensor3.getSensorName());
        button4.setText(sensor4.getSensorName());

        /*
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
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_sensor_select);
        button1 = (Button) findViewById(R.id.button6);
        button2 = (Button) findViewById(R.id.button7);
        button3 = (Button) findViewById(R.id.button8);
        button4 = (Button) findViewById(R.id.button9);


        sensor_set m = (sensor_set) getApplication();
        sensor1 = m.getSensor(1);
        sensor2 = m.getSensor(2);
        sensor3 = m.getSensor(3);
        sensor4 = m.getSensor(4);

        if (model.equals(sensor1.getSensorCode())) {//delete sensor
            flag[0] = 0;
            sensor1.setSensorName(model);//click to add sensor
        }
        if (model.equals(sensor2.getSensorCode())){
            flag[1]=0;
            sensor2.setSensorName(model);
        }
        if (model.equals(sensor3.getSensorCode())){
            flag[2]=0;
            sensor3.setSensorName(model);
        }
        if (model.equals(sensor4.getSensorCode())){
            flag[3]=0;
            sensor4.setSensorName(model);
        }

        button1.setText(sensor1.getSensorName());
        button2.setText(sensor2.getSensorName());
        button3.setText(sensor3.getSensorName());
        button4.setText(sensor4.getSensorName());
    }

    public void button1_onclick (View view){
        if(flag[0]==0){
            flag[0]=1;
            Intent intent = new Intent (this, qr_code_scanner.class);
            intent.putExtra("place","1");
            startActivity(intent);
        }
        else if (flag[0]==1){
            Intent intent2 = new Intent (this, edit_show.class);
            intent2.putExtra("place","1");
            startActivity(intent2);
        }
    }

    public void button2 (View view){
        if(flag[1]==0) {
            flag[1] = 1;
            Intent intent = new Intent(this, qr_code_scanner.class);
            intent.putExtra("place", "2");
            startActivity(intent);
        }
        else if (flag[1]==1){
            Intent intent2 = new Intent (this, edit_show.class);
            intent2.putExtra("place","2");
            startActivity(intent2);
        }
    }

    public void button3 (View view){
        if(flag[2]==0){
            flag[2]=1;
            Intent intent = new Intent (this, qr_code_scanner.class);
            intent.putExtra("place","3");
            startActivity(intent);
        }
        else if (flag[2]==1){
            Intent intent2 = new Intent (this, edit_show.class);
            intent2.putExtra("place","3");
            startActivity(intent2);
        }
    }

    public void button4 (View view){
        if(flag[3]==0){
            flag[3]=1;
            Intent intent = new Intent (this, qr_code_scanner.class);
            intent.putExtra("place","4");
            startActivity(intent);
        }
         else if (flag[3]==1){
            Intent intent2 = new Intent (this, edit_show.class);
            intent2.putExtra("place","4");
            startActivity(intent2);
        }
    }
}