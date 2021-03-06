package com.allmylists.brianjustice.allmylists.Screens;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.allmylists.brianjustice.allmylists.R;
import com.allmylists.brianjustice.allmylists.broadcastreceivertools.ItemBroadcastReceiver;
import com.allmylists.brianjustice.allmylists.datatools.SharedPreferenceService;
import com.allmylists.brianjustice.allmylists.uitools.editlist.ColorValue;
import com.allmylists.brianjustice.allmylists.uitools.list.ListsData;
import com.allmylists.brianjustice.allmylists.uitools.listitem.ItemChangeListAdapter;
import com.allmylists.brianjustice.allmylists.uitools.listitem.ItemsData;
import com.allmylists.brianjustice.allmylists.uitools.listitem.ItemRowBuilder;

public class ListItemsScreen extends ListActivity{

    private ItemRowBuilder itemRowBuilder;
    private int listID;

    private final Context context = this;
    public ItemBroadcastReceiver receiver;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                SharedPreferenceService.NOTIFICATION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        attemptUnregisterReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        attemptUnregisterReceiver();
    }

    private void attemptUnregisterReceiver(){
        try {
            unregisterReceiver(receiver);
        }
        catch(Exception e){
            //do nothing, necessary because cannot check if already unregistered.
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listitems);
//        new FilePermissions().verifyStoragePermissions(this);

        //not using progress bar in current build

        Intent passedIntent = getIntent();
        listID = passedIntent.getExtras().getInt("listID");

        //get lists data
        String[] allListsData = passedIntent.getExtras().getStringArray("listsdata");
        initializeTopSpinner(allListsData);

        //find Active item layout and Checked Items layout
        final ListView listView = getListView();
        ItemsData itemDataAccess = new ItemsData();
        itemRowBuilder = new ItemRowBuilder(context,listView, itemDataAccess,listID,this);

        //initialize broadcast receiver for accessing online list items
        receiver = new ItemBroadcastReceiver(itemRowBuilder);
        startService(receiver.createGetItemsIntent(context,listID));

        initializeLogoutButton();
        initializeAddButton();
        initializeSortButton();
    }

    public void initializeSortButton(){
        ImageButton sortButton = (ImageButton) findViewById(R.id.sortPopupButton);

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                final PopupWindow popupWindow;
                View customView = inflater.inflate(R.layout.list_sort_popup,null);

                // Initialize a new instance of popup window
                popupWindow = new PopupWindow(
                        customView,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    popupWindow.setElevation(5.0f);
                }

                int[] location = new int[2];
                view.getLocationOnScreen(location);

                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY,location[0]-(2*view.getWidth()),location[1]+view.getHeight());

                TextView sortOptions = (TextView) customView.findViewById(R.id.sortOptionsTextView);
                final Button sortAlphabetically = (Button) customView.findViewById(R.id.sortAlphabeticallyButton);
                final Button sortByColor = (Button) customView.findViewById(R.id.sortByColorButton);
                Button sortByInput = (Button) customView.findViewById(R.id.sortByInputDateButton);
                Button sortByLastUpdate = (Button) customView.findViewById(R.id.sortRecentlyUpdatedButton);
                sortOptions.setTextColor(Color.WHITE);
                sortAlphabetically.setTextColor(Color.WHITE);
                sortByColor.setTextColor(Color.WHITE);
                sortByInput.setTextColor(Color.WHITE);
                sortByLastUpdate.setTextColor(Color.WHITE);

                sortAlphabetically.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        popupWindow.dismiss();
                        itemRowBuilder.sortAdapter();
                    }
                });
                sortByColor.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        popupWindow.dismiss();
                        itemRowBuilder.sortByColor();
                    }
                });
            }
        });
    }

    private void initializeTopSpinner(final String[] listData){
        String[] listSelectorStrings = new String[listData.length/2];
        String defaultListName = "";
        final ListsData allLists = new ListsData();

        for(int i=0; i < listData.length; i+=2){
            listSelectorStrings[i/2]=listData[i];
            allLists.addList(listData[i],Integer.parseInt(listData[i+1]),2,"0","0");
            if(Integer.parseInt(listData[i+1])==listID){
                defaultListName=listData[i];
            }
        }

        final Spinner listSelectorSpinner = (Spinner) findViewById(R.id.list_selector_dropdown);

        ItemChangeListAdapter listSelectAdapter = new ItemChangeListAdapter(context,allLists.getListNames(),allLists);
        listSelectorSpinner.setAdapter(listSelectAdapter);

        int defaultPosition = listSelectAdapter.getPosition(defaultListName);
        listSelectorSpinner.setSelection(defaultPosition);

    }

    public void initializeLogoutButton(){
        final Button logoutButton = (Button) findViewById(R.id.log_out);
        logoutButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences userData = ((Activity)context).getBaseContext().getSharedPreferences("userdata",0);
                SharedPreferences.Editor spEditor = userData.edit();
                spEditor.putString("userdata","");
                spEditor.commit();

                Intent intent = new Intent(context,EnterScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void initializeAddButton(){
        final EditText addItemEditText = (EditText) findViewById(R.id.addItemEditText);
        addItemEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                Log.i("Handle","ListsAdd");
                if(i == EditorInfo.IME_ACTION_DONE){
                    String newListValue = addItemEditText.getText().toString();
                    addItemEditText.setText("");
                    if(newListValue.length() > 0){
                        startService(receiver.createAddItemIntent(context,newListValue,listID));
                    }
                    handled = true;
                }
                return handled;
            }
        });

        final Button addItemButton = (Button) findViewById(R.id.addItemButton);
        addItemButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        addItemButton.setTextColor(Color.WHITE);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newListValue = addItemEditText.getText().toString();
                addItemEditText.setText("");
                if(newListValue.length() > 0){
                    startService(receiver.createAddItemIntent(context,newListValue,listID));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == 2){
            Log.i("ListScreenResult",resultCode+"");
            if(resultCode == EditItemScreen.COLOR_CHANGE) {
                int activityItemId = intent.getIntExtra("itemid", -1);
                String passedColor = intent.getStringExtra("itemcolor");
                ColorValue colorValue = new ColorValue();
                int itemColor = colorValue.getColorInt(passedColor,context);

                Log.i("ActivityListID ", activityItemId + "");
                itemRowBuilder.changeItemColor(activityItemId, itemColor);
                startService(receiver.createChangeItemColorIntent(context,activityItemId,itemColor));
                Log.i("EditActivityResult", "ReqCode " + requestCode + " ResCode" + resultCode + " Color " + itemColor);

            }
            else if(resultCode == EditItemScreen.NAME_CHANGE){
                String newItemName = intent.getStringExtra("newitemname");
                int activityItemId = intent.getIntExtra("itemid", -1);
                itemRowBuilder.changeItemName(activityItemId,newItemName);
                startService(receiver.createChangeItemNameIntent(context,activityItemId,newItemName));

                Log.i("NewListName",newItemName+" "+activityItemId);
            }
            else if(resultCode == EditItemScreen.DELETE_ITEM){
                //startService(receiver.createDeleteListIntent(context,intent.getIntExtra("listid",-1)));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
