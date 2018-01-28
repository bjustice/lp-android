package com.allmylists.brianjustice.allmylists.uitools.listitem;

/**
 * Created by Brian on 10/9/2016.
 */

public class ItemObject {
    private String itemName;
    private int itemID;
    private Boolean checked;
    private int color;
    private String inputDate;
    private String updateDate;

    public void setItemName(String itemName){this.itemName=itemName;}
    public String getUpdateDate() {return updateDate;}
    public void setUpdateDate(String updateDate) {this.updateDate = updateDate;}
    public String getInputDate() {return inputDate;}
    public void setInputDate(String inputDate) {this.inputDate = inputDate;}
    public String getItemName(){return itemName;}
    public Boolean returnTrueIfChecked(){return checked;}
    public int getItemID(){return itemID;}
    public void setChecked(){checked=true;}
    public void setUnChecked(){checked=false;}
    public int getColor() { return color;}
    public void setColor(int color) {this.color = color;}

    public ItemObject(String lN, int lID, Boolean check, int col) {
        this.itemName = lN;
        this.itemID = lID;
        this.checked = check;
        this.color = col;
        this.inputDate="00";
        this.updateDate="00";
    }


}

