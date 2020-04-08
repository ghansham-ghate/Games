package com.gghate.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gghate.tictactoe.Configurations.ConfigureOX;
import com.gghate.tictactoe.Players.Player;
import com.gghate.tictactoe.Rules.Rule;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Player player1,player2;
    Button o1,o2,o3,o4,o5,o6,o7,o8,o9,undo1,undo2;
    Stack<Integer> moveStack=new Stack<>();
    RelativeLayout gameArea;
    int currentTurn=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intialize();
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
    }
    public void loadConfiguration()
    {

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
        if(view.getId()==R.id.undoPlayer2)
            undoLastMove();
    }

    public void makeMove(Button b,int currentPosition,int turn)
    {
       if(turn==1)
           moveX(b,currentPosition);
       else
           moveO(b,currentPosition);
    }

    public void moveX(Button b,int currentPosition)
    {
        b.setBackground(getDrawable(R.drawable.x));
        moveStack.push(currentPosition);
        player2.setPath(player2.getPath()+""+currentPosition);
        //player2.setPath(Rule.sortString(player2.getPath()));
       // Toast.makeText(this,player2.getPath(),Toast.LENGTH_SHORT).show();
        toggleCurrentTurn();
        b.setEnabled(false);
        if(Rule.bruteForceCheck(player2.getPath()))
            gameOverScreen(getDrawable(R.drawable.x_won));
        else if(moveStack.size()>=9)
        {
            Toast.makeText(MainActivity.this,"Game Tied",Toast.LENGTH_SHORT).show();
            gameReset();
        }
    }
    public  void moveO(Button b,int currentPosition)
    {
        b.setBackground(getDrawable(R.drawable.o));
        moveStack.push(currentPosition);
        player1.setPath(player1.getPath()+""+currentPosition);
        //player1.setPath(Rule.sortString(player1.getPath()));
      //  Toast.makeText(this,player1.getPath(),Toast.LENGTH_SHORT).show();
        toggleCurrentTurn();
        b.setEnabled(false);
        if(Rule.bruteForceCheck(player1.getPath()))
            gameOverScreen(getDrawable(R.drawable.o_won));
        else if(moveStack.size()>=9){
            Toast.makeText(MainActivity.this,"Game Tied",Toast.LENGTH_SHORT).show();
            gameReset();
        }
    }

    public void toggleCurrentTurn()
    {
        if(currentTurn==1)
            currentTurn=2;
        else
            currentTurn=1;
    }
    public void gameOverScreen(Drawable d)
    {
        final Dialog gameOver=new Dialog(MainActivity.this);
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
}
