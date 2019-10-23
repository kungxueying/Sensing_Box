package com.example.sensingbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class select_sensor_v2 extends AppCompatActivity {

    static int flag[]=new int[3];
    static int recent;
    static String model="Click to add sensor";
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Select_Bluetooth.mmOutStream.write("2,0\n".getBytes());
        }catch (Exception e){
            Log.d("WTF", "QWQQQQQ");
        }
        setContentView(R.layout.activity_select_sensor_v2);

        sensor_set m = (sensor_set) getApplication();

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        // Defined Array values to show in ListView
        String[] values = new String[] { "Sensor 01:    " + m.getSensor(1).getSensorName(),
                "Sensor 02:    "+ m.getSensor(2).getSensorName(),
                "Sensor 03:    "+ m.getSensor(3).getSensorName(),
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

        // Set an item click listener for ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(flag[position]==0){
                    flag[position]=1;
                    Intent intent = new Intent (select_sensor_v2.this, qr_code_scanner.class);
                    intent.putExtra("place",Integer.toString(position+1));
                    startActivity(intent);
                }
                else if (flag[position]==1){
                    Intent intent2 = new Intent (select_sensor_v2.this, edit_show.class);
                    intent2.putExtra("place",Integer.toString(position+1));
                    startActivity(intent2);
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_select_sensor_v2);

        sensor_set m = (sensor_set) getApplication();

        int i;
        for(i=1;i<=3;i++){
            if(model.equals(m.getSensor(i).getSensorCode())){
                flag[i-1]=0;
            }else
                flag[i-1]=1;
        }

/*
        if (model.equals(m.getSensor(1).getSensorCode())) {//delete sensor
            flag[0]=0;
        } else
            flag[0]=1;

        if (model.equals(m.getSensor(2).getSensorCode())){
            flag[1]=0;
        }else
            flag[1]=1;

        if (model.equals(m.getSensor(3).getSensorCode())){
            flag[2]=0;
        }else
            flag[2]=1;

        if (model.equals(m.getSensor(4).getSensorCode())){
            flag[3]=0;
        }else
            flag[3]=1;
*/
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        // Defined Array values to show in ListView
        String[] values = new String[] { "Sensor 01:    " + m.getSensor(1).getSensorName(),
                "Sensor 02:    "+ m.getSensor(2).getSensorName(),
                "Sensor 03:    "+ m.getSensor(3).getSensorName(),
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

        // Set an item click listener for ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(flag[position]==0){
                    flag[position]=1;
                    Intent intent = new Intent (select_sensor_v2.this, qr_code_scanner.class);
                    intent.putExtra("place",Integer.toString(position+1));
                    startActivity(intent);
                }
                else if (flag[position]==1){
                    Intent intent2 = new Intent (select_sensor_v2.this, edit_show.class);
                    intent2.putExtra("place",Integer.toString(position+1));
                    startActivity(intent2);
                }
            }
        });
    }


/*
    public void button1_onclick (View view){
        if(flag[0]==0){
            flag[0]=1;
            Intent intent = new Intent (this, qr_code_scanner.class);
            intent.putExtra("place","1");
            startActivity(intent);
        }
        else if (flag[0]==1){
            Intent intent2 = new Intent (this, edit_show.class);
            intent2.putExtra("place","1");
            startActivity(intent2);
        }
    }
    public void button2_onclick (View view){
        if(flag[1]==0){
            flag[1]=1;
            Intent intent = new Intent (this, qr_code_scanner.class);
            intent.putExtra("place","2");
            startActivity(intent);
        }
        else if (flag[1]==1){
            Intent intent2 = new Intent (this, edit_show.class);
            intent2.putExtra("place","2");
            startActivity(intent2);
        }
    }
    public void button3_onclick (View view){
        if(flag[2]==0){
            flag[2]=1;
            Intent intent = new Intent (this, qr_code_scanner.class);
            intent.putExtra("place","3");
            startActivity(intent);
        }
        else if (flag[2]==1){
            Intent intent2 = new Intent (this, edit_show.class);
            intent2.putExtra("place","3");
            startActivity(intent2);
        }
    }
    public void button4_onclick (View view){
        if(flag[3]==0){
            flag[3]=1;
            Intent intent = new Intent (this, qr_code_scanner.class);
            intent.putExtra("place","4");
            startActivity(intent);
        }
        else if (flag[3]==1){
            Intent intent2 = new Intent (this, edit_show.class);
            intent2.putExtra("place","4");
            startActivity(intent2);
        }
    }*/
}