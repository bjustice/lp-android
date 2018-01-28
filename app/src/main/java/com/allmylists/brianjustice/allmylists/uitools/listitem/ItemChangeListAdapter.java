package com.allmylists.brianjustice.allmylists.uitools.listitem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.allmylists.brianjustice.allmylists.R;
import com.allmylists.brianjustice.allmylists.Screens.ListItemsScreen;
import com.allmylists.brianjustice.allmylists.uitools.list.ListsData;

import java.util.List;

/**
 * Created by Brian on 3/29/2017.
 */

public class ItemChangeListAdapter extends ArrayAdapter<String> {
    Context context;
    public final List<String> values;
    private ListsData listObjects;

    public ItemChangeListAdapter(Context context, List<String> values, ListsData lo) {
        super(context, R.layout.colorspinner_layout, values);

        this.context = context;
        this.values = values;
        this.listObjects = lo;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = super.getView(position, convertView, parent);
        Log.i("CurrentVal",listObjects.getAllLists().get(position).getListName());
        return rowView;
    }

    @Override
    public View getDropDownView(final int position, View cnvtView, ViewGroup prnt) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final TextView spinnerItem = (TextView) inflater.inflate(R.layout.colorspinner_layout, null);
        spinnerItem.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        spinnerItem.setText(values.get(position));
        spinnerItem.setTextColor(Color.WHITE);
        spinnerItem.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Intent changeListIntent = new Intent(context, ListItemsScreen.class);
                changeListIntent.putExtra("listID",listObjects.getAllLists().get(position).getListID());
                changeListIntent.putExtra("listsdata",listObjects.toPassableStringArray());
                context.startActivity(changeListIntent);
                return false;
            }
        });
        spinnerItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent changeListIntent = new Intent(context, ListItemsScreen.class);
                changeListIntent.putExtra("listID",listObjects.getAllLists().get(position).getListID());
                changeListIntent.putExtra("listsdata",listObjects.toPassableStringArray());
                context.startActivity(changeListIntent);
                return false;
            }
        });

        return spinnerItem;
    }
}
