package com.allmylists.brianjustice.allmylists.uitools.listitem;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.ListView;

import com.allmylists.brianjustice.allmylists.screens.ListItemsScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brian on 9/19/2016.
 */
public class ItemRowBuilder {

    private final Context mainContext;
    private ListView allItemsListView;
    private ItemsData itemsData;
    private ItemViewArrayAdapter itemViewArrayAdapter;
    private ListItemsScreen listItemsScreen;
    private final int sdk = Build.VERSION.SDK_INT;
    private int listID;

    public ItemRowBuilder(final Context c, ListView aLayout, ItemsData ds, int lid, ListItemsScreen lis){
        mainContext = c;
        allItemsListView = aLayout;
        itemsData = ds;
        listID = lid;
        listItemsScreen = lis;
        resetAdapter();
    }

    public void addListItem(String newItem, int id, int col){
        itemsData.addItem(newItem,id,col);
        resetAdapter();
    }

    public void addCompletedListItem(String newItem, int id, int col){
        itemsData.addCompletedItem(newItem,id,col);
        resetAdapter();
    }

    public void setChecked(ItemObject currentListItem){

        if(itemsData.getActiveItems().contains(currentListItem)){
            List<ItemObject> activeItems = new ArrayList<>(itemsData.getActiveItems());
            ItemObject checkedItemObject = activeItems.get(activeItems.indexOf(currentListItem));
            checkedItemObject.setChecked();
            itemsData.makeCompleteItem(checkedItemObject);
        }
        else{
            List<ItemObject> checkedItems = new ArrayList<>(itemsData.getCompletedItems());
            ItemObject checkedItemObject = checkedItems.get(checkedItems.indexOf(currentListItem));
            currentListItem.setUnChecked();
            itemsData.makeIncompleteItem(checkedItemObject);
        }
        Log.i("Current ActiveCHECK", itemsData.getActiveItemNames().toString());
        Log.i("Current CompletedCHECK", itemsData.getCompletedItemNames().toString());
        resetAdapter();
    }

    public void deleteListItem(ItemObject lio) {
        itemsData.deleteItem(lio);
        resetAdapter();
    }

    public void sortAdapter(){
        itemsData.sortByName();
        resetAdapter();
    }

    public void sortByColor(){
        itemsData.sortByColor();
        resetAdapter();
    }

    private void resetAdapter(){
        List<String> currentFullList = new ArrayList<>(itemsData.getActiveItemNames());
        currentFullList.addAll(itemsData.getCompletedItemNames());
        itemViewArrayAdapter = new ItemViewArrayAdapter(mainContext,currentFullList, itemsData,this,listID,listItemsScreen);
        allItemsListView.setAdapter(itemViewArrayAdapter);
    }

    public void changeItemColor(int activityListId, int itemColor) {
        itemsData.changeOneListColor(activityListId, itemColor);
        itemViewArrayAdapter.notifyDataSetChanged();
    }

    public void changeItemName(int activityListId, String newListName) {
        itemsData.changeOneListName(activityListId, newListName);
        itemViewArrayAdapter.notifyDataSetChanged();
    }
}
