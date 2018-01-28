package com.allmylists.brianjustice.allmylists.uitools.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.allmylists.brianjustice.allmylists.R;
import com.allmylists.brianjustice.allmylists.screens.EditListScreen;
import com.allmylists.brianjustice.allmylists.screens.ListItemsScreen;

import java.util.List;

/**
 * Created by Brian on 1/10/2017.
 */

public class ListViewArrayAdapter extends ArrayAdapter<String> {
    Context context;
    private String userID;
    public final List<String> values;
    public ListsData listDataAccess;

    public ListViewArrayAdapter(Context context, List<String> values, ListsData listData, String uid) {
        super(context, R.layout.list_row, values);

        this.context = context;
        this.values = values;
        this.listDataAccess = listData;
        this.userID = uid;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ListObject currentList = listDataAccess.getAllLists().get(position);
        return initializeNoFolder(inflater, parent, currentList);
    }

    public View initializeNoFolder(LayoutInflater inflater, ViewGroup parent, final ListObject currentList){
        final View rowView = inflater.inflate(R.layout.list_row, parent, false);
        rowView.setBackgroundColor(currentList.getListColor());

        TextView listName =(TextView) rowView.findViewById(R.id.listName);
        TextView editText = (TextView) rowView.findViewById(R.id.editListView);
        if(listDataAccess.getAllLists().toArray().length > 0) {
            final String currentListName = currentList.getListName();

            listName.setText(currentListName);
            listName.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent startMainScroll = new Intent(context, ListItemsScreen.class);
                    startMainScroll.putExtra("listID",currentList.getListID());
                    startMainScroll.putExtra("listsdata",listDataAccess.toPassableStringArray());
                    context.startActivity(startMainScroll);
                }
            });

            editText.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent startEditListScreen = new Intent(context, EditListScreen.class);
                    startEditListScreen.putExtra("listname",currentListName);
                    startEditListScreen.putExtra("inputdate",currentList.getInputDate());
                    startEditListScreen.putExtra("updatedate",currentList.getUpdateDate());
                    startEditListScreen.putExtra("listid",currentList.getListID());
                    startEditListScreen.putExtra("userid",userID);
                    ((Activity)context).startActivityForResult(startEditListScreen,1);
                }
            });

        }
        return rowView;
    }
}
