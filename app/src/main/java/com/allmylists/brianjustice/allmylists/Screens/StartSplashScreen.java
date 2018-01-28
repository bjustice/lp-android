package com.allmylists.brianjustice.allmylists.Screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.allmylists.brianjustice.allmylists.R;
import com.allmylists.brianjustice.allmylists.broadcastreceivertools.UserBroadcastReceiver;
import com.allmylists.brianjustice.allmylists.datatools.SharedPreferenceService;

public class StartSplashScreen extends AppCompatActivity {

    private UserBroadcastReceiver userBroadcastReceiver;
    private Context context;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(userBroadcastReceiver, new IntentFilter(
                SharedPreferenceService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        attemptUnregisterReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        attemptUnregisterReceiver();
    }

    private void attemptUnregisterReceiver() {
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
        context = this;

        final View view = (View) findViewById(R.id.content_start_splash_screen);
        view.setBackgroundColor(Color.WHITE);

        SharedPreferences userData = ((Activity)context).getBaseContext().getSharedPreferences("userdata",0);
        String userDataString = userData.getString("userdata","");
        boolean googleUser = userData.getBoolean("google_user",false);

        if (userDataString.equals("no") || userDataString.equals("")) {
            Log.i("Login","no user found");
            noSavedUserFound();
        } else {
            Log.i("Login","user found. googleuser:"+googleUser);
            String userDataStringArray[] = userDataString.split(",");
            if (userDataStringArray.length == 2 || googleUser) {
                validSavedUserFound(userDataStringArray,googleUser);
            } else {
                invalidSavedUserFound();
            }
        }
    }

    //takes to Enter screen to log in or register new account
    private void noSavedUserFound(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(context, EnterScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 500);
    }

    //logs in as existing user that was saved
    private void validSavedUserFound(String[] userDataStringArray, boolean googleUser){
        if(googleUser){
            Intent activityIntent = new Intent(context, ListsScreen.class);
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activityIntent.putExtra("userID",userDataStringArray[0]);
            activityIntent.putExtra("google_user",true);
            startActivity(activityIntent);
        }
        else {
            final String user = userDataStringArray[0];
            final String pass = userDataStringArray[1];
            userBroadcastReceiver = new UserBroadcastReceiver();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    startService(userBroadcastReceiver.createValidateUser(context, user, pass, "alreadyloggedincheck"));
                }
            }, 1200);
        }
    }

    //found as user, but the format was corrupted. direct to login screen
    private void invalidSavedUserFound(){
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
