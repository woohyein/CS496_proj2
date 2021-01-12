package com.example.cs496_proj2.CSCal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.cs496_proj2.R;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChooseCharActivity extends AppCompatActivity {
    GameAPI gameAPI2;
    String serverUrl = "http://192.249.18.228:3002";
    ImageButton easy, normal, hard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_char);
        initMyAPI(serverUrl);

        easy = findViewById(R.id.easy);
        normal = findViewById(R.id.normal);
        hard = findViewById(R.id.hard);

        easy.setOnClickListener(this::m_onClick);
        normal.setOnClickListener(this::m_onClick);
        hard.setOnClickListener(this::m_onClick);
    }

    public void initMyAPI(String serverUrl){
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gameAPI2 = retrofit2.create(GameAPI.class);
    }

    public void InitServer(int i){
        ExecutorService service = Executors.newSingleThreadExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Call<ResponseBody> rb = gameAPI2.Init(i);
                ResponseBody result = null;
                try {
                    result = rb.execute().body();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        };

        Future future = service.submit(runnable);
        try{
            future.get();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void m_onClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.easy:
                InitServer(0);
                intent.putExtra("level", 0);
                setResult(1111, intent);
                finish();
                break;

            case R.id.normal:
                InitServer(1);
                intent.putExtra("level", 1);
                setResult(1111, intent);
                finish();
                break;

            case R.id.hard:
                InitServer(2);
                intent.putExtra("level", 2);
                setResult(1111, intent);
                finish();
                break;
        }
    }
}