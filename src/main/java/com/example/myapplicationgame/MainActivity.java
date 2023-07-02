package com.example.myapplicationgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    Timer time;
    BluetoothAdapter myBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // off the button activity

        // if bluetooth adapter is not in device
        if (myBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Does not support on this device", Toast.LENGTH_LONG).show();

        } // if end
        // if bluetooth is already enable in mobile
        else {
            if (myBluetoothAdapter.isEnabled()) {
                // activate the scan button activity
                Toast.makeText(getApplicationContext(), "Bluetooth is Enabled", Toast.LENGTH_LONG).show();
                //initBluetooth();
                time = new Timer();
                time.schedule(new TimerTask() {
                    @Override
                    public void run(){
                        //getApplicationContext() gives the context of the application and depends on the application's lifecycle.
                        Intent btn = new Intent(getApplicationContext(),Connect_Activity.class);
                        // Intention to start activity
                        startActivity(btn);
                        finish();
                    }
                },8000);
            } // onclick else-if end
            // if bluetooth is not enabled then ask user permission
            else {
                Toast.makeText(getApplicationContext(), "Please Enable Bluetooth", Toast.LENGTH_LONG).show();
                bluetoothONMethod();
            } // inner else end

        }// outer else end
    } // on create end

    //check permission for bluetooth adpater
    private void bluetoothONMethod() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 31) {
                //Toast.makeText(getApplicationContext(), "Ask Permission", Toast.LENGTH_LONG).show();
                // ask for the permission by the user
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100); // the request code was 100 it was working and in 1 also it still works ?
            }
            //return;
        }
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, 1);
        //initBluetooth();
        time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run(){
                //getApplicationContext() gives the context of the application and depends on the application's lifecycle.
                Intent btn = new Intent(getApplicationContext(),Connect_Activity.class);
                // Intention to start activity
                startActivity(btn);
                finish();
            }
        },8000);
    }// bluetooth on method end
} // class end

