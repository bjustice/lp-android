package com.brianjustice.listpersist.Screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.brianjustice.listpersist.R;
import com.brianjustice.listpersist.broadcastreceivertools.UserBroadcastReceiver;
import com.brianjustice.listpersist.datatools.SharedPreferenceService;

public class StartSplashScreen extends AppCompatActivity {

    private UserBroadcastReceiver userBroadcastReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(userBroadcastReceiver, new IntentFilter(
                SharedPreferenceService.NOTIFICATION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        attemptUnregsiterReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        attemptUnregsiterReceiver();
    }

    private void attemptUnregsiterReceiver() {
        try {
            unregisterReceiver(userBroadcastReceiver);
        } catch (Exception e) {
            //do nothing, necessary because cannot check if already unregistered.
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_splash_screen);
        final Context context = this;

        final View view = (View) findViewById(R.id.content_start_splash_screen);
        view.setBackgroundColor(Color.WHITE);

        SharedPreferences userData = ((Activity)context).getBaseContext().getSharedPreferences("userdata",0);
        String userDataString = userData.getString("userdata","");

        if(userDataString.equals("no")||userDataString.equals("")){

            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                public void run(){
                    Intent intent = new Intent(context, EnterScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            },500);
        }
        else {
            String userDataStringArray[] = userDataString.split(",");
            if(userDataStringArray.length == 2) {
                final String user = userDataStringArray[0];
                final String pass = userDataStringArray[1];
                userBroadcastReceiver = new UserBroadcastReceiver();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startService(userBroadcastReceiver.createVerifyUser(context, user, pass, "alreadyloggedincheck"));
                    }
                }, 1200);

            }
            else{
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(context, EnterScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }, 500);
            }

        }
    }

}
