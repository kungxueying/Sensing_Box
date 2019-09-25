package com.example.sensingbox;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Select_Bluetooth extends AppCompatActivity {

    private DB_itemDAO itemDAO;//SQLite

    private static final UUID BTMODULEUUID = UUID.fromString
            ("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1;
    // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2;
    private final static int MESSAGE_READ_CMD = 4;
    // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3;
    // used in bluetooth handler to identify message status
    private String _recieveData = "";
    private String _sendCMD = "";

    //private Handler mHandler;
    // Our main handler that will receive callback notifications
    public static ConnectedThread mConnectedThread;
    // bluetooth background worker thread to send and receive data
    public static BluetoothSocket mBTSocket = null;
    public static InputStream mmInStream = null;
    public static OutputStream mmOutStream = null;
    public BufferedReader reader;
    // bi-directional client-to-client data path

    public TextView textview;
    public Button search_btn;
    private static int data_count=0;
    int msg_flag = 0;

    //BT
    private String[] datas = {"1", "2", "3", "4", "5"};
    private BluetoothAdapter mBTAdapter;
    private ArrayAdapter<String> mBTArrayAdapter;
    private ListView mDevicesListView;
    private Dialog dialog;
    private Set<BluetoothDevice> mPairedDevices;
    public static String dev_name;

    //firebase
    DS_user newuser = new DS_user();
    DS_sensorall sensor = new DS_sensorall();
    firebase_upload fb = new firebase_upload();

    //處理字串
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){

            if(msg.what == MESSAGE_READ){ //收到MESSAGE_READ 開始接收資料
                _recieveData = null;
                _recieveData = (String)(msg.obj);
                textview.append("read: "+_recieveData+"\n"); //將收到的字串呈現在畫面上
                String[] data = _recieveData.split(",");

                //字串處理
                if(msg_flag==6 && data.length==4)
                {
                    ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

                    //如果未連線的話，mNetworkInfo會等於null
                    if(mNetworkInfo != null)
                    {
                        //網路是否已連線(true or false)
                        mNetworkInfo.isConnected();
                        //網路連線方式名稱(WIFI or mobile)
                        mNetworkInfo.getTypeName();
                        //網路連線狀態
                        mNetworkInfo.getState();
                        //網路是否可使用
                        mNetworkInfo.isAvailable();
                        //網路是否已連接or連線中
                        mNetworkInfo.isConnectedOrConnecting();
                        //網路是否故障有問題
                        mNetworkInfo.isFailover();
                        //網路是否在漫遊模式
                        mNetworkInfo.isRoaming();

                        insert_fb(data);
                    }else{
                        insert_SQLite(data);
                    }

                }else if(_recieveData.equals("upload success!")){
                    if(msg_flag==6) {
                        if(data_count!=1){
                            show_upload(data_count,1);
                            get_sensor(0);
                            msg_flag=2;
                        }

                    }
                    else if(msg_flag==2){

                    }
                }
                if(msg_flag==2 &&data.length==5){
                        sensor_set m = (sensor_set) getApplication();
                        Log.e("555555",data[0]);
                        sensor now_sensor = m.getSensor(Integer.valueOf(data[0]));
                        now_sensor.setSensorName(sensor_type_check(data[1]));
                        now_sensor.setStatus(data[2]);
                        now_sensor.setCycle(Integer.valueOf(data[3]));
                }
            }

            if(msg.what == CONNECTING_STATUS){
                //收到CONNECTING_STATUS 顯示以下訊息
                if(msg.arg1 == 1) {
                    dev_name = (String)(msg.obj);
                    textview.append("Connected to Device: " + dev_name);
                    upload_data();

                }
                else
                    textview.append("Connection Failed");

            }

            if(msg.what == MESSAGE_READ_CMD){

                _recieveData = null;
                _recieveData = (String)(msg.obj);
                String[] data = _recieveData.split(",");
                textview.append(_recieveData+"\n"); //將收到的字串呈現在畫面上

                if(data[0].equals("6"))
                {
                    msg_flag=6;
                    data_count = Integer.parseInt(data[1]);
                    Log.e("6666666",data[1]);
                    if(data_count!=1&&data_count!=0)
                    {
                        show_upload(data_count,0);
                    }

                }
                if(data[0].equals("2"))
                {
                    msg_flag=2;
                    //data_count = Integer.parseInt(data[1]);
                }
            }
        }
    };

    public void show_upload(int count ,int flag){
        Intent intent = new Intent (this, Uploading.class);
        intent.putExtra("count",count);
        intent.putExtra("flag",flag);
        startActivity(intent);

        /*
        int cnt = data;
        setContentView(R.layout.upload_data_page);
        TextView BT_dev = (TextView) findViewById(R.id.BT_device);
        TextView UP_info = (TextView) findViewById(R.id.info);
        TextView UP_status = (TextView) findViewById(R.id.status);
        ProgressBar UP_bar = (ProgressBar) findViewById(R.id.upload_bar);;
        Button UP_ok = (Button)findViewById(R.id.UP_OK);
        BT_dev.setText(dev_name);
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
            UP_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent (v.getContext(),main_screen.class);
                    startActivity(intent);
                }
            });
        }
        if(flag==2){
            UP_bar.setVisibility(View.GONE);
            UP_status.setVisibility(View.VISIBLE);
            UP_status.setTextColor(Color.parseColor("#FF0000"));
            UP_status.setText("Failed!");
            UP_info.setVisibility(View.GONE);
            UP_ok.setVisibility(View.VISIBLE);
        }
*/
    }

    public void insert_fb(String[] _data){
        DS_dataset newdata = new DS_dataset();

        newdata.id = _data[0];
        newdata.sensor = sensor_type_check(_data[1]);
        newdata.time = _data[2];
        newdata.data = _data[3];

        newdata.userID = "111";
        newdata.boxID ="2";

        newdata.locate ="民雄";
        newdata.x = "20";
        newdata.y = "121";

        fb.insertdata(newdata);
    }

    public void insert_SQLite(String[] _data){
        DS_dataset newdata = new DS_dataset();

        newdata.id = _data[0];
        newdata.sensor = sensor_type_check(_data[1]);
        newdata.time = _data[2];
        newdata.data = _data[3];

        newdata.userID = "111";
        newdata.boxID ="2";

        newdata.locate ="民雄";
        newdata.x = "20";
        newdata.y = "121";

        itemDAO.insert(newdata);
    }

    private String sensor_type_check(String type){
        String sensor_type = "";

        if(type.equals("1"))
            sensor_type = "camera";
        else if(type.equals("2"))
            sensor_type = "co2";
        else if(type.equals("3") )
            sensor_type = "temperature";
        else if(type.equals("4") )
            sensor_type = "light";

        return sensor_type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bluetooth);

        // 建立資料庫物件
        itemDAO = new DB_itemDAO(getApplicationContext());

        System.out.println("新增資料成功");
        List<DS_dataset> items = new ArrayList<>();
        // 取得所有記事資料
        items = itemDAO.getAll();

        //System.out.println(items);
        System.out.println("讀取資料成功");



        textview = (TextView) findViewById(R.id.data_log);
        textview.setText("START input your command.\n");

        search_btn = (Button)findViewById(R.id.search);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchBT();
            }
        });

    }

    public void upload_data(){
        _recieveData = ""; //清除上次收到的資料
        _sendCMD = "6\n";
        textview.append(  "click upload_data\n");
        textview.append("cmd: "+_sendCMD);
        if(mConnectedThread != null) //First check to make sure thread created
            mConnectedThread.write(_sendCMD);

    }
    public void get_sensor(int num) {
        _recieveData = ""; //清除上次收到的資料
        _sendCMD = "2,"+num+"\n";
        textview.append( "click get_sensor"+num+"\n");
        textview.append("cmd: "+_sendCMD);
        if(mConnectedThread != null) //First check to make sure thread created
            mConnectedThread.write(_sendCMD);
    }

    public void SearchBT() {
        dialog = new Dialog(this);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_listview);

        //BT 連線
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        //mBTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
        mBTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mDevicesListView = (ListView) dialog.findViewById(R.id.data_list);
        mDevicesListView.setAdapter(mBTArrayAdapter);
        mDevicesListView.setOnItemClickListener(mDeviceClickListener);


        if (mBTArrayAdapter == null) {
            // Device does not support Bluetooth
            textview.append("Status: Bluetooth not found");
            Toast.makeText(getApplicationContext(), "Bluetooth device not found!", Toast.LENGTH_SHORT).show();
        } else {
            /*
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
            */

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
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        public void run() {

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    String str = null;

                    //process data
                    str = reader.readLine();
                    Log.e("11111111111",str);

                    mHandler.obtainMessage(MESSAGE_READ_CMD, str.length(), -1, str)
                            .sendToTarget(); // Send the obtained bytes to the UI activity

                    while (true) {
                        try{
                            str = reader.readLine();
                            Log.e("datalog", str);
                        }catch (IOException e){}

                        if(str==null) {
                            break;
                        }
                        else {
                            mHandler.obtainMessage(MESSAGE_READ, str.length(), -1, str)
                                    .sendToTarget(); // Send the obtained bytes to the UI activity
                            if(str.equals("upload success!"))
                            {
                                break;
                            }
                        }

                    }


                } catch (IOException e) {
                    e.printStackTrace();

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
