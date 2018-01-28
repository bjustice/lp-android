package com.allmylists.brianjustice.allmylists.datatools;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Brian on 2/5/2017.
 */

public class SharedPreferenceService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    private static final int NO_CONNECTION = 1234;
    public static final String URL = "urlpath";
    public static final String RESULT = "result";
    public static final String PREFERENCESTRING = "listpreference";
    public static final String NOTIFICATION = "com.allmylists.brianjustice.allmylists";
    public static final String REQUEST_TABLE = "request_table";
    public static final String ACTION = "action";
    public static final String ACTION_PARAMETERS = "action_parameters";

    public static final String USER_REQUEST_CREATE = "create";
    public static final String USER_REQUEST_VALIDATE = "validate";
    public static final String USER_GOOGLE = "google_validate";

    public static final String LIST_ADD = "add";
    public static final String LIST_ALL_OWNED = "allowned";
    public static final String LIST_DELETE = "delete";
    public static final String LIST_CHANGE_COLOR = "changecolor";
    public static final String LIST_CHANGE_NAME = "changename";

    public static final String ITEM_ADD = "add";
    public static final String ITEM_ALL_OWNED = "alllistitems";
    public static final String ITEM_DELETE = "delete";
    public static final String ITEM_CHECK = "check";
    public static final String ITEM_CHANGE_COLOR = "changecolor";
    public static final String ITEM_CHANGE_NAME = "changename";

    public SharedPreferenceService() {
        super("SharedPreferenceService");
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {
        String listPreference = intent.getStringExtra(PREFERENCESTRING);
        String requestTable = intent.getStringExtra("request_table");
        String action = intent.getStringExtra("action");
        String[] actionParameters = intent.getStringArrayExtra("action_parameters");

        if (hasConnection()) {
            jsonTesting(requestTable,listPreference,action,actionParameters);
        } else {
            result = NO_CONNECTION;
            publishResults(result);
        }
    }

    private void jsonTesting(String requestTable, String listPreference, String action, String[] actionParameters){
        InputStream inStream = null;
        HttpURLConnection conn;
        try {
            URL url = new URL("https://listpersist.com/datawork/persistapi.php/"+requestTable+"/"+action);
            Log.i("userbroadcast_url",url.toString());

            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");

            JSONObject jsonData = new JSONObject();
            jsonData.put("method", "GET");
            for(String currentParam : actionParameters){
                String[] paramNameAndData = currentParam.split(",");
                jsonData.put(paramNameAndData[0],paramNameAndData[1]);
            }

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(jsonData.toString());
            wr.flush();

            inStream = conn.getInputStream();
            InputStreamReader reader = new InputStreamReader(inStream);
            BufferedReader bufferedReadereader = new BufferedReader(reader);
            StringBuilder prefString = new StringBuilder();
            String line;
            while ((line = bufferedReadereader.readLine()) != null) {
                prefString.append(line);
            }
            Log.i("received",prefString.toString());
            //Save shared preference
            SharedPreferences listData = getBaseContext().getSharedPreferences("list", 0);
            SharedPreferences.Editor spEditor = listData.edit();
            spEditor.putString(listPreference, prefString.toString());
            spEditor.commit();

            // successfully finished
            result = Activity.RESULT_OK;

        } catch (Exception e) {
            Log.i("ConnectionError", e.toString());
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        publishResults(result);
    }

    private boolean hasConnection(){
        Context context = getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork != null && activeNetwork.isConnected()){
            try {
                URL url = new URL("https://www.listpersist.com");
                HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
                urlc.setRequestProperty("User-Agent","test");
                urlc.setRequestProperty("Connection","close");
                urlc.setConnectTimeout(1000);
                urlc.connect();
                if(urlc.getResponseCode() == 200){
                    Log.i("internet_connection","TRUE");
                    return true;
                }
                else{
                    Log.i("internet_connection","FALSE");
                    return false;
                }
            }
            catch (IOException e){
                Log.i("ERROR", "Error checking for internet connection", e);
                return false;
            }
        }
        else {
            Log.i("internet_connection", "FALSE");
            return false;
        }
    }

    private void publishResults(int result) {
        Log.i("SharedPrefPublished",result+"");
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra("service","sharedpreferences");
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}
