package com.allmylists.brianjustice.allmylists.screens;

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
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.allmylists.brianjustice.allmylists.R;
import com.allmylists.brianjustice.allmylists.uitools.editlist.ColorValue;
import com.allmylists.brianjustice.allmylists.broadcastreceivertools.ListBroadcastReceiver;
import com.allmylists.brianjustice.allmylists.datatools.SharedPreferenceService;
import com.allmylists.brianjustice.allmylists.uitools.list.ListRowBuilder;
import com.allmylists.brianjustice.allmylists.uitools.list.ListsData;

public class ListsScreen extends ListActivity {

    private final Context context = this;
    private ListBroadcastReceiver receiver;
    private ListsData userLists;
    private ListRowBuilder listRowBuilder;
    private PopupWindow sortButtonWindow;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                SharedPreferenceService.NOTIFICATION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        attemptUnregsiterReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        attemptUnregsiterReceiver();
    }

    private void attemptUnregsiterReceiver(){
        try {
            unregisterReceiver(receiver);
        }
        catch(Exception e){
            //do nothing, necessary because cannot check if already unregistered. Android bug
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_screen);

        Intent passedIntent = getIntent();
        String userID = passedIntent.getExtras().getString("userID");

        final ListView activeLayout = getListView();
        userLists = new ListsData();
        listRowBuilder = new ListRowBuilder(context,activeLayout,userLists,userID);

        TextView headerTextView = (TextView) findViewById(R.id.lists_header);
        headerTextView.setTextColor(Color.WHITE);

        initializeSortButton();
        //disabled for 1.0
//        initializeAddFolderButton();
        initializeLogoutButton();
        initializeAddButton(userID);

        //initialize broadcast receiver for accessing online lists
        receiver = new ListBroadcastReceiver(listRowBuilder);
        Log.i("GetListintentCreated","On Screen");
        startService(receiver.createGetListIntent(context,userID));

    }

    private void sortAlphabetically(){
        listRowBuilder.sortAdapterByName();
    }
    private void sortByInputDate(){listRowBuilder.sortByInputDate();}
    private void sortByUpdateDate(){listRowBuilder.sortByUpdateDate();}
    private void sortByColor(){
        listRowBuilder.sortAdapterByColor();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        int activityListId;
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == 1){
            Log.i("ListScreenResult",resultCode+"");
            switch(resultCode){
                case EditListScreen.COLOR_CHANGE:
                    activityListId = intent.getIntExtra("listid", -1);
                    String passedColor = intent.getStringExtra("listcolor");
                    ColorValue colorValue = new ColorValue();
                    int listColor = colorValue.getColorInt(passedColor,context);
                    listRowBuilder.changeListColor(activityListId, listColor);
                    Log.i("EditActivityResult", "ReqCode " + requestCode + " ResCode" + resultCode);
                    startService(receiver.createChangeColorIntent(context,activityListId,listColor));
                    break;
                case EditListScreen.NAME_CHANGE:
                    String newListName = intent.getStringExtra("newlistname");
                    activityListId = intent.getIntExtra("listid", -1);
                    listRowBuilder.changeListName(activityListId,newListName);
                    Log.i("EditActivityResult", "ReqCode " + requestCode + " ResCode" + resultCode);
                    startService(receiver.createChangeListNameIntent(context,activityListId,newListName));
                    break;
                case EditListScreen.DELETE_LIST:
                    activityListId = intent.getIntExtra("listid",-1);
                    listRowBuilder.deleteList(activityListId);
                    Log.i("EditActivityResult", "ReqCode " + requestCode + " ResCode" + resultCode);
                    startService(receiver.createDeleteListIntent(context,activityListId));
                    break;
                default:
                    break;
            }
        }
    }

    private void initializeSortButton(){
        ImageButton sortButton = (ImageButton) findViewById(R.id.sortPopupButton);

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                View customView = inflater.inflate(R.layout.list_sort_popup,null);

                // Initialize a new instance of popup window
                sortButtonWindow = new PopupWindow(
                        customView,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                // Set an elevation value for popup window
                if(Build.VERSION.SDK_INT>=21){
                    sortButtonWindow.setElevation(5.0f);
                }

                int[] location = new int[2];
                view.getLocationOnScreen(location);

                sortButtonWindow.setBackgroundDrawable(new BitmapDrawable());
                sortButtonWindow.setFocusable(true);
                sortButtonWindow.showAtLocation(view, Gravity.NO_GRAVITY,location[0]-(1*view.getWidth()),location[1]+view.getHeight());

                TextView sortOptions = (TextView) customView.findViewById(R.id.sortOptionsTextView);
                final Button sortAlphabetically = (Button) customView.findViewById(R.id.sortAlphabeticallyButton);
                final Button sortByColor = (Button) customView.findViewById(R.id.sortByColorButton);
                final Button sortByInputDate = (Button) customView.findViewById(R.id.sortByInputDateButton);
                final Button sortByUpdateDate = (Button) customView.findViewById(R.id.sortRecentlyUpdatedButton);

                sortOptions.setTextColor(Color.WHITE);
                sortAlphabetically.setTextColor(Color.WHITE);
                sortByColor.setTextColor(Color.WHITE);
                sortByInputDate.setTextColor(Color.WHITE);
                sortByUpdateDate.setTextColor(Color.WHITE);

                sortAlphabetically.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        sortAlphabetically();
                        sortButtonWindow.dismiss();
                    }
                });
                sortByColor.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        sortByColor();
                        sortButtonWindow.dismiss();
                    }
                });
                sortByInputDate.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        sortByInputDate();
                        sortButtonWindow.dismiss();
                    }
                });
                sortByUpdateDate.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        sortByUpdateDate();
                        sortButtonWindow.dismiss();
                    }
                });
            }
        });
    }

    private void initializeAddButton(final String ownerID){
        final EditText addListEditText = (EditText) findViewById(R.id.addListEditText);
        addListEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                Log.i("Handle","ListsAdd");
                if(i == EditorInfo.IME_ACTION_DONE){
                    String newListValue = addListEditText.getText().toString();
                    addListEditText.setText("");
                    if(newListValue.length() > 0){
                        startService(receiver.createAddListIntent(context,newListValue,ownerID,"1"));
                    }
                    handled = true;
                }
                return handled;
            }
        });

        final Button addListButton = (Button) findViewById(R.id.addListButton);
        addListButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        addListButton.setTextColor(Color.WHITE);

        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newListValue = addListEditText.getText().toString();
                addListEditText.setText("");
                if(newListValue.length() > 0){
                    startService(receiver.createAddListIntent(context,newListValue,ownerID,"1"));
                }
            }
        });
    }

    private void initializeLogoutButton(){
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
}
