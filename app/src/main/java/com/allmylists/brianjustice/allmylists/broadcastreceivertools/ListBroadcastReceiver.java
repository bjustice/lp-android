package com.allmylists.brianjustice.allmylists.broadcastreceivertools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.allmylists.brianjustice.allmylists.datatools.SharedPreferenceService;
import com.allmylists.brianjustice.allmylists.uitools.list.ListRowBuilder;

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
        String[] actionParameters = new String[3];
        actionParameters[0] = "ownerid,"+ownerID;
        actionParameters[1] = "listname,"+listName;
        actionParameters[2] = "listcolor,"+listColor;
        return initializeIntent(context,ADD_LIST_STRING,SharedPreferenceService.LIST_ADD,actionParameters);
    }

    public Intent createGetListIntent(Context context, String ownerID){
        currentIntent = ALL_LIST_STRING;
        String[] actionParameters = new String[1];
        actionParameters[0] = "ownerid,"+ownerID;
        return initializeIntent(context,ALL_LIST_STRING,SharedPreferenceService.LIST_ALL_OWNED,actionParameters);
    }

    public Intent createDeleteListIntent(Context context, int listID){
        currentIntent = DELETE_LIST_STRING;
        String[] actionParameters = new String[1];
        actionParameters[0] = "listid,"+listID;
        return initializeIntent(context,DELETE_LIST_STRING,SharedPreferenceService.LIST_DELETE,actionParameters);
    }

    public Intent createChangeColorIntent(Context context, int listID, int listColor){
        currentIntent = CHANGE_LIST_COLOR_STRING;
        String[] actionParameters = new String[2];
        actionParameters[0] = "listid,"+listID;
        actionParameters[1] = "listcolor,"+listColor;
        return initializeIntent(context,CHANGE_LIST_COLOR_STRING,SharedPreferenceService.LIST_CHANGE_COLOR,actionParameters);
    }

    public Intent createChangeListNameIntent(Context context, int listID, String listName){
        currentIntent = CHANGE_LIST_NAME_STRING;
        String[] actionParameters = new String[2];
        actionParameters[0] = "listid,"+listID;
        actionParameters[1] = "listname,"+listName;
        return initializeIntent(context,CHANGE_LIST_NAME_STRING,SharedPreferenceService.LIST_CHANGE_NAME,actionParameters);
    }

    private Intent initializeIntent(Context context, String toCreateString, String action, String[] actionParameters){
        Intent intent = new Intent(context, SharedPreferenceService.class);
        intent.putExtra(SharedPreferenceService.PREFERENCESTRING, toCreateString);
        intent.putExtra(SharedPreferenceService.REQUEST_TABLE,"list");
        intent.putExtra(SharedPreferenceService.ACTION,action);
        intent.putExtra(SharedPreferenceService.ACTION_PARAMETERS, actionParameters);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(bundle == null || bundle.getInt(SharedPreferenceService.RESULT)!=RESULT_OK) {
            return;
        }
        Log.i("IntentReceived", currentIntent.toString());
        SharedPreferences listData = context.getSharedPreferences("list", 0);
        String resultString = listData.getString(currentIntent, "0 results");
        String[] splitItems = resultString.split(",");
        if (splitItems.length < 1) {
            return;
        }
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

