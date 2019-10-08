package com.example.sensingbox;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class qr_code_scanner extends AppCompatActivity {

    SurfaceView surfaceView;
    TextView textView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    sensor now_sensor;
    //record message
    String message;
    String now_place;
    int temp=0;
    //button
    int count,i,test;
    String[] data;
    int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scanner);
        Intent intent=getIntent();
        now_place=intent.getStringExtra("place");

        surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
        textView=(TextView)findViewById(R.id.textView);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource=new CameraSource.Builder(this,barcodeDetector)
                .setRequestedPreviewSize(300,300).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)
                    return;
                try{
                    cameraSource.start(holder);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>(){

            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes=detections.getDetectedItems();
                if(qrCodes.size()!=0){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(qrCodes.valueAt(0).displayValue);
                            //record message
                            message=qrCodes.valueAt(0).displayValue;
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onPause() {
        this.finish();
        super.onPause();
    }
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public void goTo_sensor_select (View view){
        //error detect
        if(message==null){
            Toast.makeText(getApplicationContext(),"Please scan QR Code before tapping the OK button.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            //extract sensor data + error detect
            count=0;
            length=message.length();
            for(i=0;i<length;i++)if(message.charAt(i)==',')count++;
            if(count==2)
                data = message.split(",");
            else {
                Toast.makeText(getApplicationContext(),"Please scan the correct QR Code.", Toast.LENGTH_SHORT).show();
                return;
            }

            sensor_set m = (sensor_set)getApplication();
            now_sensor = m.getSensor(Integer.valueOf(now_place));

            //sensor type
            Log.e("111",data[0]);
            Log.e("111",data[2]);

            if(isNumeric(data[0])){
                System.out.println(Integer.valueOf(data[0]));
                temp=Integer.valueOf(data[0]);
            }else {
                Toast.makeText(getApplicationContext(),"Please scan the correct QR Code.(Error 1-1)", Toast.LENGTH_SHORT).show();
                return;
            }

            if(temp>=1&&temp<=3) {
                if (temp == 1) now_sensor.setSensorName("Camera");
                else if (temp == 2) now_sensor.setSensorName("CO2");
                else if (temp == 3) now_sensor.setSensorName("Temperature");
                else if (temp == 4) now_sensor.setSensorName("Light");
            }
            else {
                Toast.makeText(getApplicationContext(),"Please scan the correct QR Code.(Error 1-2)", Toast.LENGTH_SHORT).show();
                return;
            }

            //sensor status
            if(data[1].equals("Run")||data[1].equals("run"))now_sensor.setStatus("Run");
            else if(data[1].equals("Pause")||data[1].equals("pause"))now_sensor.setStatus("Pause");
            else {
                Toast.makeText(getApplicationContext(),"Please scan the correct QR Code. (Error 2)", Toast.LENGTH_SHORT).show();
                return;
            }

            //sensor cycle
            if(isNumeric(data[2])){
                System.out.println(Integer.valueOf(data[2]));
                temp=Integer.valueOf(data[2]);
            }else {
                Toast.makeText(getApplicationContext(),"Please scan the correct QR Code.(Error 3-1)", Toast.LENGTH_SHORT).show();
                return;
            }
            /*
            if(Character.isDigit(Integer.valueOf(data[2])))
                temp=Integer.valueOf(data[2]);
            else {
                Toast.makeText(getApplicationContext(),"Please scan the correct QR Code.(Error 3-1)", Toast.LENGTH_SHORT).show();
                return;
            }*/
            if(temp>0) now_sensor.setCycle(temp);
            else {
                Toast.makeText(getApplicationContext(),"Please scan the correct QR Code. (Error 3-2)", Toast.LENGTH_SHORT).show();
                return;
            }

            //sensor code
            now_sensor.setSensorCode(message);

            //correct QR Code
            Intent intent = new Intent(this, add_sensor.class);
            //intent.putExtra("data", message);
            intent.putExtra("place", now_place);
            startActivity(intent);
        }
    }

}
