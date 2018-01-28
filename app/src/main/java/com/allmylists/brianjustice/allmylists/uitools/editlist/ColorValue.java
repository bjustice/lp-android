package com.allmylists.brianjustice.allmylists.uitools.editlist;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;

import com.allmylists.brianjustice.allmylists.R;

/**
 * Created by Brian on 2/9/2017.
 */

public class ColorValue {
    public int getColorInt(String color, Context context){
        int listColor = 0;
        switch(color){
            case "Orange":
                listColor = ResourcesCompat.getColor(context.getResources(), R.color.list_orange,null);
                break;
            case "Purple":
                listColor = ResourcesCompat.getColor(context.getResources(),R.color.list_purple,null);
                break;
            case "Gray":
                listColor = ResourcesCompat.getColor(context.getResources(),R.color.list_gray,null);
                break;
            case "Green":
                listColor = ResourcesCompat.getColor(context.getResources(),R.color.list_green,null);
                break;
            case "Blue":
                listColor = ResourcesCompat.getColor(context.getResources(),R.color.list_blue,null);
                break;
            case "Yellow":
                listColor = ResourcesCompat.getColor(context.getResources(),R.color.list_yellow,null);
                break;
        }
        return listColor;
    }
}
