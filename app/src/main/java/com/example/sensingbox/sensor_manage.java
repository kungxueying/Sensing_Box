package com.example.sensingbox;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class sensor_manage extends AppCompatActivity {


    private static final UUID BTMODULEUUID = UUID.fromString
            ("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1;
    // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2;
    // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3;
    // used in bluetooth handler to identify message status
    private String _recieveData = "";

    private Handler mHandler;
    // Our main handler that will receive callback notifications
    //private ConnectedThread mConnectedThread;
    // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null;
    // bi-directional client-to-client data path

    public TextView textview;
    private ScrollView scrollView;
    private Button searchBT;
    private EditText input_command;

    //Button
    private Button send_command;
    private Button get_sensor;
    private Button set_sensor;
    private Button del_sensor;
    private Button edit_sensor;
    private Button upload_data;

    //BT
    private String[] datas = {"1", "2", "3", "4", "5"};
    private BluetoothAdapter mBTAdapter;
    private ArrayAdapter<String> mBTArrayAdapter;
    private ListView mDevicesListView;
    private Dialog dialog;
    private Set<BluetoothDevice> mPairedDevices;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_manage);

        textview = (TextView) findViewById(R.id.data_log);
        textview.setText("START input your command.\n");

        scrollView = (ScrollView) findViewById(R.id.data_scrollview);

        input_command = (EditText) findViewById(R.id.input_command);
        send_command = (Button) findViewById(R.id.send_command);
        get_sensor = (Button) findViewById(R.id.get_sensor);
        set_sensor = (Button) findViewById(R.id.set_sensor);
        del_sensor = (Button) findViewById(R.id.del_sensor);
        edit_sensor = (Button) findViewById(R.id.edit_sensor);
        upload_data = (Button) findViewById(R.id.upload_data);

        //click button to list BT devices
        searchBT = (Button) findViewById(R.id.searchBT);
        searchBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchBT(sensor_manage.this);
            }
        });


    }

    public void SearchBT(Activity activity) {
        dialog = new Dialog(activity);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_listview);

        //BT 連線
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        //mBTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
        mBTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mDevicesListView = (ListView) dialog.findViewById(R.id.data_list);
        mDevicesListView.setAdapter(mBTArrayAdapter);
        mDevicesListView.setOnItemClickListener(mDeviceClickListener);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        if (mBTArrayAdapter == null) {
            // Device does not support Bluetooth
            textview.append("Status: Bluetooth not found");
            Toast.makeText(getApplicationContext(), "Bluetooth device not found!", Toast.LENGTH_SHORT).show();
        } else {
            send_command.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _recieveData = ""; //清除上次收到的資料

                    textview.append("click send_command \n >");
                    textview.append(input_command.getText()+ "\n");
                }
            });
            get_sensor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _recieveData = ""; //清除上次收到的資料
                    textview.append( "click get_sensor\n");
                }
            });

            set_sensor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _recieveData = ""; //清除上次收到的資料
                    textview.append(  "click set_sensor\n");
                }
            });

            del_sensor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _recieveData = ""; //清除上次收到的資料
                    textview.append(  "click del_sensor\n");
                }
            });

            edit_sensor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _recieveData = ""; //清除上次收到的資料
                    textview.append( "click edit_sensor\n");
                }
            });

            upload_data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _recieveData = ""; //清除上次收到的資料
                    textview.append(  "click upload_data\n");
                }
            });

        }
        if (!mBTAdapter.isEnabled()) {//如果藍芽沒開啟
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);//跳出視窗
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            //開啟設定藍芽畫面
            textview.append(  "Bluetooth enabled");
            Toast.makeText(getApplicationContext(), "Bluetooth turned on", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth is already on", Toast.LENGTH_SHORT).show();
            discoverBT();
            listPairedDevices();
        }
        dialog.show();
    }

    private void discoverBT(){
        // Check if the device is already discovering
        if(mBTAdapter.isDiscovering()){ //如果已經找到裝置
            mBTAdapter.cancelDiscovery(); //取消尋找
            Toast.makeText(getApplicationContext(),"Discovery stopped",Toast.LENGTH_SHORT).show();
        }
        else{
            if(mBTAdapter.isEnabled()) { //如果沒找到裝置且已按下尋找
                mBTArrayAdapter.clear(); // clear items
                mBTAdapter.startDiscovery(); //開始尋找
                Toast.makeText(getApplicationContext(), "Discovery started", Toast.LENGTH_SHORT).show();
                registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
            else{
                Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
            }
        }
    }
    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mBTArrayAdapter.notifyDataSetChanged();
            }
        }
    };
    private void listPairedDevices(){
        mPairedDevices = mBTAdapter.getBondedDevices();
        if(mBTAdapter.isEnabled()) {
            // put it's one to the adapter
            for (BluetoothDevice device : mPairedDevices)
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

            Toast.makeText(getApplicationContext(), "Show Paired Devices", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(), "Bluetooth not on",
                    Toast.LENGTH_SHORT).show();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int position, long id) {

            textview.append(  "You have clicked : " + datas[position] +"\n");
            dialog.dismiss();

            /*
            if(!mBTAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
                return;
            }
            */
        }
    };
}


