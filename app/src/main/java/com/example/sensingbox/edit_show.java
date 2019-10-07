package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class edit_show extends AppCompatActivity {

    String message="Click to add sensor";
    String now_place;
    sensor now_sensor;
    ListView listView ;
    TextView sensor_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_show);

        Intent intent2=getIntent();
        now_place=intent2.getStringExtra("place");

        sensor_set m = (sensor_set) getApplication();
        now_sensor = m.getSensor(Integer.valueOf(now_place));

        sensor_name = (TextView) findViewById(R.id.textView2);
        sensor_name.setText(now_sensor.getSensorName());


        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        // Defined Array values to show in ListView
        String[] values = new String[] { "Collection Cycle:    " + now_sensor.getCycle()+" seconds",
                "Sensor Status:    "+ now_sensor.getStatus(),
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }

    public void delete_sensor (View view){
        now_sensor.setSensorCode(message);
        now_sensor.setSensorName(message);
        String cmd = "4,"+ now_place +"\n";
        try{
            Select_Bluetooth.mmOutStream.write(cmd.getBytes());

        }catch (Exception e){
            Log.d("BT","set sensor failed." );
        }
        this.finish();
    }

    public void edit (View view){
        Intent intent = new Intent (this, edit_sensor.class);
        intent.putExtra("place",now_place);
        startActivity(intent);
        this.finish();
    }
}
