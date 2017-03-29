package com.brianjustice.listpersist.uitools.listitem;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.brianjustice.listpersist.R;
import com.brianjustice.listpersist.uitools.editlist.ColorValue;
import com.brianjustice.listpersist.uitools.list.ListObject;
import com.brianjustice.listpersist.uitools.list.ListsData;

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
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TextView spinnerItem = (TextView) inflater.inflate(R.layout.colorspinner_layout, null);
        spinnerItem.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        spinnerItem.setText(values.get(position));

        return spinnerItem;
    }
}
