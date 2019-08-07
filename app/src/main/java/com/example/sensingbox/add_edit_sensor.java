package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class add_edit_sensor extends AppCompatActivity {

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_sensor);

        TextView sensorcode = (TextView)findViewById(R.id.textView6);
        Intent intent=getIntent();
        sensorcode.setText(intent.getStringExtra("data"));
        message=intent.getStringExtra("data");
    }

    public void ok (View view){
        Intent intent = new Intent (this, sensor_select.class);
        intent.putExtra("data",message);
        startActivity(intent);
    }
}
