package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class edit_sensor extends AppCompatActivity {

    String message="Click to add sensor";
    sensor sensor1=new sensor();
    TextView sensor_name;
    EditText Cycle;
    EditText Status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sensor);

        Intent intent=getIntent();
        sensor1=(sensor)intent.getSerializableExtra("data");

        sensor_name = (TextView) findViewById(R.id.textView2);
        sensor_name.setText(sensor1.getSensorName());

        Cycle = (EditText) findViewById(R.id.editText);
        Status = (EditText) findViewById(R.id.editText2);
    }

    public void delete_sensor (View view){
        sensor1.setSensorCode(message);
        Intent intent = new Intent (this, sensor_select.class);
        intent.putExtra("data",sensor1);
        startActivity(intent);
    }

    public void ok (View view){
        if(!Cycle.getText().toString().isEmpty())sensor1.setCycle(Integer.parseInt(Cycle.getText().toString()));
        if(!Status.getText().toString().isEmpty())sensor1.setStatus(Status.getText().toString());

        Intent intent = new Intent (this, sensor_select.class);
        intent.putExtra("data",sensor1);
        startActivity(intent);
    }
}
