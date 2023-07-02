package com.example.myapplicationgame;

import static com.example.myapplicationgame.ConstantVariable.APP_NAME;
import static com.example.myapplicationgame.ConstantVariable.MY_UUID;
import static com.example.myapplicationgame.ConstantVariable.STATE_CONNECTED;
import static com.example.myapplicationgame.ConstantVariable.STATE_CONNECTING;
import static com.example.myapplicationgame.ConstantVariable.STATE_CONNECTION_FAILED;
import static com.example.myapplicationgame.ConstantVariable.STATE_LISTENING;
import static com.example.myapplicationgame.ConstantVariable.STATE_MESSAGE_RECEIVED;
import java.security.SecureRandom;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Connect_Activity extends AppCompatActivity {
    Button scan_btn;
    Button select_choice_btn;
    BluetoothAdapter myBluetoothAdapter;
    Timer time;
    ListView listview;
    // add device logical name in bluetooth device;
    BluetoothDevice[] bluetoothDeviceArray;
    String playerOne = "";
    String playerTwo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        listview = findViewById(R.id.pair_devices);
        scan_btn = findViewById(R.id.scan_devices);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // get paired devices
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPairDevices();
            }
        });
        // if click on paired device go to main game
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkBTPermission();
                playerTwo = (String) parent.getItemAtPosition(position);
                playerOne = myBluetoothAdapter.getName();
                Toast.makeText(Connect_Activity.this, "Your opponent :" + playerTwo, Toast.LENGTH_SHORT).show();
                time = new Timer();
                time.schedule(new TimerTask() {
                    @Override
                    public void run(){
                        //getApplicationContext() gives the context of the application and depends on the application's lifecycle.
                        Intent btn = new Intent(getApplicationContext(),add_players.class);
                        btn.putExtra("Playerone",playerOne);
                        btn.putExtra("Playertwo",playerTwo);
                        // Intention to start activity
                        startActivity(btn);
                        finish();
                    }
                },5000);
            }
        });
    } //oncreate end

    //check permission for bluetooth adpater
    public void checkBTPermission() {
        if (ContextCompat.checkSelfPermission(Connect_Activity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 31) {
                //Toast.makeText(getApplicationContext(), "Ask Permission", Toast.LENGTH_LONG).show();
                // ask for the permission by the user
                ActivityCompat.requestPermissions(Connect_Activity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100);
            }
            //return;
        }
    } // check permission method end

    // list down all the devices available
    private void getPairDevices() {
        checkBTPermission();
        // get list of devices to pair with
        Set<BluetoothDevice> bt = myBluetoothAdapter.getBondedDevices();
        ArrayList<String> arrayList = new ArrayList<>();
        // revise the bluetooth device
        bluetoothDeviceArray = new BluetoothDevice[bt.size()];
        int index = 0;
        // show all the devices available for paring
        if (bt.size() > 0) {
            for (BluetoothDevice device : bt) {
                bluetoothDeviceArray[index] = device;
                // add logic name in the array list
                arrayList.add(index, device.getName());
                index++;
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
            arrayAdapter.notifyDataSetChanged();
            listview.setAdapter(arrayAdapter);
        } // if end
        else {
            Toast.makeText(Connect_Activity.this, "Please Paired with another device", Toast.LENGTH_SHORT).show();
        }
    } // get pair method end

} // class end
