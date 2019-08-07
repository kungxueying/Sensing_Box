package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class edit_sensor extends AppCompatActivity {

    String message="Click to add sensor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sensor);
    }

    public void delete_sensor (View view){
        Intent intent = new Intent (this, sensor_select.class);
        intent.putExtra("data",message);
        startActivity(intent);
    }
}
