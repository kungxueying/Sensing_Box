package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class add_sensor extends AppCompatActivity {

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);

        TextView sensorcode = (TextView)findViewById(R.id.textView4);
        Intent intent=getIntent();
        sensorcode.setText(intent.getStringExtra("data"));
        message=intent.getStringExtra("data");
    }

    public void edit_sensor (View view){
        Intent intent = new Intent (this, add_edit_sensor.class);
        intent.putExtra("data",message);
        startActivity(intent);
    }

    public void ok (View view){
        Intent intent = new Intent (this, sensor_select.class);
        intent.putExtra("data",message);
        startActivity(intent);
    }
}
