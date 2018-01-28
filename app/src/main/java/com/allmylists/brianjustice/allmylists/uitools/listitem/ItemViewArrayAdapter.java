package com.allmylists.brianjustice.allmylists.uitools.listitem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allmylists.brianjustice.allmylists.R;
import com.allmylists.brianjustice.allmylists.screens.EditItemScreen;
import com.allmylists.brianjustice.allmylists.screens.ListItemsScreen;

import java.util.List;

/**
 * Created by Brian on 1/16/2017.
 */

public class ItemViewArrayAdapter extends ArrayAdapter<String> {

    Context context;
    public final List<String> values;
    private ItemsData listItemDataAccess;
    private ItemRowBuilder itemRowBuilder;
    private ListItemsScreen listItemsScreen;
    private int listID;

    public ItemViewArrayAdapter(Context context, List<String> values, ItemsData listItemData, ItemRowBuilder lirb, int listID, ListItemsScreen listItemsScreen) {
        super(context, R.layout.item_row, values);

        this.context = context;
        this.values = values;
        this.listItemDataAccess = listItemData;
        this.itemRowBuilder = lirb;
        this.listItemsScreen = listItemsScreen;
        this.listID = listID;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ItemObject currentListItem = listItemDataAccess.getAllItems().get(position);
        final LinearLayout rowView;

        rowView = (LinearLayout) inflater.inflate(R.layout.item_row, parent, false);
        rowView.setBackgroundColor((int) (currentListItem.getColor()));
        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
                public boolean onLongClick(View view) {
                Intent intent = new Intent(context, EditItemScreen.class);
                intent.putExtra("itemname",currentListItem.getItemName());
                intent.putExtra("itemid",currentListItem.getItemID());
                intent.putExtra("updatedate",currentListItem.getUpdateDate());
                intent.putExtra("inputdate",currentListItem.getInputDate());
                ((Activity)context).startActivityForResult(intent,2);
                return true;//return true to indicate that this call consumed the long click
            }
        });

        final CheckBox listItemCheck = (CheckBox) rowView.findViewById(R.id.listItemCheck);
        final TextView listItemName =(TextView) rowView.findViewById(R.id.listItemName);
        final TextView editItemText = (TextView) rowView.findViewById(R.id.editListItem);

        if(currentListItem.returnTrueIfChecked()) {
            listItemCheck.setChecked(true);
        }

        listItemCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                itemRowBuilder.setChecked(currentListItem);
                context.startService(listItemsScreen.receiver.createSetCheckedItemIntent(context,listID,currentListItem.getItemID()));
            }
        });


        final String currentListItemName = currentListItem.getItemName();
        listItemName.setText(currentListItemName);


        editItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditItemScreen.class);
                intent.putExtra("itemname",currentListItem.getItemName());
                intent.putExtra("itemid",currentListItem.getItemID());
                intent.putExtra("updatedate",currentListItem.getUpdateDate());
                intent.putExtra("inputdate",currentListItem.getInputDate());
                ((Activity)context).startActivityForResult(intent,2);

//                itemRowBuilder.deleteListItem(currentListItem);
//                context.startService(listItemsScreen.receiver.createDeleteItemIntent(context,currentListItem.getItemID()));
            }
        });
        return rowView;
    }
}
