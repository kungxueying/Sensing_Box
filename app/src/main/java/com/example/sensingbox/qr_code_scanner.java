package com.example.sensingbox;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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

public class qr_code_scanner extends AppCompatActivity {

    SurfaceView surfaceView;
    TextView textView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    //record message
    String message;
    String now_place;

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

    public void goTo_sensor_select (View view){
        //error detect
        if(message==null){
            Toast.makeText(getApplicationContext(),"Please scan QR Code before tapping the OK button.", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, add_sensor.class);
            intent.putExtra("data", message);
            intent.putExtra("place", now_place);
            startActivity(intent);
        }
    }

}
