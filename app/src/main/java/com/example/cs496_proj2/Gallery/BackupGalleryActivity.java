package com.example.cs496_proj2.Gallery;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.example.cs496_proj2.ApiService;
import com.example.cs496_proj2.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackupGalleryActivity extends AppCompatActivity {

    ArrayList<String> imagePath = new ArrayList<String>();
    ApiService apiService;
    String user = "user1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_gallery);
        initRetrofitClient();

        Log.d("asdf", "BackupGalleryActivity");
        loadimage();


    }


    private void initRetrofitClient(){
        OkHttpClient client = new OkHttpClient.Builder().build();
        apiService = new Retrofit.Builder().baseUrl("http://192.249.18.228:3001").addConverterFactory(GsonConverterFactory.create()).client(client).build().create(ApiService.class);
    }

    public void loadimage(){
        Call<ArrayList<String>> req = apiService.loadImage(user);
        req.enqueue(new Callback<ArrayList<String>>(){
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if (response.code()==200){
                    Log.d("asdf", "성공!!");
                    for(String tmp: response.body()){
                        Log.d("asdf", "" + tmp + " / " + response.body().size());
                        imagePath.add(tmp);
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Log.d("asdf", "실패!!");
                t.printStackTrace();
            }
        });
    }
}