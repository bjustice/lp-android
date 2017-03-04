package com.brianjustice.listpersist.uitools.list;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by Brian on 2/1/2017.
 */

public class ListFolder {
    private int folderID;
    private ArrayList<View> subViews;

    public ListFolder(int fID){
        folderID = fID;
        subViews = new ArrayList<View>();
    }

    public void addSubView(View view){
        subViews.add(view);
    }

    public int getFolderID(){return folderID;}
    public ArrayList<View> getSubViews(){return subViews;}
}
