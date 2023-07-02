package com.example.myapplicationgame;
import static com.example.myapplicationgame.ConstantVariable.APP_NAME;
import static com.example.myapplicationgame.ConstantVariable.MY_UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;

public class ServerSide extends Thread {
    // server class
    private BluetoothServerSocket serverSocket;
    BluetoothAdapter mybluetoothAdapter;
    Connect_Activity ca = new Connect_Activity();

    // constructor
    public ServerSide() {
        try {
            ca.checkBTPermission();
            serverSocket = mybluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    } // constructor end

    public void run() {
        BluetoothSocket socket = null;
        ca.checkBTPermission();
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException ex) {
                ex.printStackTrace();
            } //try/catch end
            // If a connection was accepted
            if (socket != null) {
                break;
            }
        } // while end
    } // run method end

    //Will cancel the listening socket, and cause the thread to finish
    public void cancel() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
} // server class end

