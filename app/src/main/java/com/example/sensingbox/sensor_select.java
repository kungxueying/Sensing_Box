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
    static String sensor_code1="Click to add sensor";
    static String sensor_code2="Click to add sensor";
    static String sensor_code3="Click to add sensor";
    static String sensor_code4="Click to add sensor";
    static String model="Click to add sensor";
    static String sensor_name1="Click to add sensor";
    static String sensor_name2="Click to add sensor";
    static String sensor_name3="Click to add sensor";
    static String sensor_name4="Click to add sensor";
    static int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_select);
        button1 = (Button)findViewById(R.id.button6);
        button2 = (Button)findViewById(R.id.button7);
        button3 = (Button)findViewById(R.id.button8);
        button4 = (Button)findViewById(R.id.button9);

        Intent intent=getIntent();
        if(recent==1) {
                sensor_code1=intent.getStringExtra("data");
                if (model.equals(sensor_code1)){//delete sensor
                    flag[0]=0;
                    sensor_name1=sensor_code1;//click to add sensor
                }
                else {//add or edit sensor
                    flag[0]=1;
                    temp=sensor_code1.charAt(6)-'0';
                    if (temp==1)sensor_name1="camera";
                    else if (temp==2) sensor_name1="CO2";
                    else if (temp==3) sensor_name1="temperature";
                }

                button1.setText(sensor_name1);
                button2.setText(sensor_name2);
                button3.setText(sensor_name3);
                button4.setText(sensor_name4);
        }
        else if(recent==2){
            sensor_code2=intent.getStringExtra("data");
            if (model.equals(sensor_code2)){//delete sensor
                flag[1]=0;
                sensor_name2=sensor_code2;//click to add sensor
            }
            else {//add or edit sensor
                flag[1]=1;
                temp=sensor_code2.charAt(6)-'0';
                if (temp==1)sensor_name2="camera";
                else if (temp==2) sensor_name2="CO2";
                else if (temp==3) sensor_name2="temperature";
            }

            button1.setText(sensor_name1);
            button2.setText(sensor_name2);
            button3.setText(sensor_name3);
            button4.setText(sensor_name4);
        }
        else if(recent==3){
            sensor_code3=intent.getStringExtra("data");
            if (model.equals(sensor_code3)){//delete sensor
                flag[2]=0;
                sensor_name3=sensor_code3;//click to add sensor
            }
            else {//add or edit sensor
                flag[2]=1;
                temp=sensor_code3.charAt(6)-'0';
                if (temp==1)sensor_name3="camera";
                else if (temp==2) sensor_name3="CO2";
                else if (temp==3) sensor_name3="temperature";
            }

            button1.setText(sensor_name1);
            button2.setText(sensor_name2);
            button3.setText(sensor_name3);
            button4.setText(sensor_name4);
        }
        else if(recent==4){
            sensor_code4=intent.getStringExtra("data");
            if (model.equals(sensor_code4)){//delete sensor
                flag[3]=0;
                sensor_name4=sensor_code4;//click to add sensor
            }
            else {//add or edit sensor
                flag[3]=1;
                temp=sensor_code4.charAt(6)-'0';
                if (temp==1)sensor_name4="camera";
                else if (temp==2) sensor_name4="CO2";
                else if (temp==3) sensor_name4="temperature";
            }

            button1.setText(sensor_name1);
            button2.setText(sensor_name2);
            button3.setText(sensor_name3);
            button4.setText(sensor_name4);
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
            intent2.putExtra("data",sensor_code1);
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
            intent2.putExtra("data",sensor_code2);
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
            intent2.putExtra("data",sensor_code3);
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
            intent2.putExtra("data",sensor_code4);
            startActivity(intent2);}
    }
}