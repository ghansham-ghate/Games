package com.gghate.tictactoe.HTTP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gghate.tictactoe.ConfirmOnlineGame;
import com.gghate.tictactoe.Message.Message;
import com.gghate.tictactoe.OnlineTicTacToe;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.gghate.tictactoe.Constant.ACCEPTOR;
import static com.gghate.tictactoe.Constant.BODY;
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
import static com.gghate.tictactoe.Constant.TITLE;
import static com.gghate.tictactoe.Constant.TYPE;

public class FCMHTTPRequest {
    public static RequestQueue requestQueue=null;
    public static  final String URL="https://fcm.googleapis.com/fcm/send";
    public static final String TOKEN="AAAAKGjRtcI:APA91bEldmxShiNx84brg1Ong5vHuBVdLzRdS3ykG0FcQ4F9pohFW3tQqIR-En4w7KGh9gCxJ-JBXEGJ6Q3LmwljTQwfqBULOeMwoLYu9FNGKPu69R-1gh4NKmKM-Ddg8370tJ2rqom8";
    public static RequestQueue getRequestQueue(Context context)
    {
        if(requestQueue==null)
        {
            requestQueue=Volley.newRequestQueue(context);
        }

        return requestQueue;
    }

    public static void sendMessage(final Context context,String topic,String gameID,String playerType,final int move)
    {  Toast.makeText(context,"topic"+topic,Toast.LENGTH_SHORT).show();
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("to","/topics/"+topic);
            JSONObject notification=new JSONObject();
            JSONObject data=new JSONObject();
            notification.put(TITLE,"New Game Request");
            notification.put(BODY,topic+" Confirmed game request start playing");
            data.put(TYPE,GAME_MOVE_REQUEST);
            data.put(GAME_ID,gameID);
            data.put(PLAYER_TYPE,playerType);
            data.put(MOVE,move);
            jsonObject.put("notification",notification);
            jsonObject.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(URL,jsonObject,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Move",""+move);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> authMap=new HashMap<>();
                authMap.put("Content-Type","application/json");
                authMap.put("Authorization","key="+TOKEN);
                return authMap;
            }
        };
        getRequestQueue(context).add(jsonObjectRequest);
    }

    public static void sendGameRequest(final Context context,final String phoneNumber,final String topic)
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("to","/topics/"+topic);
            JSONObject notification=new JSONObject();
            JSONObject data=new JSONObject();
            notification.put(TITLE,"New Game Request");
            notification.put(BODY,phoneNumber+" wants to play TicTacToe with you");
            data.put(TYPE,NEW_GAME_REQUEST);
            data.put(GAME_ID,topic.substring(0,5)+phoneNumber.substring(0,5));
            data.put(REQUESTOR,phoneNumber);
            data.put(ACCEPTOR,topic);
            jsonObject.put("notification",notification);
            jsonObject.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(URL,jsonObject,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences=context.getSharedPreferences(GAME_REQUEST,MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString(EXPIRE_IN,""+System.currentTimeMillis());
                editor.putString(REQUESTOR,phoneNumber);
                editor.putString(ACCEPTOR,topic);
                editor.putString(GAME_ID,topic.substring(0,5)+phoneNumber.substring(0,5));
                editor.apply();
                editor.commit();
                Intent intent=new Intent(context, ConfirmOnlineGame.class);
                intent.putExtra(PLAYER_TYPE,PLAYER_1);
                context.startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> authMap=new HashMap<>();
                authMap.put("Content-Type","application/json");
                authMap.put("Authorization","key="+TOKEN);
                return authMap;
            }
        };
        getRequestQueue(context).add(jsonObjectRequest);
    }
    public static void confirmGameRequest(final Context context, final String requestor, final String acceptor, final String gameID,final Button confirmButton)
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("to","/topics/"+requestor);
            JSONObject notification=new JSONObject();
            JSONObject data=new JSONObject();
            notification.put(TITLE,"New Game Request");
            notification.put(BODY,acceptor+" Confirmed game request start playing");
            data.put(TYPE,GAME_CONFIRM_REQUEST);
            data.put(GAME_ID,gameID);
            data.put(REQUESTOR,requestor);
            data.put(ACCEPTOR,acceptor);
            jsonObject.put("notification",notification);
            jsonObject.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(URL,jsonObject,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, OnlineTicTacToe.class);
                intent.putExtra(GAME_ID,gameID);
                intent.putExtra(PLAYER_TYPE,PLAYER_2);
                intent.putExtra(ACCEPTOR,acceptor);
                intent.putExtra(REQUESTOR,requestor);
                context.startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();
                confirmButton.setAlpha(1F);
                confirmButton.setEnabled(false);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> authMap=new HashMap<>();
                authMap.put("Content-Type","application/json");
                authMap.put("Authorization","key="+TOKEN);
                return authMap;
            }
        };
        getRequestQueue(context).add(jsonObjectRequest);
    }
}
