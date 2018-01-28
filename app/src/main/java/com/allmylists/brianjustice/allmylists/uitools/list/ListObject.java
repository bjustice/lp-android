package com.allmylists.brianjustice.allmylists.uitools.list;

/**
 * Created by Brian on 10/8/2016.
 */

public class ListObject {

    private String listName;
    private int listID;
    private int listColor;
    private String updateDate;
    private int folderID;//-1 for no folder
    private String inputDate;



    public ListObject(String lN, int lID, int color, String upDate, String inDate, int fID) {
        listName = lN;
        listID = lID;
        listColor = color;
        updateDate = upDate;
        inputDate = inDate;
        folderID = fID;
    }

    public int getListID(){return listID;}
    public String getInputDate() {return inputDate;}
    public String getUpdateDate() {return updateDate;}
    public int getFolderID() {return folderID;}


    public int getListColor(){return listColor;}
    public void setListColor(int lc){listColor=lc;}

    public String getListName(){return listName;}
    public void setListName(String ln){listName=ln;}

}

