package com.brianjustice.listpersist.datatools;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public static final String NOTIFICATION = "com.brianjustice.allmylists";

    public SharedPreferenceService() {
        super("SharedPreferenceService");
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {
        String urlPath = intent.getStringExtra(URL);
        String listPreference = intent.getStringExtra(PREFERENCESTRING);

        if(hasConnection()) {
            InputStream stream = null;
            try {
                java.net.URL url = new URL(urlPath);
                stream = url.openConnection().getInputStream();
                InputStreamReader reader = new InputStreamReader(stream);
                BufferedReader bufferedReadereader = new BufferedReader(reader);
                StringBuilder prefString = new StringBuilder();
                String line;
                while ((line = bufferedReadereader.readLine()) != null) {
                    prefString.append(line);
                }
                //Save shared preference
                SharedPreferences listData = getBaseContext().getSharedPreferences("list", 0);
                SharedPreferences.Editor spEditor = listData.edit();
                spEditor.putString(listPreference, prefString.toString());
                spEditor.commit();

                // successfully finished
                result = Activity.RESULT_OK;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else{
            result = NO_CONNECTION;
        }
        publishResults(result);
    }

    private boolean hasConnection(){
        Context context = getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork != null && activeNetwork.isConnected()){
            try {
                URL url = new URL("http://www.listpersist.com");
                HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
                urlc.setRequestProperty("User-Agent","test");
                urlc.setRequestProperty("Connection","close");
                urlc.setConnectTimeout(1000);
                urlc.connect();
                if(urlc.getResponseCode() == 200){
                    Log.i("CONNECTIONVALIDATOR","TRUE");
                    return true;
                }
                else{
                    Log.i("CONNECTIONVALIDATOR","FALSE");
                    return false;
                }
            }
            catch (IOException e){
                Log.i("ERROR", "Error checking internet connection", e);
                return false;
            }
        }
        else {
            Log.i("CONNECTIONVALIDATOR", "FALSE");
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
