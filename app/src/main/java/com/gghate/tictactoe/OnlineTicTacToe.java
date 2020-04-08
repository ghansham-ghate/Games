package com.gghate.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gghate.tictactoe.Configurations.ConfigureOX;
import com.gghate.tictactoe.HTTP.FCMHTTPRequest;
import com.gghate.tictactoe.Message.Message;
import com.gghate.tictactoe.Players.Player;
import com.gghate.tictactoe.Rules.Rule;

import java.util.Stack;

import static com.gghate.tictactoe.Constant.ACCEPTOR;
import static com.gghate.tictactoe.Constant.GAME_ID;
import static com.gghate.tictactoe.Constant.GAME_MOVE_REQUEST;
import static com.gghate.tictactoe.Constant.GAME_REQUEST;
import static com.gghate.tictactoe.Constant.MOVE;
import static com.gghate.tictactoe.Constant.PLAYER_1;
import static com.gghate.tictactoe.Constant.PLAYER_2;
import static com.gghate.tictactoe.Constant.PLAYER_TYPE;
import static com.gghate.tictactoe.Constant.REQUESTOR;
import static com.gghate.tictactoe.Constant.TYPE;

public class OnlineTicTacToe extends AppCompatActivity implements View.OnClickListener {
    Player player1,player2;
    Button o1,o2,o3,o4,o5,o6,o7,o8,o9,undo1,undo2;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Stack<Integer> moveStack=new Stack<>();
    RelativeLayout gameArea;
    BroadcastReceiver receiver;
    int currentTurn=1;
    String gameID,acceptor,requestor,player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_tic_tac_toe);
        gameID=getIntent().getStringExtra(GAME_ID);
        player=getIntent().getStringExtra(PLAYER_TYPE);
        acceptor=getIntent().getStringExtra(ACCEPTOR);
        requestor=getIntent().getStringExtra(REQUESTOR);
        builder=new AlertDialog.Builder(OnlineTicTacToe.this);
        builder.setTitle("Wait for your turn");
        builder.setCancelable(false);
        dialog=builder.create();
        Toast.makeText(OnlineTicTacToe.this,player+requestor+acceptor,Toast.LENGTH_SHORT).show();
        intialize();

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.my.app.onMoveMessageReceived");
        receiver=new PlayerMoveReceiver();
        registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    public void intialize()
    {
        player1=new Player();
        player2=new Player();
        player1.setName("player1");
        player2.setName("player2");
        player1.setType(1);
        player2.setType(2);
        player1.setStatus(ConfigureOX.PLAYING);
        player2.setStatus(ConfigureOX.PLAYING);
        player1.setPath("");
        player2.setPath("");
        gameArea=(RelativeLayout)findViewById(R.id.gameArea);

        o1=(Button)findViewById(R.id.o1);
        o1.setOnClickListener(this);
        o2=(Button)findViewById(R.id.o2);
        o2.setOnClickListener(this);
        o3=(Button)findViewById(R.id.o3);
        o3.setOnClickListener(this);
        o4=(Button)findViewById(R.id.o4);
        o4.setOnClickListener(this);
        o5=(Button)findViewById(R.id.o5);
        o5.setOnClickListener(this);
        o6=(Button)findViewById(R.id.o6);
        o6.setOnClickListener(this);
        o7=(Button)findViewById(R.id.o7);
        o7.setOnClickListener(this);
        o8=(Button)findViewById(R.id.o8);
        o8.setOnClickListener(this);
        o9=(Button)findViewById(R.id.o9);
        o9.setOnClickListener(this);
        undo2=(Button)findViewById(R.id.undoPlayer2);
        undo2.setOnClickListener(this);
        if(player.equals(PLAYER_2))
            disableScreen();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.o1)
        {
            makeMove(o1,1,currentTurn);
        }
        if(view.getId()==R.id.o2)
        {
            makeMove(o2,2,currentTurn);
        }
        if(view.getId()==R.id.o3)
        {
            makeMove(o3,3,currentTurn);
        }
        if(view.getId()==R.id.o4)
        {
            makeMove(o4,4,currentTurn);
        }
        if(view.getId()==R.id.o5)
        {
            makeMove(o5,5,currentTurn);
        }
        if(view.getId()==R.id.o6)
        {
            makeMove(o6,6,currentTurn);
        }
        if(view.getId()==R.id.o7)
        {
            makeMove(o7,7,currentTurn);
        }
        if(view.getId()==R.id.o8)
        {
            makeMove(o8,8,currentTurn);
        }
        if(view.getId()==R.id.o9)
        {
            makeMove(o9,9,currentTurn);
        }
    }

    public void makeMove(Button b,int currentPosition,int turn)
    {
        if(turn==1)
            moveOnlineX(b,currentPosition);
        else
            moveOnlineO(b,currentPosition);
    }
    public void moveOnlineO(Button b,int currentPosition)
    {
        b.setBackground(getDrawable(R.drawable.o));
        moveStack.push(currentPosition);
        player2.setPath(player2.getPath()+""+currentPosition);
        b.setEnabled(false);
        b.setAlpha(0.3F);
        enableDisableGameLayout(false);
        disableScreen();
        toggleCurrentTurn();
        FCMHTTPRequest.sendMessage(OnlineTicTacToe.this,requestor,gameID,PLAYER_2,currentPosition);
        if(Rule.bruteForceCheck(player2.getPath()))
            gameOverScreen(getDrawable(R.drawable.o_won));
        else if(moveStack.size()>=9)
        {
            Toast.makeText(OnlineTicTacToe.this,"Game Tied",Toast.LENGTH_SHORT).show();
            gameReset();
        }
    }
    public void receiveOnlineO(int currentPosition)
    {
        switch (currentPosition)
        {
            case 1:receiveOnlineOCheckWin(o1,1);break;
            case 2:receiveOnlineOCheckWin(o2,2);break;
            case 3:receiveOnlineOCheckWin(o3,3);break;
            case 4:receiveOnlineOCheckWin(o4,4);break;
            case 5:receiveOnlineOCheckWin(o5,5);break;
            case 6:receiveOnlineOCheckWin(o6,6);break;
            case 7:receiveOnlineOCheckWin(o7,7);break;
            case 8:receiveOnlineOCheckWin(o8,8);break;
            case 9:receiveOnlineOCheckWin(o9,9);break;
        }
    }
    public void receiveOnlineOCheckWin(Button b,int currentPosition)
    {   enableScreen();
        b.setBackground(getDrawable(R.drawable.o));
        moveStack.push(currentPosition);
        player2.setPath(player2.getPath()+""+currentPosition);
        b.setEnabled(false);
        b.setAlpha(0.3F);
        enableDisableGameLayout(true);
        toggleCurrentTurn();
        if(Rule.bruteForceCheck(player2.getPath()))
            gameOverScreen(getDrawable(R.drawable.o_won));
        else if(moveStack.size()>=9)
        {
            Toast.makeText(OnlineTicTacToe.this,"Game Tied",Toast.LENGTH_SHORT).show();
            gameReset();
        }
    }
    public void moveOnlineX(Button b,int currentPosition)
    {
        b.setBackground(getDrawable(R.drawable.x));
        moveStack.push(currentPosition);
        player1.setPath(player1.getPath()+""+currentPosition);
        b.setEnabled(false);
        b.setAlpha(0.3F);
        enableDisableGameLayout(false);
        disableScreen();
        toggleCurrentTurn();
        FCMHTTPRequest.sendMessage(OnlineTicTacToe.this,acceptor,gameID,PLAYER_1,currentPosition);
        if(Rule.bruteForceCheck(player1.getPath()))
            gameOverScreen(getDrawable(R.drawable.x_won));
        else if(moveStack.size()>=9)
        {
            Toast.makeText(OnlineTicTacToe.this,"Game Tied",Toast.LENGTH_SHORT).show();
            gameReset();
        }
    }

    public void receiveOnlineX(int currentPosition)
    {
        switch (currentPosition)
        {
            case 1:receiveOnlineXCheckWin(o1,1);break;
            case 2:receiveOnlineXCheckWin(o2,2);break;
            case 3:receiveOnlineXCheckWin(o3,3);break;
            case 4:receiveOnlineXCheckWin(o4,4);break;
            case 5:receiveOnlineXCheckWin(o5,5);break;
            case 6:receiveOnlineXCheckWin(o6,6);break;
            case 7:receiveOnlineXCheckWin(o7,7);break;
            case 8:receiveOnlineXCheckWin(o8,8);break;
            case 9:receiveOnlineXCheckWin(o9,9);break;
        }
    }
    public void receiveOnlineXCheckWin(Button b,int currentPosition)
    {
        enableScreen();
        b.setBackground(getDrawable(R.drawable.x));
        moveStack.push(currentPosition);
        player1.setPath(player1.getPath()+""+currentPosition);
        b.setEnabled(false);
        b.setAlpha(0.3F);
        enableDisableGameLayout(true);
        toggleCurrentTurn();
        if(Rule.bruteForceCheck(player1.getPath()))
            gameOverScreen(getDrawable(R.drawable.x_won));
        else if(moveStack.size()>=9)
        {
            Toast.makeText(OnlineTicTacToe.this,"Game Tied",Toast.LENGTH_SHORT).show();
            gameReset();
        }
    }
    public void gameOverScreen(Drawable d)
    {
        final Dialog gameOver=new Dialog(OnlineTicTacToe.this);
        gameOver.setContentView(R.layout.game_over);
        ImageButton winMessage=gameOver.findViewById(R.id.winMessage);
        winMessage.setBackground(d);
        ImageButton quit=gameOver.findViewById(R.id.quit);
        ImageButton playAgain=gameOver.findViewById(R.id.playAgain);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameReset();
                gameOver.cancel();
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
        gameOver.show();
    }

    public void gameReset()
    {
        int size=moveStack.size();
        for(int i=0;i<size;i++)
        {
            undoLastMove();
        }
    }
    public void modifyPath()
    {
        if(currentTurn==1)
        {
            player1.setPath(player1.getPath().substring(0,player1.getPath().length()-1));
            toggleCurrentTurn();
        }
        else
        {
            player2.setPath(player2.getPath().substring(0,player2.getPath().length()-1));
            toggleCurrentTurn();
        }
    }
    public void toggleCurrentTurn()
    {
        if(currentTurn==1)
            currentTurn=2;
        else
            currentTurn=1;
    }
    public void undoLastMove()
    {
        if(moveStack.size()>0)
        {
            int i=moveStack.pop();
            switch (i)
            {
                case 1:o1.setEnabled(true);
                    o1.setBackground(getDrawable(R.drawable.plain));
                    modifyPath();
                    break;
                case 2:o2.setEnabled(true);
                    o2.setBackground(getDrawable(R.drawable.plain));
                    modifyPath();
                    break;
                case 3:o3.setEnabled(true);
                    o3.setBackground(getDrawable(R.drawable.plain));
                    modifyPath();
                    break;
                case 4:o4.setEnabled(true);
                    o4.setBackground(getDrawable(R.drawable.plain));
                    modifyPath();
                    break;
                case 5:o5.setEnabled(true);
                    o5.setBackground(getDrawable(R.drawable.plain));
                    modifyPath();
                    break;
                case 6:o6.setEnabled(true);
                    o6.setBackground(getDrawable(R.drawable.plain));
                    modifyPath();
                    break;
                case 7:o7.setEnabled(true);
                    o7.setBackground(getDrawable(R.drawable.plain));
                    modifyPath();
                    break;
                case 8:o8.setEnabled(true);
                    o8.setBackground(getDrawable(R.drawable.plain));
                    modifyPath();
                    break;
                case 9:o9.setEnabled(true);
                    o9.setBackground(getDrawable(R.drawable.plain));
                    modifyPath();
                    break;
            }
        }

    }
    public class PlayerMoveReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences=OnlineTicTacToe.this.getSharedPreferences(GAME_MOVE_REQUEST,MODE_PRIVATE);
            String id=sharedPreferences.getString(GAME_ID,"NONE");
            if(id.equals(gameID))
            {
                String player=sharedPreferences.getString(PLAYER_TYPE,"NONE");
                switch (player)
                {
                    case PLAYER_1:
                        receiveOnlineX(Integer.parseInt(sharedPreferences.getString(MOVE,"NONE")));
                        break;
                    case PLAYER_2:
                        receiveOnlineO(Integer.parseInt(sharedPreferences.getString(MOVE,"NONE")));
                        break;
                }
            }
            else
            {
                Toast.makeText(OnlineTicTacToe.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void enableDisableGameLayout(boolean value)
    {
        if(!o1.isEnabled())
        {
            o1.setEnabled(value);
            o1.setAlpha(0.3F);
        }
        if(!o2.isEnabled())
        {
            o2.setEnabled(value);
            o2.setAlpha(0.3F);
        }
        if(!o3.isEnabled())
        {
            o3.setEnabled(value);
            o3.setAlpha(0.3F);
        }
        if(!o4.isEnabled())
        {
            o4.setEnabled(value);
            o4.setAlpha(0.3F);
        }
        if(!o5.isEnabled())
        {
            o5.setEnabled(value);
            o5.setAlpha(0.3F);
        }
        if(!o6.isEnabled())
        {
            o6.setEnabled(value);
            o6.setAlpha(0.3F);
        }
        if(!o7.isEnabled())
        {
            o7.setEnabled(value);
            o7.setAlpha(0.3F);
        }
        if(!o8.isEnabled())
        {
            o8.setEnabled(value);
            o8.setAlpha(0.3F);
        }
        if(!o9.isEnabled())
        {
            o9.setEnabled(value);
            o9.setAlpha(0.3F);
        }



    }
    public void disableScreen()
    {
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(null);
    }
    public void enableScreen()
    {
        dialog.cancel();
    }
}
