package com.example.cs496_proj2.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs496_proj2.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class Login extends AppCompatActivity {
    private static Context context;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private LoginCallback mLoginCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Login.context = this;
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mLoginCallback = new LoginCallback();
        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        loginButton.registerCallback(callbackManager, mLoginCallback);

    }

/*
    public void onStart() {
        super.onStart();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                Toast mymsg = Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT);
                                mymsg.show();
                                //super.onBackPressed();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }

                            @Override
                            public void onCancel() {
                                // App code
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                            }
                        });

                //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

                // Callback registration
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //Toast msg = Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT);
                        //msg.show();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                LoginManager.getInstance().logInWithReadPermissions((Activity) getApplicationContext(), Arrays.asList("public_profile"));
            }
        });
    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Context getAppContext(){
        return Login.context;
    }
}

