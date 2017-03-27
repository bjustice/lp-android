package com.brianjustice.listpersist.broadcastreceivertools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.brianjustice.listpersist.datatools.SharedPreferenceService;
import com.brianjustice.listpersist.uitools.list.ListRowBuilder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Brian on 10/8/2016.
 */

public class ListBroadcastReceiver extends BroadcastReceiver{

    private String currentIntent;
    public static final String ADD_LIST_STRING = "addliststring";
    public static final String CHANGE_LIST_NAME_STRING = "changelistnamestring";
    public static final String CHANGE_LIST_COLOR_STRING = "changelistcolorstring";
    public static final String DELETE_LIST_STRING = "deleteliststring";
    public static final String ALL_LIST_STRING = "allliststring";
    private ListRowBuilder listRowBuilder;

    public ListBroadcastReceiver(ListRowBuilder lrb){
        listRowBuilder = lrb;
    }

    public Intent createAddListIntent(Context context, String listName, String ownerID, String listColor){
        currentIntent = ADD_LIST_STRING;
        String addListURL = "http://www.listpersist.com/datawork/addlist.php?ownerID="+ownerID+"&listname="+listName+"&listcolor="+listColor;
        return initializeIntent(context,addListURL,ADD_LIST_STRING);
    }

    public Intent createGetListIntent(Context context, String ownerID){
        currentIntent = ALL_LIST_STRING;
        String getListURL = "http://www.listpersist.com/datawork/getownedlists.php?ownerID="+ownerID;
        return initializeIntent(context,getListURL,ALL_LIST_STRING);
    }

    public Intent createDeleteListIntent(Context context, int listID){
        currentIntent = DELETE_LIST_STRING;
        String deleteListURL = "http://www.listpersist.com/datawork/deletelist.php?listid="+listID;
        return initializeIntent(context,deleteListURL, DELETE_LIST_STRING);
    }

    public Intent createChangeColorIntent(Context context, int listID, int listColor){
        currentIntent = CHANGE_LIST_COLOR_STRING;
        String changeColorURL = "http://www.listpersist.com/datawork/changelist.php?changetype=COLOR&listid="+listID+"&newlistcolor="+listColor;
        return initializeIntent(context,changeColorURL,CHANGE_LIST_COLOR_STRING);
    }

    public Intent createChangeListNameIntent(Context context, int listID, String listName){
        currentIntent = CHANGE_LIST_NAME_STRING;
        String changeNameURL = "http://www.listpersist.com/datawork/changelist.php?changetype=NAME&listid="+listID+"&newlistname="+listName;
        return initializeIntent(context,changeNameURL,CHANGE_LIST_NAME_STRING);
    }

    private Intent initializeIntent(Context context, String requestURL, String toCreateString){
        Intent intent = new Intent(context, SharedPreferenceService.class);
        //url that is being accessed
        intent.putExtra(SharedPreferenceService.URL,requestURL);
        //preference string to save an returned data
        intent.putExtra(SharedPreferenceService.PREFERENCESTRING, toCreateString);
        Log.i("listbroadcast_url",requestURL);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(bundle!=null && bundle.getInt(SharedPreferenceService.RESULT)==RESULT_OK) {
            Log.i("IntentReceived", currentIntent.toString());
            SharedPreferences listData = context.getSharedPreferences("list", 0);
            String resultString = listData.getString(currentIntent, "0 results");
            String[] splitItems = resultString.split(",");
            if (splitItems.length > 1) {
                switch (currentIntent) {
                    case ADD_LIST_STRING:
                        try {
                            listRowBuilder.addList(splitItems[1], Integer.parseInt(splitItems[2]), 4, splitItems[3], splitItems[4]);
                        } catch (NumberFormatException e) {
                            Log.i("ListBroadcastCase0Error", e.toString());
                        }
                        break;
                    case ALL_LIST_STRING:
                        for (int i = 0; i < splitItems.length; i += 5) {
                            try {
                                listRowBuilder.addList(splitItems[i],
                                        Integer.parseInt(splitItems[i + 1]),
                                        Integer.parseInt(splitItems[i + 2]),
                                        splitItems[i + 3],
                                        splitItems[i + 4]);
                            } catch (NumberFormatException e) {
                                Log.i("ListBroadcastCase0Error", e.toString());
                            }
                        }
                        break;
                }
            }
        }
    }
}

