package com.example.cs496_proj2.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs496_proj2.GlobalId;
import com.example.cs496_proj2.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    private static Context context;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private Button mybutton;
    public static  AccessToken accessToken;
    private LoginCallback mLoginCallback;
    ServiceAPI mMyAPI;
    String baseUrl = "http://192.249.18.209:3000";

    String str_id;
    String str_token;
    String str_appId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Login.context = this;
        setContentView(R.layout.activity_login);

        // init API
        initMyAPI(baseUrl);
        //accessToken = AccessToken.getCurrentAccessToken();

        // Login
        //mLoginCallback = new LoginCallback();
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        mybutton = (Button) findViewById(R.id.mybutton);
        mybutton.setOnClickListener(this::onClick);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Callback :: ", "onSuccess");
                requestMe(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }

            public void requestMe(AccessToken token) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(token,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d("result", object.toString());

                                str_id = token.getUserId();
                                str_token = token.getToken();
                                str_appId = token.getApplicationId();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }
        });

        /*
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn){ // Move to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("token", accessToken);
            startActivity(intent);
        }
        else{ // Need Registration
            Log.d("reg", "else isLoggedIn");
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static Context getAppContext(){
        return Login.context;
    }

    public void initMyAPI(String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mMyAPI = retrofit.create(ServiceAPI.class);
    }

    public void onClick(View view){

        switch(view.getId()){
            case R.id.mybutton:
                Call<ResponseBody> rb = mMyAPI.login(str_id, str_token, str_appId);
                rb.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code()==200){
                            Log.d("tag", "patch success");
                            Context con = getAppContext();
                            GlobalId.getInstance().setId(str_id);
                            finish();
                        }
                        else{
                            Log.d("tag", "Status code: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("tag", "Fail msg: "+t.getMessage());
                    }
                });
        }
    }
}

