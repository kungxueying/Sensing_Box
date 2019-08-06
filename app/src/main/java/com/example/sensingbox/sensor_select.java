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
<<<<<<< Updated upstream
=======
    static int flag[]=new int[4];
    static int recent;
>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_select);
        button1 = (Button)findViewById(R.id.button6);
        button2 = (Button)findViewById(R.id.button7);
        button3 = (Button)findViewById(R.id.button8);
        button4 = (Button)findViewById(R.id.button9);
<<<<<<< Updated upstream

        Intent intent=getIntent();
        button1.setText(intent.getStringExtra("data"));
    }

    public void goTo_qrcode_scanner1 (View view){
        Intent intent = new Intent (this, qr_code_scanner.class);
        startActivity(intent);
=======

        Intent intent=getIntent();
        if(recent==1)button1.setText(intent.getStringExtra("data"));
        else if(recent==2)button2.setText(intent.getStringExtra("data"));
        else if(recent==3)button3.setText(intent.getStringExtra("data"));
        else if(recent==4)button4.setText(intent.getStringExtra("data"));
>>>>>>> Stashed changes
    }

    public void button1 (View view){
        if(flag[0]==0){
        flag[0]=1;recent=1;
        Intent intent = new Intent (this, qr_code_scanner.class);
<<<<<<< Updated upstream
        startActivity(intent);
=======
        startActivity(intent);}
        else if (flag[0]==1){
            flag[0]=0;recent=1;
            Intent intent2 = new Intent (this, delete_sensor.class);
            startActivity(intent2);}
>>>>>>> Stashed changes
    }

    public void button2 (View view){
        if(flag[1]==0){
        flag[1]=1;recent=2;
        Intent intent = new Intent (this, qr_code_scanner.class);
<<<<<<< Updated upstream
        startActivity(intent);
=======
        startActivity(intent);}
        else if (flag[1]==1){
            flag[1]=0;recent=2;
            Intent intent2 = new Intent (this, delete_sensor.class);
            startActivity(intent2);}
>>>>>>> Stashed changes
    }

    public void button3 (View view){
        if(flag[2]==0){
        flag[2]=1;recent=3;
        Intent intent = new Intent (this, qr_code_scanner.class);
<<<<<<< Updated upstream
        startActivity(intent);
=======
        startActivity(intent);}
        else if (flag[2]==1){
            flag[2]=0;recent=3;
            Intent intent2 = new Intent (this, delete_sensor.class);
            startActivity(intent2);}
    }

    public void button4 (View view){
        if(flag[3]==0){
        flag[3]=1;recent=4;
        Intent intent = new Intent (this, qr_code_scanner.class);
        startActivity(intent);}
         else if (flag[3]==1){
            flag[3]=0;recent=4;
            Intent intent2 = new Intent (this, delete_sensor.class);
            startActivity(intent2);}
>>>>>>> Stashed changes
    }
}
