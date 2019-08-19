package com.example.sensingbox;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
import android.util.Log;
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

import java.io.InputStream;
import java.io.InputStreamReader;

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
    private String _sendCMD = "";

    private Handler mHandler;
    // Our main handler that will receive callback notifications
    private ConnectedThread mConnectedThread;
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

    //firebase
    DS_user newuser = new DS_user();
    DS_sensorall sensor = new DS_sensorall();
    firebase_upload fb = new firebase_upload();

    @Override
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

    public void insert_fb(String[] _data){
        DS_dataset newdata = new DS_dataset();

        //sensor type list
        if(_data[1].equals("2"))
            newdata.sensor = "co2";
        else if(_data[1].equals("3") )
            newdata.sensor = "temperature";

        newdata.time = _data[2];
        newdata.data = _data[3];

        newdata.userID = "111";
        newdata.boxID ="2";

        newdata.locate ="民雄";
        newdata.x = "20";
        newdata.y = "121";

        fb.insertdata(newdata);
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

        //處理字串
        mHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(msg.what == MESSAGE_READ){ //收到MESSAGE_READ 開始接收資料
                    _recieveData = null;
                    _recieveData = (String)(msg.obj);
                    _recieveData+="\n";
                    textview.append(_recieveData); //將收到的字串呈現在畫面上
                    String[] data = _recieveData.split(",");
                    if(data.length==4)
                        insert_fb(data);//上傳資料庫
                }

                if(msg.what == CONNECTING_STATUS){
                    //收到CONNECTING_STATUS 顯示以下訊息
                    if(msg.arg1 == 1)
                        textview.append("Connected to Device: " + (String)(msg.obj));
                    else
                        textview.append("Connection Failed");
                }
            }
        };


        if (mBTArrayAdapter == null) {
            // Device does not support Bluetooth
            textview.append("Status: Bluetooth not found");
            Toast.makeText(getApplicationContext(), "Bluetooth device not found!", Toast.LENGTH_SHORT).show();
        } else {
            send_command.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _recieveData = ""; //清除上次收到的資料
                    _sendCMD = input_command.getText()+ "\n";
                    textview.append("click send_command \n >");
                    textview.append("cmd: "+input_command.getText()+ "\n");
                    if(mConnectedThread != null) //First check to make sure thread created
                        mConnectedThread.write(_sendCMD);

                    //傳送將輸入的資料出去


                }
            });
            get_sensor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _recieveData = ""; //清除上次收到的資料
                    _sendCMD = "2,0\n";
                    textview.append( "click get_sensor(all)\n");
                    textview.append("cmd: "+_sendCMD);
                    if(mConnectedThread != null) //First check to make sure thread created
                        mConnectedThread.write(_sendCMD);
                }
            });

            set_sensor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _recieveData = ""; //清除上次收到的資料
                    _sendCMD = "3,3,3,run,5,tttt2222,/data/datalog.txt\n";
                    textview.append(  "click set_sensor\n");
                    textview.append("cmd: "+_sendCMD);
                    if(mConnectedThread != null) //First check to make sure thread created
                        mConnectedThread.write(_sendCMD);
                }
            });

            del_sensor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _recieveData = ""; //清除上次收到的資料
                    _sendCMD = "4,3\n";
                    textview.append("click del_sensor\n");
                    textview.append("cmd: "+_sendCMD);
                    if(mConnectedThread != null) //First check to make sure thread created
                        mConnectedThread.write(_sendCMD);
                }
            });

            edit_sensor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _recieveData = ""; //清除上次收到的資料
                    _sendCMD = "5,3,3,run,1,tttt2222,/data/datalog.txt\n";
                    textview.append( "click edit_sensor\n");
                    textview.append("cmd: "+_sendCMD);
                    if(mConnectedThread != null) //First check to make sure thread created
                        mConnectedThread.write(_sendCMD);
                }
            });

            upload_data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _recieveData = ""; //清除上次收到的資料
                    _sendCMD = "6\n";
                    textview.append(  "click upload_data\n");
                    textview.append("cmd: "+_sendCMD);
                    if(mConnectedThread != null) //First check to make sure thread created
                        mConnectedThread.write(_sendCMD);
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
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

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
            Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int position, long id) {
            textview.append("You have clicked : " + datas[position] +"\n");
            dialog.dismiss();

            if(!mBTAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), "Bluetooth not on",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            textview.append("Connecting...\n");
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0,info.length() - 17);

            // Spawn a new thread to avoid blocking the GUI one
            new Thread()
            {
                public void run() {
                    boolean fail = false;
                    //取得裝置MAC找到連接的藍芽裝置
                    BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                    try {
                        mBTSocket = createBluetoothSocket(device);
                        //建立藍芽socket
                    } catch (IOException e) {
                        fail = true;
                        Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }
                    // Establish the Bluetooth socket connection.
                    try {
                        mBTSocket.connect(); //建立藍芽連線
                    } catch (IOException e) {
                        try {
                            fail = true;
                            mBTSocket.close(); //關閉socket
                            //開啟執行緒 顯示訊息
                            mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                    .sendToTarget();
                        } catch (IOException e2) {
                            //insert code to deal with this
                            Toast.makeText(getBaseContext(), "Socket creation failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(fail == false) {
                        //開啟執行緒用於傳輸及接收資料
                        mConnectedThread = new ConnectedThread(mBTSocket);
                        mConnectedThread.start();
                        //開啟新執行緒顯示連接裝置名稱
                        mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                                .sendToTarget();
                    }
                }
            }.start();
        }
    };

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connection with BT device using UUID
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        BufferedReader reader;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = new DataInputStream(tmpIn);
            mmOutStream = new DataOutputStream(tmpOut);
            reader = new BufferedReader(new InputStreamReader(mmInStream));
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    String str = null;
                    while (true) {
                        str = reader.readLine();
                        if(str!=null) {
                            Log.e("RRRRRRRRRRRRRRRRRRR",str);
                            mHandler.obtainMessage(MESSAGE_READ, str.length(), -1, str)
                                    .sendToTarget(); // Send the obtained bytes to the UI activity
                        }else
                            break;
                    }
                    /*bytes = mmInStream.available();
                    if(bytes != 0) {
                        SystemClock.sleep(100);
                        //pause and wait for rest of data
                        bytes = mmInStream.available();

                        // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes);
                        // record how many bytes we actually read
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity

                        BufferedReader reader = new BufferedReader(new InputStreamReader(mmInStream));


                        mmInStream.close();
                    }*/

                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

}


