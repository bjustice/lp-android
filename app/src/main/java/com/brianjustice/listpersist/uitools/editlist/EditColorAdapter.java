package com.brianjustice.listpersist.uitools.editlist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.brianjustice.listpersist.R;

import java.util.List;

/**
 * Created by Brian on 2/8/2017.
 */

public class EditColorAdapter extends ArrayAdapter<String> {

    Context context;
    public final List<String> values;

    public EditColorAdapter(Context context, List<String> values) {
        super(context, R.layout.colorspinner_layout, values);

        this.context = context;
        this.values = values;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = super.getView(position, convertView, parent);
        ColorValue colorValue = new ColorValue();
        int lineColor = colorValue.getColorInt(values.get(position),context);
        Log.i("CurrentVal",values.get(position));
        rowView.setBackgroundColor(lineColor);
        return rowView;
    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView spinnerItem = (TextView) inflater.inflate(R.layout.colorspinner_layout, null);
        ColorValue colorValue = new ColorValue();
        int lineColor = colorValue.getColorInt(values.get(position),context);
        spinnerItem.setBackgroundColor(lineColor);
        spinnerItem.setText(values.get(position));

        return spinnerItem;
    }
}
