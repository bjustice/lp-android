package com.allmylists.brianjustice.allmylists.broadcastreceivertools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.allmylists.brianjustice.allmylists.Screens.ListsScreen;
import com.allmylists.brianjustice.allmylists.datatools.SharedPreferenceService;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Brian on 1/8/2017.
 */

public class UserBroadcastReceiver extends BroadcastReceiver {

    String username;
    String pass;
    String checkType;
    String googleUserId;
    private String userBroadcastType;
    private static final String NEW_USER_STRING = "new_user_string";
    private static final String LOGIN_RESULT_STRING = "login_result_string";
    private static final String GOOGLE_LOGIN_STRING = "google_login_string";

    public Intent createAddNewUser(Context context, String un, String p, String email){
        username = un;
        pass = p;
        userBroadcastType =NEW_USER_STRING;
        String[] requestVariables = new String[3];
        requestVariables[0] = "username,"+username;
        requestVariables[1] = "pass,"+pass;
        requestVariables[2] = "email,"+email;
        return initializeIntent(context,userBroadcastType, SharedPreferenceService.USER_REQUEST_CREATE, requestVariables);
    }

    public Intent createValidateUser(Context context, String un, String p, String ct){
        username = un;
        pass = p;
        checkType = ct;
        userBroadcastType = LOGIN_RESULT_STRING;
        String[] requestVariables = new String[2];
        requestVariables[0] = "username,"+username;
        requestVariables[1] = "pass,"+pass;
        return initializeIntent(context,userBroadcastType, SharedPreferenceService.USER_REQUEST_VALIDATE, requestVariables);
    }

    public Intent createVerifyGoogleUser(Context context, String idToken, String userId){
        googleUserId = userId;
        userBroadcastType = GOOGLE_LOGIN_STRING;
        String[] requestVariables = new String[1];
        requestVariables[0] = "id_token,"+idToken;
        return initializeIntent(context,userBroadcastType,SharedPreferenceService.USER_GOOGLE, requestVariables);
    }

    private Intent initializeIntent(Context context, String typeString, String action, String[] requestParameters){
        Intent intent = new Intent(context, SharedPreferenceService.class);
        intent.putExtra(SharedPreferenceService.REQUEST_TABLE,"user");
        intent.putExtra(SharedPreferenceService.ACTION,action);
        intent.putExtra(SharedPreferenceService.ACTION_PARAMETERS,requestParameters);
        intent.putExtra(SharedPreferenceService.PREFERENCESTRING,typeString);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int resultCode = bundle.getInt(SharedPreferenceService.RESULT);
            SharedPreferences listData = context.getSharedPreferences("list", 0);
            SharedPreferences userData = ((Activity)context).getBaseContext().getSharedPreferences("userdata",0);
            Log.i("Receive,UserBroadcast:",userBroadcastType);
            if (resultCode == RESULT_OK) {
                switch(userBroadcastType) {
                    case NEW_USER_STRING:
                        String newUserResult = listData.getString(NEW_USER_STRING, "0 results");
                        String[] newUserResultArray = newUserResult.split(",");

                        if (newUserResultArray.length > 1) {
                            String userID = newUserResultArray[1];
                            //Save shared preference
                            SharedPreferences.Editor spEditor = userData.edit();
                            spEditor.putString("userdata",username+","+pass);
                            spEditor.commit();

                            Intent activityIntent = new Intent(context, ListsScreen.class);
                            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activityIntent.putExtra("userID", userID);
                            context.startActivity(activityIntent);
                        } else {
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, newUserResult, duration);
                            toast.show();
                        }
                        break;
                    case LOGIN_RESULT_STRING:
                        String loginResult = listData.getString(LOGIN_RESULT_STRING,"0 results");
                        String[] resultArray = loginResult.split(",");

                        if(resultArray.length>1){
                            String userID = resultArray[1];
                            if(checkType.equals("login")) {
                                SharedPreferences.Editor spEditor = userData.edit();
                                spEditor.putString("userdata",username+","+pass);
                                spEditor.putBoolean("google_user",false);
                                spEditor.commit();
                            }
                            Intent activityIntent = new Intent(context, ListsScreen.class);
                            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activityIntent.putExtra("userID",userID);
                            context.startActivity(activityIntent);
                        }
                        else{
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, loginResult, duration);
                            toast.show();
                        }
                        break;
                    case GOOGLE_LOGIN_STRING:
                        SharedPreferences.Editor spEditor = userData.edit();
                        spEditor.putString("userdata",googleUserId);
                        spEditor.putBoolean("google_user",true);
                        spEditor.commit();

                        Intent activityIntent = new Intent(context, ListsScreen.class);
                        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activityIntent.putExtra("userID",googleUserId);
                        activityIntent.putExtra("google_user",true);
                        context.startActivity(activityIntent);
                        break;
                }
            }
        }
    }

}

