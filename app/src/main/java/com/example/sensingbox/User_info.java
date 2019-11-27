package com.example.sensingbox;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class User_info extends AppCompatActivity {

    ListView listView;
    //DS_user user = new DS_user();
    //DS_dataset data = new DS_dataset();
    sensor user_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        sensor_set m = (sensor_set) getApplication();
        user_info = m.getSensor(Integer.valueOf(0));

        if(user_info.getSensorName().equals("Click to add sensor"))
            user_info.setSensorName("Adam");

        listView = (ListView) findViewById(R.id.list);

        // Defined Array values to show in ListView
        String[] values = new String[]{"Username: " + user_info.getSensorName(),
                //"Box Number:    "+data.boxID,
                "Email: " + user_info.getSensorCode(),
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(User_info.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

    }
}