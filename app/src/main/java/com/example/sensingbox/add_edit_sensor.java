package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class add_edit_sensor extends AppCompatActivity {

    sensor now_sensor;
    TextView sensor_name;
    Spinner spinner1;
    Spinner spinner2;
    String cycle_temp;
    String status_temp;
    String now_place;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_sensor);


        Intent intent=getIntent();
        now_place=intent.getStringExtra("place");

        sensor_set m = (sensor_set) getApplication();
        now_sensor = m.getSensor(Integer.valueOf(now_place));

        sensor_name = (TextView) findViewById(R.id.textView2);
        sensor_name.setText(now_sensor.getSensorName());

        //spinner
        spinner1 = (Spinner)findViewById(R.id.spinner1);
        final String[] cycle = {"5", "10", "20", "30", "60"};
        ArrayAdapter<String> cycleList = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                cycle);
        spinner1.setAdapter(cycleList);

        spinner2 = (Spinner)findViewById(R.id.spinner2);
        final String[] status = {"run", "pause"};
        ArrayAdapter<String> statusList = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                status);
        spinner2.setAdapter(statusList);

        cycle_temp=Integer.toString(now_sensor.getCycle());
        status_temp=now_sensor.getStatus();

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
        now_sensor.setCycle(Integer.parseInt(cycle_temp));
        now_sensor.setStatus(status_temp);

        String[] code= now_sensor.getSensorCode().split(",");
        String cmd = "3,"+ now_place + ","+ code[0]+","+status_temp +","+cycle_temp+ ",aded7777\n";

        try{
            Select_Bluetooth.mmOutStream.write(cmd.getBytes());

        }catch (Exception e){
            Log.d("BT","add_edit sensor failed." );
        }
        this.finish();
    }
}
