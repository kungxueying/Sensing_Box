package com.example.sensingbox;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
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
    private static int data_count = 0;
    private static int img_count = 0;
    private static String now_img;
    private static String now_img_data;
    private static byte[] idata = new byte[0];
    private static int img_name_size = 0;
    ArrayList<String> img_name = new ArrayList<String>();
    int img_idx = 0;
    int msg_flag = 0;
    int rcv_flag = 0;
    boolean write_flag = false;
    public static int BOXID = 2;
    public static String sensorList = "";

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

    //gps
    private double locationX = 0.0;
    private double locationY = 0.0;
    boolean gpsON = false;
    LocationManager mlocationManager;
    private LocationManager lms;
    private String bestProvider = LocationManager.GPS_PROVIDER;    //最佳資訊提供者

    //處理字串
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg.what == MESSAGE_READ) { //收到MESSAGE_READ 開始接收資料
                _recieveData = null;
                _recieveData = (String) (msg.obj);
                textview.append("read: " + _recieveData + "\n"); //將收到的字串呈現在畫面上

                //字串處理
                String[] data = _recieveData.split(",");

                //收到結尾符號
                if (_recieveData.equals("upload success!")) {
                    if (msg_flag == 6) {
                        if (img_count != 0)
                            receive_img();
                        else if (rcv_flag == 0 || data_count != 1) {
                            show_upload(data_count, 1);
                            get_sensor(0);
                            msg_flag = 2;
                        }
                        rcv_flag = 1;
                    } else if (msg_flag == 2) {
                        msg_flag = 0;
                        fb.insertBox(sensorList);
                        //sensor config upload success
                    } else if (msg_flag == 7) {
                        Log.e("now_img_base64::::", now_img_data);
                        writeToSDcard(now_img, now_img_data);

                        //img upload success
                        msg_flag = 7;
                        img_idx = 0;
                        now_img_data = "";
                        img_name_size = img_name.size();
                        if (img_name_size != 0) {
                            Log.e("img_name_size:::", String.valueOf(img_name_size));
                            img_name_size--;
                            now_img = img_name.get(img_name_size);
                            get_img(now_img);
                            img_name.remove(now_img);
                        } else {
                            _recieveData = ""; //清除上次收到的資料
                            _sendCMD = "7,0,\n";
                            textview.append("upload_end\n");
                            textview.append("cmd: " + _sendCMD);
                            if (mConnectedThread != null) //First check to make sure thread created
                                mConnectedThread.write(_sendCMD);
                            if (rcv_flag == 0 || data_count != 1) {
                                show_upload(data_count, 1);
                                get_sensor(0);
                                msg_flag = 2;
                            }
                        }
                    }
                }
                if (msg_flag == 6 && data.length == 4) {
                    ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

                    //如果未連線的話，mNetworkInfo會等於null
                    if (mNetworkInfo != null) {
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
                    } else {
                        insert_SQLite(data);
                    }
                }

                if (msg_flag == 7 && !data[0].equals("upload success!")) {
                    //process img receive
                    if (write_flag) {
                        //receive all base64 string
                        byte[] imageBytes = Base64.decode(data[0], Base64.DEFAULT);
                        byte[] allByteArray = new byte[idata.length + imageBytes.length];
                        ByteBuffer buff = ByteBuffer.wrap(allByteArray);
                        buff.put(idata);
                        buff.put(imageBytes);
                        idata = buff.array();
                    } else
                        Log.e("??????", "write error!");
                }

                if (msg_flag == 2 && data.length == 5) {
                    //receive sensor config detail
                    sensor_set m = (sensor_set) getApplication();
                    sensor now_sensor = m.getSensor(Integer.valueOf(data[0]));
                    now_sensor.setSensorName(sensor_type_check(data[1]));
                    now_sensor.setStatus(data[2]);
                    now_sensor.setCycle(Integer.valueOf(data[3]));
                    String code = data[1] + data[2] + data[3];
                    now_sensor.setSensorCode(code);
                    if(!sensorList.equals(data[1])) {
                        sensorList = sensorList + sensor_type_check(data[1]) + " ";
                    }
                }
            }

            if (msg.what == MESSAGE_READ_CMD) {
                _recieveData = null;
                _recieveData = (String) (msg.obj);
                String[] data = _recieveData.split(",");
                textview.append(_recieveData + "\n"); //將收到的字串呈現在畫面上

                if (data[0].equals("6")) {
                    msg_flag = 6;
                    data_count = Integer.parseInt(data[1]);
                    if (rcv_flag == 0) {
                        show_upload(data_count, 0);
                    }
                }
                if (data[0].equals("7")) {
                    msg_flag = 7;
                    //img_size = Integer.parseInt(data[1]);
                    //String imgname = String.valueOf(data[2]);
                    //Log.e("77777772",String.valueOf(img_size));
                    //Log.e("77777772",imgname);
                }
                if (data[0].equals("2")) {
                    msg_flag = 2;
                    data_count = Integer.parseInt(data[1]);
                }
            }

            if (msg.what == CONNECTING_STATUS) {
                //收到CONNECTING_STATUS 顯示以下訊息
                if (msg.arg1 == 1) {
                    dev_name = (String) (msg.obj);
                    textview.append("Connected to Device: " + dev_name);
                    upload_data();
                } else
                    textview.append("Connection Failed");
            }

        }
    };


    public void show_upload(int count, int flag) {
        Intent intent = new Intent(this, Uploading.class);
        intent.putExtra("count", count);
        intent.putExtra("flag", flag);
        startActivity(intent);
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    //取得系統定位服務
    private void testLocationProvider() {
        mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);//設置允許產生資費
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = mlocationManager.getBestProvider(criteria, false);

        //詢問是否存取位置資訊
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1
            );
        }else{
            Location location = mlocationManager.getLastKnownLocation(provider);
            updateLocation(location);
            mlocationManager.requestLocationUpdates(provider, 3000, 0, locationListener);
        }

    }

    private void updateLocation(Location location) {
        if (location != null) {
            locationX = location.getLatitude();
            locationY  = location.getLongitude();
        } else {
            locationX = 0.0;
            locationY = 0.0;
        }
        //背景執行時關閉顯示地點
        if(gpsON == true){
            Toast.makeText(this, "" + "x:" + locationX  + " y:" + locationY , Toast.LENGTH_SHORT).show();
        }
    }
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateLocation(location);

        }
        public void onProviderDisabled(String provider){
            updateLocation(null);
        }
        public void onProviderEnabled(String provider){

        }
        public void onStatusChanged(String provider, int status,Bundle extras){

        }
    };

    public void insert_fb(String[] _data){
        DS_dataset newdata = new DS_dataset();

        newdata.id = _data[0];
        newdata.sensor = sensor_type_check(_data[1]);
        newdata.time = _data[2];
        newdata.data = _data[3];

        if(_data[1].equals("1")) {
            img_name.add(_data[3]);
            img_count++;
        }
        newdata.userID = "111";
        newdata.boxID = String.valueOf(BOXID);

        newdata.locate ="民雄";
        newdata.x =  String.valueOf(locationX) ;
        newdata.y = String.valueOf(locationY);

        fb.insertdata(newdata);
    }

    public void insert_SQLite(String[] _data){
        DS_dataset newdata = new DS_dataset();

        newdata.id = _data[0];
        newdata.sensor = sensor_type_check(_data[1]);
        newdata.time = _data[2];
        newdata.data = _data[3];
        if(_data[1].equals("1"))
            img_name.add(_data[3]);

        newdata.userID = "111";
        newdata.boxID = String.valueOf(BOXID);

        newdata.locate ="民雄";
        newdata.x =  String.valueOf(locationX) ;
        newdata.y = String.valueOf(locationY);

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


    //寫檔到sdcard
    private void writeToSDcard(String filename, String data){
        //建立自己的目錄
        String path = Environment.getExternalStorageDirectory().getPath();
        File dir = new File(path + "/sensingbox/img");
        if (!dir.exists()){
            dir.mkdirs();
        }
        try {
            InputStream is = new ByteArrayInputStream(idata);
            Bitmap image= BitmapFactory.decodeStream(is);

            File file = new File(path + "/sensingbox" + filename);
            FileOutputStream fout = new FileOutputStream(file);

            image.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            is.close();
            image.recycle();
            //fout.write(data.getBytes());
            fout.flush();
            fout.close();
            idata = null;
            idata = new byte[0];
            String jpgname = filename.substring(5,9);
            fb.uploadImg(path + "/sensingbox" + filename,jpgname,"民雄");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("file", "Write to SDCARD!");
    }

    public void receive_img(){
        msg_flag=7;
        img_idx =0;
        now_img_data = "";
        img_name_size = img_name.size();
        if(img_name_size!=0) {
            Log.e("img_name_size:::",String.valueOf(img_name.size()));
            img_name_size--;
            now_img = img_name.get(img_name_size);
            get_img(now_img);
            img_name.remove(now_img);
            Log.e("7777771",now_img);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bluetooth);

        //取得定位
        testLocationProvider();

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

        if(isExternalStorageReadable()&&isExternalStorageWritable()){
            write_flag=true;
        }
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
    public void get_img(String imgname) {
        _recieveData = ""; //清除上次收到的資料
        _sendCMD = "7,1,"+imgname+"\n";
        textview.append( "get_img"+imgname+"\n");
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
