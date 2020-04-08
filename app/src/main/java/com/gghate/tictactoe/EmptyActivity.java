package com.gghate.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gghate.tictactoe.FCMOnlineSettings.FCMConfig;
import com.gghate.tictactoe.HTTP.FCMHTTPRequest;
import com.google.firebase.messaging.FirebaseMessaging;

import com.google.firebase.messaging.RemoteMessage;

import static com.gghate.tictactoe.Constant.ACCEPTOR;
import static com.gghate.tictactoe.Constant.EXPIRE_IN;
import static com.gghate.tictactoe.Constant.GAME_REQUEST;
import static com.gghate.tictactoe.Constant.NEW_GAME_REQUEST;
import static com.gghate.tictactoe.Constant.PLAYER_2;
import static com.gghate.tictactoe.Constant.PLAYER_TYPE;
import static com.gghate.tictactoe.Constant.TYPE;

public class EmptyActivity extends AppCompatActivity {
    MyBroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

    }

    public void goToOfflineActivity(View view) {
        Intent intent = new Intent(EmptyActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onlineMode(View view) {

        if (!profileCheck()) {
            createProfileDialog();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
            FCMConfig.suscribeToTopic(sharedPreferences.getString("PhoneNumber", "None"), EmptyActivity.this);
            getOnlinePartnerNumber();
        }

    }

    public boolean profileCheck() {
        SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
        if (sharedPreferences.getString("PhoneNumber", "None").equals("None")) {
            return false;
        } else {
            return true;
        }
    }

    public void createProfileDialog() {
        final Dialog profileDialog = new Dialog(EmptyActivity.this);
        profileDialog.setContentView(R.layout.input_dialog);
        final EditText phoneNumberText = (EditText) profileDialog.findViewById(R.id.phoneNumber);
        Button cancel = (Button) profileDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileDialog.cancel();
            }
        });
        Button ok = (Button) profileDialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneNumberText.getText().toString().trim().length() == 10) {
                    createProfile(phoneNumberText.getText().toString());
                    profileDialog.cancel();
                } else {
                    Toast.makeText(EmptyActivity.this, "Phone Number Should have 10 digit", Toast.LENGTH_SHORT).show();

                }
            }
        });
        profileDialog.show();
    }

    public void createProfile(String phoneNumber) {
        SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PhoneNumber", phoneNumber);
        editor.apply();
        editor.commit();
        Toast.makeText(EmptyActivity.this, "Profile Created !", Toast.LENGTH_SHORT).show();
        FCMConfig.suscribeToTopic(sharedPreferences.getString("PhoneNumber", "None"), EmptyActivity.this);
        getOnlinePartnerNumber();
    }

    public void getOnlinePartnerNumber() {
        final Dialog profileDialog = new Dialog(EmptyActivity.this);
        profileDialog.setContentView(R.layout.input_dialog);
        final EditText phoneNumberText = (EditText) profileDialog.findViewById(R.id.phoneNumber);
        Button cancel = (Button) profileDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileDialog.cancel();
            }
        });
        final TextView dialog_title = (TextView) profileDialog.findViewById(R.id.dialog_title);
        dialog_title.setText("Enter the Player's phone number you want to play with");
        Button ok = (Button) profileDialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneNumberText.getText().toString().trim().length() == 10) {
                    sendGameRequest(phoneNumberText.getText().toString().trim());
                    profileDialog.cancel();
                } else {
                    Toast.makeText(EmptyActivity.this, "Phone Number Should have 10 digit", Toast.LENGTH_SHORT).show();

                }
            }
        });
        profileDialog.show();
    }

    public void sendGameRequest(String topic) {
        SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
        String requestor = sharedPreferences.getString("PhoneNumber", "None");
        FCMHTTPRequest.sendGameRequest(EmptyActivity.this, requestor, topic);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invokeGameRequest();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.my.app.onMessageReceived");
        receiver= new MyBroadcastReceiver();
        registerReceiver(receiver, intentFilter);
    }

    public void invokeGameRequest() {
        SharedPreferences sharedPreferences = getSharedPreferences(GAME_REQUEST, MODE_PRIVATE);
        if (!sharedPreferences.getString(EXPIRE_IN, "NONE").equals("NONE")) {
            String timeString = sharedPreferences.getString(EXPIRE_IN, "NONE");
            if (!timeString.equals("NONE")) {
                long time = Long.parseLong(timeString);
                if ( System.currentTimeMillis() <=time) {
                    Intent intent = new Intent(EmptyActivity.this, ConfirmOnlineGame.class);
                    intent.putExtra(PLAYER_TYPE, PLAYER_2);
                    startActivity(intent);
                } else {
                    Toast.makeText(EmptyActivity.this, "Request Expired", Toast.LENGTH_SHORT).show();
                    sharedPreferences.edit().clear().commit();
                }
            }
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            invokeGameRequest();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }
}
