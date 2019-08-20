package com.example.sensingbox;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class bluetooth {
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

    // Member fields
    private  BluetoothAdapter mAdapter;
    private  Handler mHandler;

    //private Handler mHandler;
    // Our main handler that will receive callback notifications
    private ConnectedThread mConnectedThread;
    // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null;
    // bi-directional client-to-client data path


    public bluetooth(Handler handler) {
        mHandler = handler;
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
