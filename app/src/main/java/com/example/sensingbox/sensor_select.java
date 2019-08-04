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

    Button button1 = (Button)findViewById(R.id.button6);
    Button button2 = (Button)findViewById(R.id.button7);
    Button button3 = (Button)findViewById(R.id.button8);
    Button button4 = (Button)findViewById(R.id.button9);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_select);
    }

    public void goTo_qrcode_scanner1 (View view){
        Intent intent = new Intent (this, qr_code_scanner.class);
        startActivityForResult(intent,1);
    }

    public void goTo_qrcode_scanner2 (View view){
        Intent intent = new Intent (this, qr_code_scanner.class);
        startActivityForResult(intent,2);
    }

    public void goTo_qrcode_scanner3 (View view){
        Intent intent = new Intent (this, qr_code_scanner.class);
        startActivityForResult(intent,3);
    }

    public void goTo_qrcode_scanner4 (View view){
        Intent intent = new Intent (this, qr_code_scanner.class);
        startActivityForResult(intent,4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1)
        {
            // fetch the message String
            String message1=data.getStringExtra("MESSAGE");
            // Set the message string in textView
            button1.setText(message1);
        }
        else if(requestCode==2)
        {
            // fetch the message String
            String message2=data.getStringExtra("MESSAGE");
            // Set the message string in textView
            button2.setText(message2);
        }
        else if(requestCode==3)
        {
            // fetch the message String
            String message3=data.getStringExtra("MESSAGE");
            // Set the message string in textView
            button3.setText(message3);
        }
        else if(requestCode==4)
        {
            // fetch the message String
            String message4=data.getStringExtra("MESSAGE");
            // Set the message string in textView
            button4.setText(message4);
        }

    }
}
