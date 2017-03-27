package com.brianjustice.listpersist.customviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

import com.brianjustice.listpersist.R;

/**
 * Created by Brian on 1/24/2017.
 */

public class DarkButton extends android.support.v7.widget.AppCompatButton {

    public DarkButton(Context context) {
        super(context);
        setFormatting();
    }

    public DarkButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFormatting();
    }

    public DarkButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFormatting();
    }

    private void setFormatting(){
        this.setTextColor(Color.WHITE);
        this.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        this.setHintTextColor(Color.WHITE);
    }

}
