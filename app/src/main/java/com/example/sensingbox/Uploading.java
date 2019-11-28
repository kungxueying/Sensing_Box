package com.example.sensingbox;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Uploading extends AppCompatActivity {

    int cnt =0;
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_data_page);
        Intent intent=getIntent();

        cnt = intent.getIntExtra("count",cnt);
        flag = intent.getIntExtra("flag",flag);


        TextView BT_dev = (TextView) findViewById(R.id.BT_device);
        TextView UP_info = (TextView) findViewById(R.id.info);
        TextView UP_status = (TextView) findViewById(R.id.status);
        ProgressBar UP_bar = (ProgressBar) findViewById(R.id.upload_bar);
        Button UP_ok = (Button)findViewById(R.id.UP_OK);
        BT_dev.setText(Select_Bluetooth.dev_name);
        if(flag==0)
        {
            UP_info.setText("Uploading data...");
            UP_bar.setVisibility(View.VISIBLE);
            UP_status.setVisibility(View.GONE);
        }
        if(flag ==1)
        {
            UP_bar.setVisibility(View.GONE);
            UP_status.setVisibility(View.VISIBLE);
            UP_status.setTextColor(Color.parseColor("#00FF00"));
            UP_status.setText("SUCCESS!");
            UP_info.setText("You upload "+cnt+" data.");

            UP_ok.setVisibility(View.VISIBLE);
            UP_bar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            flag=0;
        }
        if(flag==2){
            UP_bar.setVisibility(View.GONE);
            UP_status.setVisibility(View.VISIBLE);
            UP_status.setTextColor(Color.parseColor("#FF0000"));
            UP_status.setText("Failed!");
            UP_info.setVisibility(View.GONE);
            UP_ok.setVisibility(View.VISIBLE);
        }
    }

    public void UP_success(View v){
        Intent intent = new Intent(this,main_screen.class);
        startActivity(intent);
        this.finish();
    }


}
