package com.example.sensingbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class add_sensor extends AppCompatActivity {

    String message;
    ListView listView ;
    sensor sensor1=new sensor();
    //temp variables
    int temp;
    char c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sensor);

        Intent intent=getIntent();
        message=intent.getStringExtra("data");
        //extract sensor data
        //sensor code
        sensor1.setSensorCode(message);
        //name
        temp=message.charAt(0)-'0';
        if (temp==1)sensor1.setSensorName("Camera");
        else if (temp==2) sensor1.setSensorName("CO2");
        else if (temp==3) sensor1.setSensorName("Temperature");
        //status
        c=message.charAt(2);
        if (c=='r')sensor1.setStatus("run");
        else if(c=='s')sensor1.setStatus("stop");
        //delay time = cycle
        temp=message.indexOf(',',4);//location of comma before delay time
        temp++;//location of delay time
        temp=Integer.parseInt(message.substring(temp));//extract delay time + convert string to int
        sensor1.setCycle(temp);

        // Get ListView object from xml
                listView = (android.widget.ListView) findViewById(R.id.list);

        // Defined Array values to show in ListView
        String[] values = new String[] { "Sensor Type:  "+sensor1.getSensorName(),
                "Collection Cycle:  "+sensor1.getCycle()+" seconds",
                "Default Sensor Status: "+sensor1.getStatus(),
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }

    public void edit_sensor (View view){
        Intent intent = new Intent (this, add_edit_sensor.class);
        intent.putExtra("data",sensor1);
        startActivity(intent);
    }

    public void ok (View view){
        Intent intent = new Intent (this, sensor_select.class);
        intent.putExtra("data",sensor1);
        startActivity(intent);
    }
}
