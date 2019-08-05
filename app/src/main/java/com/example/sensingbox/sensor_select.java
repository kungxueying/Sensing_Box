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

<<<<<<< Updated upstream
=======
    Button button1;
    Button button2;
    Button button3;
    Button button4;

>>>>>>> Stashed changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_select);
<<<<<<< Updated upstream

=======
        button1 = (Button)findViewById(R.id.button6);
        button2 = (Button)findViewById(R.id.button7);
        button3 = (Button)findViewById(R.id.button8);
        button4 = (Button)findViewById(R.id.button9);

        Intent intent=getIntent();
        button1.setText(intent.getStringExtra("data"));
    }

    public void goTo_qrcode_scanner1 (View view){
        Intent intent = new Intent (this, qr_code_scanner.class);
        startActivity(intent);
    }

    public void goTo_qrcode_scanner2 (View view){
        Intent intent = new Intent (this, qr_code_scanner.class);
        startActivity(intent);
    }

    public void goTo_qrcode_scanner3 (View view){
        Intent intent = new Intent (this, qr_code_scanner.class);
        startActivity(intent);
>>>>>>> Stashed changes
    }

    public void goTo_qrcode_scanner (View view){
        Intent intent = new Intent (this, qr_code_scanner.class);
        startActivity(intent);
    }
}
