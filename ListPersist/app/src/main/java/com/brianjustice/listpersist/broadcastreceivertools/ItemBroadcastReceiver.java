package com.brianjustice.listpersist.broadcastreceivertools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

import com.brianjustice.listpersist.datatools.SharedPreferenceService;
import com.brianjustice.listpersist.uitools.listitem.ItemRowBuilder;

/**
 * Created by Brian on 10/2/2016.
 */

public class ItemBroadcastReceiver extends BroadcastReceiver{


    private String currentIntent;
    private static final String CREATE_ADD_ITEM_STRING = "create_add_item_string";
    private static final String CREATE_GET_ITEM_STRING = "create_get_item_string";
    private static final String CREATE_DELETE_ITEM_STRING = "create_delete_item_string";
    private static final String CREATE_CHECK_ITEM_STRING = "create_check_item_string";
    private static final String CREATE_CHANGE_ITEM_COLOR_STRING = "create_change_item_color_string";
    private static final String CREATE_CHANGE_ITEM_NAME_STRING = "create_change_item_name_string";

    private ItemRowBuilder itemRowBuilder;

    public ItemBroadcastReceiver(ItemRowBuilder lrb){
        itemRowBuilder = lrb;
    }

    public Intent createAddItemIntent(Context context, String itemText, int listID){
        currentIntent = CREATE_ADD_ITEM_STRING;
        String requestURL = "http://www.listpersist.com/datawork/addlistitem.php?listID="+listID+"+&listtext="+itemText;
        return initializeIntent(context,requestURL,CREATE_ADD_ITEM_STRING);
    }

    public Intent createGetItemsIntent(Context context, int listID){
        currentIntent = CREATE_GET_ITEM_STRING;
        String requestURL = "http://www.listpersist.com/datawork/getListItems.php?masterID="+listID;
        return initializeIntent(context,requestURL,CREATE_GET_ITEM_STRING);
    }

    public Intent createDeleteItemIntent(Context context, int itemID){
        currentIntent = CREATE_DELETE_ITEM_STRING;
        String requestURL = "http://www.listpersist.com/datawork/deletelistitem.php?itemid="+itemID;
        return initializeIntent(context,requestURL,CREATE_DELETE_ITEM_STRING);
    }

    public Intent createSetCheckedItemIntent(Context context, int listID, int itemID){
        currentIntent = CREATE_CHECK_ITEM_STRING;
        String requestURL = "http://www.listpersist.com/datawork/togglecheckitem.php?listid="+listID+"&itemid="+itemID;
        return initializeIntent(context,requestURL,CREATE_CHECK_ITEM_STRING);
    }

    public Intent createChangeItemColorIntent(Context context, int itemID, int itemColor) {
        currentIntent = CREATE_CHANGE_ITEM_COLOR_STRING;
        String requestURL = "http://www.listpersist.com/datawork/changeitem.php?changetype=COLOR&itemid="+itemID+"&itemcolor="+itemColor;
        return initializeIntent(context,requestURL,CREATE_CHANGE_ITEM_COLOR_STRING);
    }

    public Intent createChangeItemNameIntent(Context context, int itemID, String newItemName) {
        currentIntent = CREATE_CHANGE_ITEM_NAME_STRING;
        String requestURL = "http://www.listpersist.com/datawork/changeitem.php?changetype=NAME&itemid="+itemID+"&itemname="+newItemName;
        return initializeIntent(context,requestURL,CREATE_CHANGE_ITEM_NAME_STRING);
    }

    private Intent initializeIntent(Context context, String requestURL, String toCreateString){
        Intent intent = new Intent(context, SharedPreferenceService.class);
        intent.putExtra(SharedPreferenceService.URL,requestURL);
        intent.putExtra(SharedPreferenceService.PREFERENCESTRING,toCreateString);
        Log.i("itembroadcast_url",requestURL);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int resultCode = bundle.getInt(SharedPreferenceService.RESULT);
            Log.i("ItemBRReceived",resultCode+"");
            if (resultCode == RESULT_OK) {
                SharedPreferences listData = context.getSharedPreferences("list", 0);
                switch (currentIntent) {
                    case CREATE_ADD_ITEM_STRING:
                        Log.i("ItemCase0", "Logged");
                        String allListItems1 = listData.getString(CREATE_ADD_ITEM_STRING, "0 results");
                        String[] splitListItems1 = allListItems1.split(",");
                        Log.i("SplitListItem1-0",splitListItems1[0]);
                        if(splitListItems1[0].equals("SUCCESS")){
                            itemRowBuilder.addListItem(splitListItems1[2],Integer.parseInt(splitListItems1[1]), Color.WHITE);
                        }
                        break;
                    case CREATE_GET_ITEM_STRING:
                        Log.i("ItemCase1", "Logged");
                        String allListItems2 = listData.getString(CREATE_GET_ITEM_STRING, "0 results");
                        String[] splitListItems = allListItems2.split(",");
                        if (splitListItems[0].equals("0 results")) {
                            //// TODO: 2/25/2017 implement no items display 
                        } else {
                            for (int i = 0; i < splitListItems.length; i += 4) {
                                if (splitListItems[i + 3].equals("0")) {
                                    itemRowBuilder.addListItem(splitListItems[i + 1], Integer.parseInt(splitListItems[i]), Integer.parseInt(splitListItems[i + 2]));
                                } else {
                                    itemRowBuilder.addCompletedListItem(splitListItems[i + 1], Integer.parseInt(splitListItems[i]), Integer.parseInt(splitListItems[i + 2]));
                                }
                            }
                        }
                        break;
                    case CREATE_DELETE_ITEM_STRING:
                        Log.i("ItemCase2", "Logged");
                        break;
                    case CREATE_CHECK_ITEM_STRING:
                        Log.i("ItemCase3", "Logged");
                        break;
                    case CREATE_CHANGE_ITEM_COLOR_STRING:
                        Log.i("ItemCase4", "Logged");
                        break;
                    case CREATE_CHANGE_ITEM_NAME_STRING:
                        Log.i("ItemCase5", "Logged");
                        break;
                }
            }
            else{
                Toast.makeText(context,"You must be connected to the internet to continue.",Toast.LENGTH_LONG).show();
            }
        }
    }


}
