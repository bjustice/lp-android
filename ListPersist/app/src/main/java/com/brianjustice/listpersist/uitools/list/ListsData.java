package com.brianjustice.listpersist.uitools.list;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Brian on 10/8/2016.
 */

public class ListsData {

    private List<ListObject> allLists;
    private int nextSort;

    private static int ALPHABETICAL_SORT = 1;
    private static int REVERSE_ALPHABETICAL = 2;

    public ListsData(){
        nextSort = ALPHABETICAL_SORT;
        allLists = new ArrayList<>();
    }

    public List<String> getListNames(){
        List<String> allNames = new ArrayList<>();

        for(ListObject currentObj : allLists){
            allNames.add(currentObj.getListName());
        }

        return allNames;
    }

    public void sortByName(){
        if(nextSort == ALPHABETICAL_SORT) {
            Collections.sort(allLists, new Comparator<ListObject>() {
                @Override
                public int compare(ListObject listObject, ListObject t1) {
                    return listObject.getListName().compareToIgnoreCase(t1.getListName());
                }
            });
            nextSort=REVERSE_ALPHABETICAL;
        }
        else if(nextSort == REVERSE_ALPHABETICAL){
            Collections.sort(allLists, new Comparator<ListObject>() {
                @Override
                public int compare(ListObject listObject, ListObject t1) {
                    return (listObject.getListName().compareToIgnoreCase(t1.getListName()));
                }
            });
            Collections.reverse(allLists);
            nextSort=ALPHABETICAL_SORT;
        }
    }

    public void changeOneListColor(int listId,int color){
        for(ListObject lo : allLists){
            if(lo.getListID()==listId){
                lo.setListColor(color);
            }
        }
    }

    public void changeOneListName(int listID, String name){
        for(ListObject lo: allLists){
            if(lo.getListID()==listID){
                lo.setListName(name);
            }
        }
    }

    public void sortByColor(){
        //next sort is always alphabetical except when previous sort was alphabetical
        nextSort = ALPHABETICAL_SORT;
        Collections.sort(allLists, new Comparator<ListObject>() {
            @Override
            public int compare(ListObject listObject, ListObject t1) {

                return String.valueOf(listObject.getListColor()).compareTo(String.valueOf(t1.getListColor()));
            }
        });
    }

    public void sortByInputDate(){
        //next sort is always alphabetical except when previous sort was alphabetical
        nextSort = ALPHABETICAL_SORT;
        Collections.sort(allLists, new Comparator<ListObject>() {
            @Override
            public int compare(ListObject listObject, ListObject t1) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date1 = simpleDateFormat.parse(listObject.getInputDate());
                    Date date2 = simpleDateFormat.parse(t1.getInputDate());
                    return date1.compareTo(date2);
                }
                catch(Exception e){
                    Log.i("DateFailedListsData",e.toString());
                }
                return 0;
            }
        });
        Collections.reverse(allLists);
    }

    public void sortByUpdateDate(){
        //next sort is always alphabetical except when previous sort was alphabetical
        nextSort = ALPHABETICAL_SORT;
        Collections.sort(allLists, new Comparator<ListObject>() {
            @Override
            public int compare(ListObject listObject, ListObject t1) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date1 = simpleDateFormat.parse(listObject.getUpdateDate());
                    Date date2 = simpleDateFormat.parse(t1.getUpdateDate());
                    return date1.compareTo(date2);
                }
                catch(Exception e){
                    Log.i("DateFailedListsData",e.toString());
                }
                return 0;
            }
        });
        Collections.reverse(allLists);

    }


    public List<ListObject> getAllLists(){return allLists;}

    public void addList(String s, int id, int color, String upDate, String inDate){
        allLists.add(new ListObject(s,id,color,upDate,inDate,-1));
    }

    public String[] toPassableStringArray(){
        String[] passableArray = new String[allLists.size()*2];
        int counter = 0;
        for(ListObject lo : allLists){
            passableArray[counter] = lo.getListName();
            passableArray[counter+1] = String.valueOf(lo.getListID());
            counter += 2;
        }
        return passableArray;
    }

}


