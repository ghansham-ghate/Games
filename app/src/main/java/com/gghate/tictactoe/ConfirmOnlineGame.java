package com.gghate.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gghate.tictactoe.HTTP.FCMHTTPRequest;
import com.gghate.tictactoe.Message.Message;


import static com.gghate.tictactoe.Constant.ACCEPTOR;
import static com.gghate.tictactoe.Constant.GAME_ID;
import static com.gghate.tictactoe.Constant.GAME_REQUEST;
import static com.gghate.tictactoe.Constant.NEW_GAME_REQUEST;
import static com.gghate.tictactoe.Constant.PLAYER_1;
import static com.gghate.tictactoe.Constant.PLAYER_2;
import static com.gghate.tictactoe.Constant.PLAYER_TYPE;
import static com.gghate.tictactoe.Constant.REQUESTOR;
import static com.gghate.tictactoe.Constant.TYPE;

public class ConfirmOnlineGame extends AppCompatActivity {
    TextView statusText;
    Button confirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_online_game);
        statusText=(TextView)findViewById(R.id.confirmText);
        confirmButton=(Button)findViewById(R.id.confirm);
        String gameRequest=getIntent().getStringExtra(PLAYER_TYPE);
        if(gameRequest.equals(PLAYER_2))
            loadNewGameRequestConfigurationForAcceptor();
        if(gameRequest.equals(PLAYER_1))
            loadNewGameRequestConfigurationForRequestor();

    }
    public void loadNewGameRequestConfigurationForRequestor()
    {
        final Message gameDetials=getGameDetailsOfRequestor();
        statusText.setText("Requested "+gameDetials.getAcceptorPlayer()+" with new Game Request .Waiting for him to confirm");
        confirmButton.setAlpha(0.4F);
        confirmButton.setEnabled(false);
        final CountDownTimer countDownTimer=new CountDownTimer(120000,5000) {
            @Override
            public void onTick(long l) {
                SharedPreferences sharedPreferences=getSharedPreferences(GAME_REQUEST,MODE_PRIVATE);
                if(sharedPreferences.getString("request","None").equals("accepted"))
                {
                    confirmButton.setEnabled(true);
                    this.cancel();
                    final Message gameDetials1=getGameDetailsOfRequestor();
                    Toast.makeText(ConfirmOnlineGame.this,"Request Accepted "+gameDetials.getGameID(),Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ConfirmOnlineGame.this, OnlineTicTacToe.class);
                    intent.putExtra(PLAYER_TYPE,PLAYER_1);
                    intent.putExtra(GAME_ID,gameDetials1.getGameID());
                    intent.putExtra(ACCEPTOR, gameDetials1.getAcceptorPlayer());
                    intent.putExtra(REQUESTOR,gameDetials1.getRequestorPlayer());
                    startActivity(intent);
                }

            }

            @Override
            public void onFinish() {
                Toast.makeText(ConfirmOnlineGame.this,"Request timed out . ",Toast.LENGTH_SHORT).show();
            }
        };
        countDownTimer.start();
    }
    public Message getGameDetailsOfRequestor()
    {
        SharedPreferences sharedPreferences=getSharedPreferences(GAME_REQUEST,MODE_PRIVATE);
        Message gameDetails=new Message();
        gameDetails.setGameID(sharedPreferences.getString(GAME_ID,"NONE"));
        gameDetails.setRequestorPlayer(sharedPreferences.getString(REQUESTOR,"NONE"));
        gameDetails.setAcceptorPlayer(sharedPreferences.getString(ACCEPTOR,"NONE"));
        return gameDetails;
    }
    public void loadNewGameRequestConfigurationForAcceptor()
    {
      Message gameDetails=getGameDetailsOfAccpetor();
      statusText.setText(gameDetails.getRequestorPlayer()+" wants to play Tic Tac Toe with you ");
    }

    public Message getGameDetailsOfAccpetor()
    {
        SharedPreferences sharedPreferences=getSharedPreferences(GAME_REQUEST,MODE_PRIVATE);
        Message gameDetails=new Message();
        gameDetails.setGameID(sharedPreferences.getString(GAME_ID,"NONE"));
        gameDetails.setRequestorPlayer(sharedPreferences.getString(REQUESTOR,"NONE"));
        gameDetails.setAcceptorPlayer(sharedPreferences.getString(ACCEPTOR,"NONE"));
        return gameDetails;
    }

    public void moveToOnlineGame(View view)
    {
        FCMHTTPRequest.confirmGameRequest(ConfirmOnlineGame.this,getGameDetailsOfAccpetor().getRequestorPlayer(),getGameDetailsOfAccpetor().getAcceptorPlayer(),getGameDetailsOfAccpetor().getGameID(),confirmButton);
        confirmButton.setAlpha(0.4F);
        confirmButton.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        getSharedPreferences(GAME_REQUEST,MODE_PRIVATE).edit().clear().commit();
        finishAffinity();
    }

    @Override
    protected void onStop() {
        super.onStop();
     //   getSharedPreferences(GAME_REQUEST,MODE_PRIVATE).edit().clear().commit();
      //  getSharedPreferences(NEW_GAME_REQUEST,MODE_PRIVATE).edit().clear().commit();
    }
}
