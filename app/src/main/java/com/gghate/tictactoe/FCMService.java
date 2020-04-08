package com.gghate.tictactoe;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.gghate.tictactoe.Constant.ACCEPTOR;
import static com.gghate.tictactoe.Constant.EXPIRE_IN;
import static com.gghate.tictactoe.Constant.GAME_CONFIRM_REQUEST;
import static com.gghate.tictactoe.Constant.GAME_ID;
import static com.gghate.tictactoe.Constant.GAME_MOVE_REQUEST;
import static com.gghate.tictactoe.Constant.GAME_REQUEST;
import static com.gghate.tictactoe.Constant.MOVE;
import static com.gghate.tictactoe.Constant.NEW_GAME_REQUEST;
import static com.gghate.tictactoe.Constant.PLAYER_1;
import static com.gghate.tictactoe.Constant.PLAYER_2;
import static com.gghate.tictactoe.Constant.PLAYER_TYPE;
import static com.gghate.tictactoe.Constant.REQUESTOR;

public class FCMService extends FirebaseMessagingService {
    public FCMService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       // super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData() != null) {
            String type=remoteMessage.getData().get("type");
            if(type.equals(NEW_GAME_REQUEST))
            {
                SharedPreferences sharedPreferences=getSharedPreferences(GAME_REQUEST,MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                long time=System.currentTimeMillis();
                editor.putString(EXPIRE_IN,""+time+120000);
                editor.putString(REQUESTOR,remoteMessage.getData().get(REQUESTOR));
                editor.putString(ACCEPTOR,remoteMessage.getData().get(ACCEPTOR));
                editor.putString(GAME_ID,remoteMessage.getData().get(GAME_ID));
                editor.putString(PLAYER_TYPE,PLAYER_2);
                editor.apply();
                editor.commit();
                Log.d("Received Data New Game",remoteMessage.getData().toString());
                remoteMessage.getNotification();
                Intent intent = new Intent();
                intent.setAction("com.my.app.onMessageReceived");
                sendBroadcast(intent);
            }
            else if(type.equals(GAME_CONFIRM_REQUEST))
            {
                remoteMessage.getNotification();
                SharedPreferences sharedPreferences=getSharedPreferences(GAME_REQUEST,MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString(REQUESTOR,remoteMessage.getData().get(REQUESTOR));
                editor.putString(GAME_ID,remoteMessage.getData().get(GAME_ID));
                editor.putString(ACCEPTOR,remoteMessage.getData().get(ACCEPTOR));
                editor.putString(PLAYER_TYPE,PLAYER_1);
                editor.putString("request","accepted");
                editor.apply();
                editor.commit();
                Log.d("Confirmed Data New Game",remoteMessage.getData().toString());
            }
            else if(type.equals(GAME_MOVE_REQUEST))
            {
                SharedPreferences sharedPreferences=getSharedPreferences(GAME_MOVE_REQUEST,MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString(GAME_ID,remoteMessage.getData().get(GAME_ID));
                editor.putString(MOVE,remoteMessage.getData().get(MOVE));
                editor.putString(PLAYER_TYPE,remoteMessage.getData().get(PLAYER_TYPE));
                editor.apply();
                editor.commit();
                Intent intent = new Intent();
                intent.setAction("com.my.app.onMoveMessageReceived");
                sendBroadcast(intent);
                Log.d("MOVE FCM", remoteMessage.getData().toString());
            }
            Log.d("FCM", "Message Notification Body: " + remoteMessage.getData().toString());
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}
