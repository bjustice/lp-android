package com.brianjustice.listpersist;

/**
 * Created by Brian on 1/21/2017.
 */

public enum EditReturnValues {
    COLOR_CHANGE(1),
    NAME_CHANGE(2),
    DELETE_LIST(3);


    private int value;
    EditReturnValues(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
