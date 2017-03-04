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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class EnterScreen extends AppCompatActivity {

    private UserBroadcastReceiver userBroadcastReceiver;
    private GoogleApiClient mGoogleApiClient;
    private Context context;
    private final int RC_SIGN_IN = 100;
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(userBroadcastReceiver, new IntentFilter(
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

    private void attemptUnregsiterReceiver() {
        try {
            unregisterReceiver(userBroadcastReceiver);
        } catch (Exception e) {
            //do nothing, necessary because cannot check if already unregistered.
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final View topLevelView = findViewById(R.id.content_enter_screen);
        topLevelView.setBackgroundColor(Color.WHITE);

        userBroadcastReceiver = new UserBroadcastReceiver();
        context = this;

        final Context context = this;
        final EditText user = (EditText) findViewById(R.id.userLoginName);
        final EditText pass = (EditText) findViewById(R.id.userPassword);

        initializeGoogleSignIn();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        Button login = (Button) findViewById(R.id.attempt_login_button);
        login.setTextColor(Color.WHITE);
        login.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startService(userBroadcastReceiver.createVerifyUser(context,user.getText().toString(),pass.getText().toString(),"login"));
            }
        });

        Button register = (Button) findViewById(R.id.attempt_register_button);
        register.setTextColor(Color.WHITE);
        register.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context,RegisterScreen.class);
                startActivity(intent);
            }
        });
    }

    private void initializeGoogleSignIn(){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Signin Handled", "handleSignInResult:" + result.isSuccess());
        Toast.makeText(context,"Signin Handled handleSignInResult:" + result.isSuccess(),Toast.LENGTH_LONG).show();
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }


}
