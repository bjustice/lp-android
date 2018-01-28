package com.allmylists.brianjustice.allmylists.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.allmylists.brianjustice.allmylists.R;
import com.allmylists.brianjustice.allmylists.customviews.DarkButton;
import com.allmylists.brianjustice.allmylists.uitools.editlist.EditColorAdapter;

import java.util.ArrayList;

public class EditItemScreen extends AppCompatActivity {

    public static final int NAME_CHANGE = 1;
    public static final int COLOR_CHANGE = 2;
    public static final int DELETE_ITEM = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View topView = findViewById(R.id.content_edit_list_screen);

        final EditText nameInput = (EditText) findViewById(R.id.editItemNameInput);
        DarkButton changeNameButton = (DarkButton) findViewById(R.id.editItemChangeNameButton);
        DarkButton deleteItemButton = (DarkButton) findViewById(R.id.editItemDeleteButton);
        DarkButton changeColorButton = (DarkButton) findViewById(R.id.changeColorButton);
        final Spinner colorSpinner = (Spinner) findViewById(R.id.editColorSpinner);
        TextView inputDateDisplay = (TextView) findViewById(R.id.inputDateDisplay);
        TextView updateDateDisplay = (TextView) findViewById(R.id.updateDateDisplay);

        ArrayList<String> colors = new ArrayList<String>();
        colors.add("Orange");
        colors.add("Purple");
        colors.add("Gray");
        colors.add("Green");
        colors.add("Blue");
        colors.add("Yellow");

        EditColorAdapter spinnerArrayAdapter = new EditColorAdapter(this,colors);
        colorSpinner.setAdapter(spinnerArrayAdapter);

        Intent passedIntent = getIntent();
        final String itemName = passedIntent.getExtras().getString("itemname");
        final int itemId = passedIntent.getExtras().getInt("itemid");
        final String updateDate = passedIntent.getExtras().getString("updatedate");
        final String inputDate = passedIntent.getExtras().getString("inputdate");

        nameInput.setText(itemName);
        inputDateDisplay.setText(inputDate);
        updateDateDisplay.setText(updateDate);


        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("newitemname",nameInput.getText().toString());
                intent.putExtra("itemid",itemId);

                setResult(NAME_CHANGE,intent);
                finish();
            }
        });

        changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("itemcolor",colorSpinner.getSelectedItem().toString());
                intent.putExtra("itemid",itemId);
                setResult(COLOR_CHANGE,intent);
                finish();
            }
        });

        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("itemid",itemId);
                setResult(DELETE_ITEM,intent);
                finish();
            }
        });
    }
}
