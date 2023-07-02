package com.example.myapplicationgame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class add_players extends AppCompatActivity {
    String getPlayerOneName;
    String getPlayerTwoName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize the object
        setContentView(R.layout.activity_add_players);
        EditText playerOne = findViewById(R.id.playerOne);
        EditText playerTwo = findViewById(R.id.playerTwo);
        playerOne.setText(getIntent().getStringExtra("Playerone"));
        playerTwo.setText(getIntent().getStringExtra("Playertwo"));
        // get opponent device logical name
        Button startGameButton = findViewById(R.id.startGameButton);
        Button swap_players = findViewById(R.id.swap_players);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 getPlayerOneName = playerOne.getText().toString();
                 getPlayerTwoName = playerTwo.getText().toString();
                if (getPlayerOneName.isEmpty() || getPlayerTwoName.isEmpty()) {
                    Toast.makeText(add_players.this, "Please enter player name", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(add_players.this, MainGame.class);
                    intent.putExtra("playerone", getPlayerOneName);
                    intent.putExtra("playertwo", getPlayerTwoName);
                    startActivity(intent);
                    finish();
                }
            }
        });

        swap_players.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // extract data from edit text
                 String player1 = playerOne.getText().toString();
                 String player2 = playerTwo.getText().toString();
                 // set by swapping
                 playerOne.setText(player2);
                 playerTwo.setText(player1);
            }
        });
    }
}