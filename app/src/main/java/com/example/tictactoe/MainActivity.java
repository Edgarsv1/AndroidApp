package com.example.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.translation.ViewTranslationCallback;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText playerName;
    private Button savePlayerName;
    boolean activePlayer;
    private Button[] buttons = new Button[9];
    private int roundCount;
    private Button resetGame;
    // player 1 = 0     player 2 = 1        empty = 2
    int[] gameState = {2,2,2,2,2,2,2,2,2,2};
    int[][] winningPositions = {
            {0,1,2}, {3,4,5}, {6,7,8},  //horizontals
            {0,3,6}, {1,4,7}, {2,5,8},  // vertikals
            {0,4,8}, {2,4,6}            // sliipi
    };

    // daļa no base game tika taisīta līdzīgi kā šajā video https://www.youtube.com/watch?v=CCQTD7ptYqY
    // neatradu repositoriju vai cita veida norādi uz kodu, kas ir šajā video


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createPlayerNamePopup();




        int resetID = getResources().getIdentifier("resetGame", "id", getPackageName());
        View b = findViewById(resetID);
        b.setVisibility(View.GONE);
        resetGame = (Button) findViewById(resetID);
        resetGame.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                playAgain();
                b.setVisibility(View.GONE);
            }
        });

        for(int i =0; i< buttons.length; i++){
            String btnID = "btn_" + i;

            int resourceID = getResources().getIdentifier(btnID, "id", getPackageName());
            buttons[i] = (Button) findViewById((resourceID));
            buttons[i].setOnClickListener(this::onClick);
        }
        activePlayer = true;
        roundCount = 0;
    }


    public void onClick(View view) {

        if(!((Button)view).getText().toString().equals("")){
            return;
        }


        String btnID = view.getResources().getResourceEntryName(view.getId());
        int gameStatePointer = Integer.parseInt(btnID.substring(btnID.length()-1, btnID.length()));

        if(activePlayer){
            ((Button)view).setText("X");
            ((Button)view).setTextColor(Color.parseColor("#FFFFFF"));
            gameState[gameStatePointer] = 0;
        }
        else{
            ((Button)view).setText("O");
            ((Button)view).setTextColor(Color.parseColor("#FFFFFF"));
            gameState[gameStatePointer] = 1;
        }
        roundCount++;

        if(checkWinner()){

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int resetID = getResources().getIdentifier("resetGame", "id", getPackageName());
                    View b = findViewById(resetID);
                    b.setVisibility(View.VISIBLE);
                }
            }, 100);


            int rateID = getResources().getIdentifier("player1", "id", getPackageName());
            final TextView textViewToChange = (TextView) findViewById(rateID);



            if(activePlayer){
                Toast.makeText(this,  textViewToChange.getText() + " Won!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Player two Won!", Toast.LENGTH_SHORT).show();

            }

        }else if(roundCount==9){
            Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();


        }else{
            activePlayer = !activePlayer;
        }






    }

    public boolean checkWinner(){
        boolean res = false;
        for(int [] winningPosition : winningPositions){
            if(gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] !=2 ){
                res = true;
            }
        }
        return res;
    }

    public void playAgain(){
        activePlayer = true;
        for(int i=0; i<buttons.length; i++){
            gameState[i] = 2;
            buttons[i].setText("");
        }
    }


    public void createPlayerNamePopup(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View PlayerPopupView = getLayoutInflater().inflate(R.layout.popup,null);
        playerName = (EditText) PlayerPopupView.findViewById(R.id.playerName);
        savePlayerName = (Button) PlayerPopupView.findViewById(R.id.saveButton);
        dialogBuilder.setView(PlayerPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        savePlayerName.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                int rateID = getResources().getIdentifier("player1", "id", getPackageName());
                final TextView textViewToChange = (TextView) findViewById(rateID);
                textViewToChange.setText(playerName.getText());
                dialog.dismiss();
            }
        });

    }
}