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

    private ListRowBuilder listRowBuilder;
    private ListBroadcastType currentIntent;

    public ListBroadcastReceiver(ListRowBuilder lrb){
        listRowBuilder = lrb;
    }

    public Intent createAddListIntent(Context context, String listName, String ownerID, String listColor){
        currentIntent = ListBroadcastType.ADD_LIST;
        //initialize broadcast receiver for accessing online list items
        Intent intent = new Intent(context, SharedPreferenceService.class);

        // add infos for the service which file to download and where to store
        String addListURL = "http://www.listpersist.com/datawork/addlist.php?ownerID="+ownerID+"&listname="+listName+"&listcolor="+listColor;
        intent.putExtra(SharedPreferenceService.URL,
                addListURL);
        intent.putExtra(SharedPreferenceService.PREFERENCESTRING,"addliststring");
        Log.i("AddListURL",addListURL);
        return intent;
    }

    public Intent createGetListIntent(Context context, String ownerID){
        currentIntent = ListBroadcastType.GET_OWNED_LISTS;
        //initialize broadcast receiver for accessing online list items
        Intent intent = new Intent(context, SharedPreferenceService.class);

        // add infos for the service which file to download and where to store
        intent.putExtra(SharedPreferenceService.URL,
                "http://www.listpersist.com/datawork/getownedlists.php?ownerID="+ownerID);
        intent.putExtra(SharedPreferenceService.PREFERENCESTRING,"allliststring");
        Log.i("GetListURL","http://www.listpersist.com/datawork/getownedlists.php?ownerID="+ownerID);
        return intent;
    }

    public Intent createDeleteListIntent(Context context, int listID){
        currentIntent = ListBroadcastType.DELETE_LIST;
        //initialize broadcast receiver for accessing online list items
        Intent intent = new Intent(context, SharedPreferenceService.class);

        // add infos for the service which file to download and where to store
        intent.putExtra(SharedPreferenceService.URL,
                "http://www.listpersist.com/datawork/deletelist.php?listid="+listID);
        intent.putExtra(SharedPreferenceService.PREFERENCESTRING,"deleteliststring");
        Log.i("DeleteListURL","http://www.listpersist.com/datawork/deletelistitem.php?listid="+listID);
        return intent;
    }

    public Intent createChangeColorIntent(Context context, int listID, int listColor){
        currentIntent = ListBroadcastType.CHANGE_COLOR;
        //initialize broadcast receiver for accessing online list items
        Intent intent = new Intent(context, SharedPreferenceService.class);

        // add infos for the service which file to download and where to store
        String changeColorURL = "http://www.listpersist.com/datawork/changelist.php?changetype=COLOR&listid="+listID+"&newlistcolor="+listColor;

        intent.putExtra(SharedPreferenceService.URL,
                changeColorURL);
        intent.putExtra(SharedPreferenceService.PREFERENCESTRING,"changelistcolorstring");
        Log.i("ChangeColorURL",changeColorURL);
        return intent;
    }

    public Intent createChangeListNameIntent(Context context, int listID, String listName){
        currentIntent = ListBroadcastType.CHANGE_NAME;
        //initialize broadcast receiver for accessing online list items
        Intent intent = new Intent(context, SharedPreferenceService.class);

        // add infos for the service which file to download and where to store
        String changeNameURL = "http://www.listpersist.com/datawork/changelist.php?changetype=NAME&listid="+listID+"&newlistname="+listName;

        intent.putExtra(SharedPreferenceService.URL,
                changeNameURL);
        intent.putExtra(SharedPreferenceService.PREFERENCESTRING,"changelistnamestring");
        Log.i("ChangeNameURL",changeNameURL);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(bundle!=null && bundle.getInt(SharedPreferenceService.RESULT)==RESULT_OK) {
            Log.i("IntentReceived",currentIntent.toString());
            SharedPreferences listData = context.getSharedPreferences("list", 0);
            switch (currentIntent) {
                case ADD_LIST:
                    String addListItem = listData.getString("addliststring", "0 results");
                    String[] splitItems = addListItem.split(",");
                    if (splitItems.length > 1) {
                        try {
                            listRowBuilder.addList(splitItems[1], Integer.parseInt(splitItems[2]), 4, splitItems[3], splitItems[4]);
                        } catch (NumberFormatException e) {
                            Log.i("ListBroadcastCase0Error", e.toString());
                        }
                    }
                    break;
                case GET_OWNED_LISTS:
                    String allListItems = listData.getString("allliststring", "0 results");
                    Log.i("ListItemString", allListItems);
                    if (!allListItems.equals("0 results")) {
                        String[] splitListItems = allListItems.split(",");
                        for (int i = 0; i < splitListItems.length; i += 5) {
                            try {
                                listRowBuilder.addList(splitListItems[i],
                                        Integer.parseInt(splitListItems[i + 1]),
                                        Integer.parseInt(splitListItems[i + 2]),
                                        splitListItems[i + 3],
                                        splitListItems[i + 4]);
                            } catch (NumberFormatException e) {
                                Log.i("ListBroadcastCase0Error", e.toString());
                                Toast.makeText(context, "Invalid List data found", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    break;
            }
        }
    }
    enum ListBroadcastType{
        ADD_LIST,
        GET_OWNED_LISTS,
        DELETE_LIST,
        CHANGE_COLOR,
        CHANGE_NAME;
    }
}

