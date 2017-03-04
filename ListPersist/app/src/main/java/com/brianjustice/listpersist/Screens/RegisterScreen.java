package com.brianjustice.listpersist.Screens;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brianjustice.listpersist.R;
import com.brianjustice.listpersist.broadcastreceivertools.UserBroadcastReceiver;
import com.brianjustice.listpersist.datatools.SharedPreferenceService;

public class RegisterScreen extends AppCompatActivity {

    private UserBroadcastReceiver receiver;

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
        setContentView(R.layout.activity_register_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        View topView = findViewById(R.id.registerTopLevel);

        final Context context = this;

        final EditText usernameEdit = (EditText) findViewById(R.id.registerUserUsernameInput);
        final EditText passEdit = (EditText) findViewById(R.id.registerUserPasswordInput);
        final EditText emailEdit = (EditText) findViewById(R.id.registerUserEmailInput);

        receiver = new UserBroadcastReceiver();
        Log.i("GetListintentCreated","On Screen");


        Button registerButton = (Button) findViewById(R.id.registerUserRegisterButton);
        registerButton.setTextColor(Color.WHITE);
        registerButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(usernameEdit.getText().toString().length() < 3) {
                    Toast.makeText(context,"Username must be at least 3 characters.",Toast.LENGTH_SHORT).show();
                }
                else if(passEdit.getText().toString().length() < 3) {
                    Toast.makeText(context,"Password must be at least 3 characters.",Toast.LENGTH_SHORT).show();
                }
                else{
                    startService(receiver.createAddNewUser(context, usernameEdit.getText().toString(), passEdit.getText().toString(), emailEdit.getText().toString()));
                }
            }
        });

        Button cancelButton = (Button) findViewById(R.id.registerUserBackButton);
        cancelButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        cancelButton.setTextColor(Color.WHITE);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context,EnterScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

}
