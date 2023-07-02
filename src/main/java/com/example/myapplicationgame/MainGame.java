package com.example.myapplicationgame;
import static com.example.myapplicationgame.ConstantVariable.APP_NAME;
import static com.example.myapplicationgame.ConstantVariable.MY_UUID;
import static com.example.myapplicationgame.ConstantVariable.STATE_CONNECTED;
import static com.example.myapplicationgame.ConstantVariable.STATE_CONNECTING;
import static com.example.myapplicationgame.ConstantVariable.STATE_CONNECTION_FAILED;
import static com.example.myapplicationgame.ConstantVariable.STATE_LISTENING;
import static com.example.myapplicationgame.ConstantVariable.STATE_MESSAGE_RECEIVED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.myapplicationgame.databinding.ActivityMainGameBinding;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MainGame extends AppCompatActivity {
    ActivityMainGameBinding binding;
    private final List<int[]> combinationList = new ArrayList<>();
    // which player has played how many boxes
    private int[] boxPositions = {0,0,0,0,0,0,0,0,0}; //9 zero
    private int playerTurn = 1;
    private int totalSelectedBoxes = 1;
    BluetoothAdapter mybluetoothAdapter;
    BluetoothDevice [] bluetoothDeviceArray;
    // to get the box pos to be sent to remote device
    String boxid = "";
    ClientClass clientClass;
    ServerSide serverSide;
    SendReceive sendReceive;
    Timer time;
    String getPlayerOneName;
    String getPlayerTwoName;
    // get respective name of mobile
    String MobileName;
    // get message from remote device
    String [] tempMsg;
    protected void onCreate( Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        binding = ActivityMainGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mybluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        MobileName = mybluetoothAdapter.getName();

        getRemoteDevices();
        serverSide = new ServerSide();
        serverSide.start();
        combinationList.add(new int[] {0,1,2});
        combinationList.add(new int[] {3,4,5});
        combinationList.add(new int[] {6,7,8});
        combinationList.add(new int[] {0,3,6});
        combinationList.add(new int[] {1,4,7});
        combinationList.add(new int[] {2,5,8});
        combinationList.add(new int[] {2,4,6});
        combinationList.add(new int[] {0,4,8});

        // playerone = 1 and playertwo = 2
        getPlayerOneName = getIntent().getStringExtra("playerone");
        getPlayerTwoName = getIntent().getStringExtra("playertwo");

        binding.playerOneName.setText(getPlayerOneName);
        binding.playerTwoName.setText(getPlayerTwoName);

        checkBTPermission();
        for (int i = 0; i < bluetoothDeviceArray.length;i++) {
            // get lists of paired and connected devices
            if(bluetoothDeviceArray[i].getName().equals(getPlayerTwoName) && (getPlayerTwoName != MobileName)){
                clientClass = new ClientClass(bluetoothDeviceArray[i]);
                break;
            }
            else if(bluetoothDeviceArray[i].getName().equals(getPlayerOneName) && (getPlayerOneName != MobileName)){
                clientClass = new ClientClass(bluetoothDeviceArray[i]);
                break;
            }
        } // for loop end
        clientClass.start();

        binding.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(0)){
                    boxid = String.valueOf(0);
                    //Toast.makeText(MainGame.this,boxid,Toast.LENGTH_SHORT).show();
                    performAction((ImageView) view, 0);
                }
            }
        });
        binding.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(1)){
                    boxid = String.valueOf(1);
                    //Toast.makeText(MainGame.this,boxid,Toast.LENGTH_SHORT).show();
                    performAction((ImageView) view, 1);
                }
            }
        });
        binding.image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(2)){
                    boxid = String.valueOf(2);
                    //Toast.makeText(MainGame.this,boxid,Toast.LENGTH_SHORT).show();
                    performAction((ImageView) view, 2);
                }
            }
        });
        binding.image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(3)){
                    boxid = String.valueOf(3);
                    //Toast.makeText(MainGame.this,boxid,Toast.LENGTH_SHORT).show();
                    performAction((ImageView) view, 3);
                }
            }
        });
        binding.image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(4)){
                    boxid = String.valueOf(4);
                    //Toast.makeText(MainGame.this,boxid,Toast.LENGTH_SHORT).show();
                    performAction((ImageView) view, 4);
                }
            }
        });
        binding.image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(5)){
                    boxid = String.valueOf(5);
                    //Toast.makeText(MainGame.this,boxid,Toast.LENGTH_SHORT).show();
                    performAction((ImageView) view, 5);
                }
            }
        });
        binding.image7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boxid = String.valueOf(6);
                //Toast.makeText(MainGame.this,boxid,Toast.LENGTH_SHORT).show();
                if (isBoxSelectable(6)){
                    performAction((ImageView) view, 6);
                }
            }
        });
        binding.image8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boxid = String.valueOf(7);
                //Toast.makeText(MainGame.this,boxid,Toast.LENGTH_SHORT).show();
                if (isBoxSelectable(7)){
                    performAction((ImageView) view, 7);
                }
            }
        });
        binding.image9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boxid = String.valueOf(8);
                //Toast.makeText(MainGame.this,boxid,Toast.LENGTH_SHORT).show();
                if (isBoxSelectable(8)){
                    performAction((ImageView) view, 8);
                }
            }
        });
        // By Default player x will play first
        binding.playerOneLayout.setBackgroundResource(R.drawable.black_border);
        // Start running the BLE services
        Message message = Message.obtain();
        if(message.what == STATE_CONNECTING){
            serverSide.run();
            handler.sendMessage(message);
            clientClass.run();
        }
        else{
            handler.sendMessage(message);
        }
    } // on create method end

    private void getRemoteDevices() {
        checkBTPermission();
        // get list of devices to pair with
        Set<BluetoothDevice> bt = mybluetoothAdapter.getBondedDevices();
        // revise the bluetooth device
        bluetoothDeviceArray = new BluetoothDevice[bt.size()];
        int index = 0;
        // show all the devices available for paring
        if (bt.size() > 0) {
            for (BluetoothDevice device : bt) {
                bluetoothDeviceArray[index] = device;
                index++;
            }
        } // if end
    } // get pair method end

    private void performAction(ImageView  imageView, int selectedBoxPosition){
        // sets which player has played which box
        boxPositions[selectedBoxPosition] = playerTurn;
        // get the position of the box
        boxid = String.valueOf(selectedBoxPosition);
        if (playerTurn == 1 && MobileName.equals(getPlayerOneName)){
            imageView.setImageResource(R.drawable.ximage);
            // send message to remote device as the player X has selected box ...
            time = new Timer();
            time.schedule(new TimerTask() {
                @Override
                public void run(){
                    checkBTPermission();
                    sendReceive.write((boxid+" "+getPlayerOneName+" X").getBytes(StandardCharsets.UTF_8));
                    sendReceive.run();
                }
            },4000);

            if (checkResults()) {
                ResultDialog resultDialog = new ResultDialog(MainGame.this, binding.playerOneName.getText().toString()
                        + " is a Winner!", MainGame.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else if(totalSelectedBoxes == 9) {
                ResultDialog resultDialog = new ResultDialog(MainGame.this, "Match Draw", MainGame.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else {
                changePlayerTurn(2);
                totalSelectedBoxes++;
            }
        }
        else{
            Toast.makeText(MainGame.this,"Wait for other player move",Toast.LENGTH_SHORT).show();
        } // else end for player one

        if (playerTurn == 2 && MobileName.equals(getPlayerTwoName)) {
            imageView.setImageResource(R.drawable.oimage);
            time = new Timer();
            time.schedule(new TimerTask() {
                @Override
                public void run(){
                    sendReceive.write((boxid+" "+getPlayerTwoName+" "+"O").getBytes(StandardCharsets.UTF_8));
                    sendReceive.run();
                }
            },4000);
            if (checkResults()) {
                ResultDialog resultDialog = new ResultDialog(MainGame.this, binding.playerTwoName.getText().toString()
                        + " is a Winner!", MainGame.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else if(totalSelectedBoxes == 9) {
                ResultDialog resultDialog = new ResultDialog(MainGame.this, "Match Draw", MainGame.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else {
                changePlayerTurn(1);
                totalSelectedBoxes++;
            }
        } // if end
        else{
            Toast.makeText(MainGame.this,"Wait for other player move",Toast.LENGTH_SHORT).show();
        } // else end  for player two
    } // perform action end

    private void receivePlayerMove(String pos, String player, String Value){
        checkBTPermission();
        int position = Integer.parseInt(pos);
        // whether the box is empty or not and message is from remote player only then
        if(isBoxSelectable(position))
        {
            if(Value.equals("X") && MobileName!= player)
            {
                playerTurn = 1;
                // find the position and player should be remote one
                switch (position){
                    case 0:
                        binding.image1.setImageResource(R.drawable.ximage);
                        break;
                    case 1:
                        binding.image2.setImageResource(R.drawable.ximage);
                        break;
                    case 2:
                        binding.image3.setImageResource(R.drawable.ximage);
                        break;
                    case 3:
                        binding.image4.setImageResource(R.drawable.ximage);
                        break;
                    case 4:
                        binding.image5.setImageResource(R.drawable.ximage);
                        break;
                    case 5:
                        binding.image6.setImageResource(R.drawable.ximage);
                        break;
                    case 6:
                        binding.image7.setImageResource(R.drawable.ximage);
                        break;
                    case 7:
                        binding.image8.setImageResource(R.drawable.ximage);
                        break;
                    case 8:
                        binding.image9.setImageResource(R.drawable.ximage);
                        break;
                } // switch case end
                // sets which player has played which box
                boxPositions[position] = playerTurn;
                //Toast.makeText(getApplicationContext(),Arrays.toString(boxPositions),Toast.LENGTH_SHORT).show();
                // after set the remote device value in the box check win or lose case
                if (checkResults()) {
                    ResultDialog resultDialog = new ResultDialog(MainGame.this, player +" is a Winner!", MainGame.this);
                    resultDialog.setCancelable(false);
                    resultDialog.show();
                } else if(totalSelectedBoxes == 9) {
                    ResultDialog resultDialog = new ResultDialog(MainGame.this, "Match Draw", MainGame.this);
                    resultDialog.setCancelable(false);
                    resultDialog.show();
                }
                else{
                    totalSelectedBoxes++;
                }
                changePlayerTurn(2);
            } // inner if end

            // if box is empty and choose O from remote player then
            else if(Value.equals("O") && MobileName != player){
                playerTurn = 2;
                // find the position and player should be remote one
                switch (position){
                    case 0:
                        //binding.image1.setEnabled(true);
                        binding.image1.setImageResource(R.drawable.oimage);
                        break;
                    case 1:
                        //binding.image2.setEnabled(true);
                        binding.image2.setImageResource(R.drawable.oimage);
                        break;
                    case 2:
                        //binding.image3.setEnabled(true);
                        binding.image3.setImageResource(R.drawable.oimage);
                        break;
                    case 3:
                        //binding.image4.setEnabled(true);
                        binding.image4.setImageResource(R.drawable.oimage);
                        break;
                    case 4:
                        binding.image5.setImageResource(R.drawable.oimage);
                        break;
                    case 5:
                        binding.image6.setImageResource(R.drawable.oimage);
                        break;
                    case 6:
                        binding.image7.setImageResource(R.drawable.oimage);
                        break;
                    case 7:
                        binding.image8.setImageResource(R.drawable.oimage);
                        break;
                    case 8:
                        binding.image9.setImageResource(R.drawable.oimage);
                        break;
                } // switch case end
                // sets which player has played which box
                boxPositions[position] = playerTurn;
                //Toast.makeText(getApplicationContext(),Arrays.toString(boxPositions),Toast.LENGTH_SHORT).show();
                // after set the remote device value in the box check win or lose case
                if (checkResults()) {
                    ResultDialog resultDialog = new ResultDialog(MainGame.this, player +" is a Winner!", MainGame.this);
                    resultDialog.setCancelable(false);
                    resultDialog.show();
                } else if(totalSelectedBoxes == 9) {
                    ResultDialog resultDialog = new ResultDialog(MainGame.this, "Match Draw", MainGame.this);
                    resultDialog.setCancelable(false);
                    resultDialog.show();
                }
                else{
                    totalSelectedBoxes++;
                }
                changePlayerTurn(1);
            } // inner else if end
        } // outer if end
        else{
            Toast.makeText(getApplicationContext(),"Box is not empty"+Arrays.toString(boxPositions),Toast.LENGTH_SHORT).show();
        } // outer else end

} // receive msg end

    // player turn border to be seen who has to play currently
    private void changePlayerTurn(int currentPlayerTurn) {
        playerTurn = currentPlayerTurn;
        if (playerTurn == 1) {
            binding.playerOneLayout.setBackgroundResource(R.drawable.black_border);
            binding.playerTwoLayout.setBackgroundResource(R.drawable.white_box);
        } else {
            binding.playerTwoLayout.setBackgroundResource(R.drawable.black_border);
            binding.playerOneLayout.setBackgroundResource(R.drawable.white_box);
        }
    }
    private boolean checkResults(){
        boolean response = false;
        for (int i = 0; i < combinationList.size(); i++){
            final int[] combination = combinationList.get(i);
            if (boxPositions[combination[0]] == playerTurn && boxPositions[combination[1]] == playerTurn &&
                    boxPositions[combination[2]] == playerTurn) {
                //Toast.makeText(getApplicationContext(),"result case",Toast.LENGTH_SHORT).show();
                response = true;
            }
        }
        return response;
    }
    // To see if the boc is empty to play
    private boolean isBoxSelectable(int boxPosition) {
        boolean response = false;
        if (boxPositions[boxPosition] == 0) {
            response = true;
        }
        return response;
    }
    // reset the game for new play
    public void restartMatch(){
        boxPositions = new int[] {0,0,0,0,0,0,0,0,0}; //9 zero
        playerTurn = 1;
        totalSelectedBoxes = 1;
        binding.image1.setImageResource(R.drawable.white_box);
        binding.image2.setImageResource(R.drawable.white_box);
        binding.image3.setImageResource(R.drawable.white_box);
        binding.image4.setImageResource(R.drawable.white_box);
        binding.image5.setImageResource(R.drawable.white_box);
        binding.image6.setImageResource(R.drawable.white_box);
        binding.image7.setImageResource(R.drawable.white_box);
        binding.image8.setImageResource(R.drawable.white_box);
        binding.image9.setImageResource(R.drawable.white_box);
        // By Default player x will play first
        binding.playerOneLayout.setBackgroundResource(R.drawable.black_border);
    }

    // HANDLER
    // START MESSAGING HANDLER
    Handler handler = new Handler((new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            checkBTPermission();
            switch (message.what) {
                case STATE_LISTENING:
                    Toast.makeText(MainGame.this, "Listening", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_CONNECTING:
                    Toast.makeText(MainGame.this, "Connecting", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_CONNECTED:
                    Toast.makeText(MainGame.this, "Connected", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_CONNECTION_FAILED:
                    Toast.makeText(MainGame.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuffer = (byte[]) message.obj;
                    tempMsg = new String(readBuffer, 0, message.arg1).split(" ");
                    // message received from remote device
                    //Toast.makeText(MainGame.this,"pos: "+tempMsg[0]+" player: "+tempMsg[1]+" value: "+tempMsg[tempMsg.length -1], Toast.LENGTH_SHORT).show();
                    receivePlayerMove(tempMsg[0],tempMsg[1],tempMsg[tempMsg.length -1]);
                    //Toast.makeText(MainGame.this,"Working on it.....",Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    }));

    //check permission for bluetooth adpater
    public void checkBTPermission() {
        if (ContextCompat.checkSelfPermission(MainGame.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 31) {
                //Toast.makeText(getApplicationContext(), "Ask Permission", Toast.LENGTH_LONG).show();
                // ask for the permission by the user
                ActivityCompat.requestPermissions(MainGame.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100); // the request code was 100 it was working and in 1 also it still works ?
            }
            //return;
        }
    } // check permission method end

    // client class
    public class ClientClass extends Thread {
        private final BluetoothDevice device;
        private BluetoothSocket socket;

        // constructor
        private ClientClass(BluetoothDevice device1) {
            device = device1;
            try {
                checkBTPermission();
                //Toast.makeText(Connect_Activity.this, "Message: Client start" , Toast.LENGTH_SHORT).show();
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void run() {
            // if you are trying to discovering the new devices to connect with then
            // since only bonded devices are used not closing is required
            // STOP DISCOVERY HERE WRITE CODE FOR IT
            try {
                checkBTPermission();
                socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Will cancel an in-progress connection, and close the socket
        public void cancel() {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    } // client class end

    // Server And Client Architecture
    public class ServerSide extends Thread {
        // server class
        private BluetoothServerSocket serverSocket;

        // constructor
        public ServerSide() {
            try {
                checkBTPermission();
                serverSocket = mybluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } // constructor end

        public void run() {
            BluetoothSocket socket = null;
            checkBTPermission();
            while (true) {
                try {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTING;
                    handler.sendMessage(message);
                    socket = serverSocket.accept();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                } //try/catch end
                // If a connection was accepted
                if (socket != null) {
                    // start communication
                    sendReceive= new SendReceive(socket);
                    sendReceive.start();
                    // send/receive code
                    break;
                }
            } // while end
            sendReceive.run();
        } // run method end

        //Will cancel the listening socket, and cause the thread to finish
        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }// server class end


    public class SendReceive extends Thread{
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive(BluetoothSocket socket){
            bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;
            try {
                tempIn = bluetoothSocket.getInputStream();
                tempOut = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream = tempIn;
            outputStream = tempOut;
        }

        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;
            while(true){
                try {
                    bytes = inputStream.read(buffer);
                    // send message to handler send bytes the size of msg to target
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte [] bytes){
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } // send receiver class end

} // MainGame class end