package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class edit_sensor extends AppCompatActivity {

    String message="Click to add sensor";
    String sensor_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sensor);

        TextView sensorcode = (TextView)findViewById(R.id.textView4);
        Intent intent=getIntent();
        sensorcode.setText(intent.getStringExtra("data"));
        sensor_code=intent.getStringExtra("data");
    }

    public void delete_sensor (View view){
        Intent intent = new Intent (this, sensor_select.class);
        intent.putExtra("data",message);
        startActivity(intent);
    }

    public void ok (View view){
        Intent intent = new Intent (this, sensor_select.class);
        intent.putExtra("data",sensor_code);
        startActivity(intent);
    }
}
