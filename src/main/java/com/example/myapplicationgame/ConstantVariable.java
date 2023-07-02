package com.example.myapplicationgame;

import java.util.UUID;

public class ConstantVariable {
    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;
    // to start communication and and play
    static final String APP_NAME = "TICTACTOE";
    static final UUID MY_UUID = UUID.fromString("867bb1de-01df-4456-8c2b-f74cf88986a5");

}
