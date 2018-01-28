package com.allmylists.brianjustice.allmylists.uitools.list;

import android.content.Context;
import android.widget.ListView;

import com.allmylists.brianjustice.allmylists.screens.ListsScreen;

/**
 * Created by Brian on 10/8/2016.
 */

public class ListRowBuilder extends ListsScreen{
    private final Context mainContext;
    private ListView allListsListView;
    private ListsData listDataAccess;
    private ListViewArrayAdapter listViewArrayAdapter;
    private String userID;
    private String inputDate;
    private String updateDate;

    public ListRowBuilder(final Context context, ListView aLayout, ListsData uL, String uid){
        mainContext = context;
        allListsListView = aLayout;
        listDataAccess = uL;
        userID = uid;

//        listDataAccess.addTestList();
        resetAdapter();
    }

    public void sortAdapterByName(){
        listDataAccess.sortByName();
        resetAdapter();
    }

    public void sortAdapterByColor(){
        listDataAccess.sortByColor();
        resetAdapter();
    }

    public void sortByInputDate(){
        listDataAccess.sortByInputDate();
        resetAdapter();
    }

    public void sortByUpdateDate(){
        listDataAccess.sortByUpdateDate();
        resetAdapter();
    }

    /**
     * Adds a new list and notfies view of the new list
     * @param newItem String name of the new list being added.
     * @param newItemID Unique int identifier of the new list.
     * @param listColor Color of new list.
     * @param inDate Input Date of new list.
     * @param upDate Update Date of new list.
     */
    public void addList(String newItem, int newItemID, int listColor, String inDate, String upDate){
        listDataAccess.addList(newItem,newItemID,listColor,inDate,upDate);
        listViewArrayAdapter.values.add(0,newItem);
        listViewArrayAdapter.notifyDataSetChanged();
    }

    public void changeListColor(int listID, int color){
        listDataAccess.changeOneListColor(listID, color);
        listViewArrayAdapter.notifyDataSetChanged();
    }

    public void changeListName(int listID, String name){
        listDataAccess.changeOneListName(listID, name);
        listViewArrayAdapter.notifyDataSetChanged();
    }

    public void deleteList(int listID){
        listDataAccess.deleteOneList(listID);
        listViewArrayAdapter.notifyDataSetChanged();
    }

    private void resetAdapter(){
        listViewArrayAdapter = new ListViewArrayAdapter(mainContext,listDataAccess.getListNames(),listDataAccess,userID);
        allListsListView.setAdapter(listViewArrayAdapter);
    }
}
