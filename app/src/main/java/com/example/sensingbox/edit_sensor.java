package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class edit_sensor extends AppCompatActivity {

    sensor sensor1=new sensor();
    TextView sensor_name;
    Spinner spinner1;
    Spinner spinner2;
    String cycle_temp;
    String status_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sensor);

        Intent intent=getIntent();
        sensor1=(sensor)intent.getSerializableExtra("data");

        sensor_name = (TextView) findViewById(R.id.textView2);
        sensor_name.setText(sensor1.getSensorName());

        //spinner
        spinner1 = (Spinner)findViewById(R.id.spinner1);
        final String[] cycle = {"5", "10", "20", "30", "60"};
        ArrayAdapter<String> cycleList = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                cycle);spinner1.setAdapter(cycleList);

        spinner2 = (Spinner)findViewById(R.id.spinner2);
        final String[] status = {"Run", "Pause"};
        ArrayAdapter<String> statusList = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                status);spinner2.setAdapter(statusList);

        cycle_temp=Integer.toString(sensor1.getCycle());
        status_temp=sensor1.getStatus();

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cycle_temp = cycle[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status_temp = status[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void ok (View view){
        sensor1.setCycle(Integer.parseInt(cycle_temp));
        sensor1.setStatus(status_temp);

        Intent intent = new Intent (this, sensor_select.class);
        intent.putExtra("data",sensor1);
        startActivity(intent);
    }
}
