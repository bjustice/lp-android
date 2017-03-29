package com.brianjustice.listpersist.broadcastreceivertools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.brianjustice.listpersist.Screens.ListsScreen;
import com.brianjustice.listpersist.datatools.SharedPreferenceService;
import com.brianjustice.listpersist.datatools.URLData;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Brian on 1/8/2017.
 */

public class UserBroadcastReceiver extends BroadcastReceiver {

    String username;
    String pass;
    String checkType;
    private UserBroadcastType userBroadcastType;
    private static final String NEW_USER_STRING = "new_user_string";
    private static final String LOGIN_RESULT_STRING = "login_result_string";
    private static final String GOOGLE_LOGIN_STRING = "google_login_string";

    public Intent createAddNewUser(Context context, String un, String p, String email){
        username = un;
        pass = p;
        userBroadcastType = UserBroadcastType.NEW_USER;
        String requestURL = (new URLData().getStartAndDomain())+"/adduser.php?username="+username+"&pass="+pass+"&email="+email;

        Intent intent = new Intent(context, SharedPreferenceService.class);
        intent.putExtra(SharedPreferenceService.URL,
                requestURL);
        intent.putExtra(SharedPreferenceService.PREFERENCESTRING,NEW_USER_STRING);
        Log.i("AddUserURL",requestURL);
        return intent;
    }

    public Intent createVerifyUser(Context context, String un, String p, String ct){
        username = un;
        pass = p;
        checkType = ct;
        userBroadcastType = UserBroadcastType.LOGIN;
        String requestURL = "http://www.listpersist.com/datawork/verifylogin.php?username="+username+"&pass="+pass;

        Intent intent = new Intent(context, SharedPreferenceService.class);
        intent.putExtra(SharedPreferenceService.URL,
                requestURL);
        intent.putExtra(SharedPreferenceService.PREFERENCESTRING,LOGIN_RESULT_STRING);
        Log.i("LoginURL",requestURL);
        return intent;
    }

    public Intent createVerifyGoogleUser(Context context, String googleID){
        userBroadcastType = UserBroadcastType.GOOGLE_USER;
        String requestURL = "http://wwww.listpersist.com/datawork/verifygooglelogin.php?googleid="+googleID;

        Intent intent = new Intent(context, SharedPreferenceService.class);
        intent.putExtra(SharedPreferenceService.URL,
                requestURL);
        intent.putExtra(SharedPreferenceService.PREFERENCESTRING,GOOGLE_LOGIN_STRING);
        Log.i("VerifyGoogleURL",requestURL);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int resultCode = bundle.getInt(SharedPreferenceService.RESULT);
            SharedPreferences listData = context.getSharedPreferences("list", 0);
            SharedPreferences userData = ((Activity)context).getBaseContext().getSharedPreferences("userdata",0);
            if (resultCode == RESULT_OK) {
                switch(userBroadcastType) {
                    case NEW_USER:
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
                    case LOGIN:
                        String loginResult = listData.getString(LOGIN_RESULT_STRING,"0 results");
                        String[] resultArray = loginResult.split(",");

                        if(resultArray.length>1){
                            String userID = resultArray[1];
                            if(checkType.equals("login")) {
                                SharedPreferences.Editor spEditor = userData.edit();
                                spEditor.putString("userdata",username+","+pass);
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
                    case GOOGLE_USER:
                        String googleLoginResult = listData.getString(GOOGLE_LOGIN_STRING,"0 results");
                        String[] googleResultArray = googleLoginResult.split(",");

                        if(googleResultArray.length>1){
                            String userID = googleResultArray[1];
                            SharedPreferences.Editor spEditor = userData.edit();
                            spEditor.putString("userdata",userID);
                            spEditor.commit();

                            Intent activityIntent = new Intent(context, ListsScreen.class);
                            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activityIntent.putExtra("userID",userID);
                            context.startActivity(activityIntent);
                        }
                        break;
                }
            }
        }
    }

    enum UserBroadcastType{
        LOGIN,
        NEW_USER,
        GOOGLE_USER;
    }
}

