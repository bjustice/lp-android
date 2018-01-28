package com.allmylists.brianjustice.allmylists.uitools.listitem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Brian on 9/16/2016.
 */
public class ItemsData {

    private List<ItemObject> activeItems;
    private List<ItemObject> completedItems;
    private int nextSort;

    public ItemsData(){
        activeItems = new ArrayList<ItemObject>();
        completedItems = new ArrayList<ItemObject>();
        nextSort = 0;
    }

    public List<ItemObject> getActiveItems(){return new ArrayList<>(activeItems);}
    public List<String> getActiveItemNames(){
        List<String> allActiveItems = new ArrayList<String>();
        for(ItemObject lio: activeItems){
            allActiveItems.add(lio.getItemName());
        }
        return allActiveItems;
    }

    public List<ItemObject> getCompletedItems() { return new ArrayList<>(completedItems); }
    public List<String> getCompletedItemNames(){
        List<String> allCompletedItems = new ArrayList<String>();
        for(ItemObject lio: completedItems){
            allCompletedItems.add(lio.getItemName());
        }
        return allCompletedItems;
    }

    public List<ItemObject> getAllItems(){
        List<ItemObject> allListItems = new ArrayList<>(activeItems);
        allListItems.addAll(completedItems);
        return allListItems;
    }

    public void addItem(String s, int id, int col){
        if(!activeItems.contains(s)) {
            activeItems.add(new ItemObject(s,id,false,col));
        }
    }

    public void addCompletedItem(String s, int id, int col){
        completedItems.add(new ItemObject(s,id,true,col));
    }

    public void deleteItem(ItemObject lio){
        activeItems.remove(lio);
        completedItems.remove(lio);
    }

    public void makeIncompleteItem(ItemObject lio){
        completedItems.remove(lio);
        lio.setUnChecked();
        activeItems.add(lio);
    }

    public void makeCompleteItem(ItemObject lio){
        activeItems.remove(lio);
        lio.setChecked();
        completedItems.add(lio);
    }


    public void sortByName(){
        Collections.sort(activeItems, new Comparator<ItemObject>() {
            @Override
            public int compare(ItemObject itemObject, ItemObject t1) {
                return itemObject.getItemName().compareTo(t1.getItemName());
            }
        });
        if(nextSort==0) {
            nextSort = 1;
        }
        else{
            Collections.reverse(activeItems);
            nextSort = 0;
        }
    }

    public void sortByColor(){
        Collections.sort(activeItems, new Comparator<ItemObject>() {
            @Override
            public int compare(ItemObject itemObject, ItemObject t1) {
                if(itemObject.getColor() > t1.getColor()){
                    return 1;
                }
                else if(itemObject.getColor() < t1.getColor()){
                    return -1;
                }
                else{
                    return  0;
                }
            }
        });
    }

    public void changeOneListColor(int activityListId, int itemColor) {
        for(ItemObject io : activeItems){
            if(io.getItemID()==activityListId){
                io.setColor(itemColor);
            }
        }
        for(ItemObject io : completedItems){
            if(io.getItemID()==activityListId){
                io.setColor(itemColor);
            }
        }
    }

    public void changeOneListName(int activityListId, String newListName) {
        for(ItemObject io : activeItems){
            if(io.getItemID()==activityListId){
                io.setItemName(newListName);
            }
        }
        for(ItemObject io : completedItems){
            if(io.getItemID()==activityListId){
                io.setItemName(newListName);
            }
        }
    }
}
