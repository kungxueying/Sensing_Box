package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goTo_Main_Screen (View view){
        Intent intent = new Intent (this, main_screen.class);
        startActivity(intent);
    }
}
