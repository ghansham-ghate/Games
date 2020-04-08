package com.gghate.tictactoe.FCMOnlineSettings;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


public class FCMConfig {
    public static void suscribeToTopic(String topic,final Context context)
    {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "Suscription Failed. Please check network connectivity", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(context, "Suscription Success", Toast.LENGTH_SHORT).show();

                        }
                        //Log.d(TAG, msg);
                    }
                });
    }
}
