package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class add_edit_sensor extends AppCompatActivity {

    sensor sensor1=new sensor();
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_sensor);

        Intent intent=getIntent();
        sensor1=(sensor)intent.getSerializableExtra("data");

        // Get ListView object from xml
        listView = (android.widget.ListView) findViewById(R.id.list);

        // Defined Array values to show in ListView
        String[] values = new String[] { "Sensor Name:  "+sensor1.getSensorName(),
                "Sensor Code:   "+sensor1.getSensorCode(),
                "Collection Cycle:  ",
                "Data Storage Path: ",
                "Sensor Status: ",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }

    public void ok (View view){
        Intent intent = new Intent (this, sensor_select.class);
        intent.putExtra("data",sensor1);
        startActivity(intent);
    }
}
