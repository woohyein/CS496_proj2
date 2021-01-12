package com.example.cs496_proj2.CSCal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs496_proj2.CSCal.GameAPI;
import com.example.cs496_proj2.GlobalId;
import com.example.cs496_proj2.MainActivity;
import com.example.cs496_proj2.R;
import com.example.cs496_proj2.contacts.AddContactActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CSCalFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private GameAdapter adapter;
    ArrayList<String> log, data;
    Button submit, verify, start;
    EditText answer;
    GameAPI gameAPI1, gameAPI2;
    String baseUrl = "http://192.249.18.228:3005";
    String serverUrl = "http://192.249.18.228:3002";
    Boolean isVerified = false;
    Boolean isLose = false;
    int level = 0;


    public CSCalFragment() {
    }

    public static com.example.cs496_proj2.CSCal.CSCalFragment newInstance() {
        com.example.cs496_proj2.CSCal.CSCalFragment fragment = new com.example.cs496_proj2.CSCal.CSCalFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c_s_cal, container, false);

        // Init layout
        submit = view.findViewById(R.id.submit_button);
        verify = view.findViewById(R.id.verify_button);
        start = view.findViewById(R.id.start_button);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_game);
        answer = (EditText) view.findViewById(R.id.answer);
        log = new ArrayList<String>();
        data = new ArrayList<String>();
        initMyAPI(baseUrl, serverUrl);

        // Set layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Initialize adapter
        adapter = new com.example.cs496_proj2.CSCal.GameAdapter(log, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (adapter.getItemCount() != 0){
            recyclerView.scrollToPosition(adapter.getItemCount()-1);
        }

        // set clickListeners
        verify.setOnClickListener(this::m_onClick);
        submit.setOnClickListener(this::m_onClick);
        start.setOnClickListener(this::m_onClick);

        FloatingActionButton btn = (FloatingActionButton) view.findViewById(R.id.score);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScoreListActivity.class);
                intent.putExtra("level", level);
                startActivity(intent);
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        level = data.getExtras().getInt("level");
        Log.d("asdf", "" + level);
    }

    public void CheckValidate(String answer) {
        // check null
        if (answer == null || (answer.length() == 0)) {
            isVerified = false;
            return;
        }

        // check blank char
        for (int i = 0; i < answer.length(); i++) {
            if (Character.isWhitespace(answer.charAt(i))) {
                isVerified = false;
                return;
            }
        }

        // check whether the first char matches the last char of former word
        if (log.size() != 0){
            String last = log.get(log.size() - 1);
            if (last.charAt(last.length()-1) != answer.charAt(0)){
                isLose = true;
                isVerified = true;
                return;
            }
        }

        // Compare from previous result
        if (data.contains(answer)) {
            isLose = true;
            isVerified = true;
            return;
        }

        // check from wikipedia
        getWebCalls(answer);
    }

    private void getWebCalls(String answer){
        ExecutorService service = Executors.newSingleThreadExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Call<ResponseBody> rb = gameAPI1.CheckVal(answer);
                ResponseBody result = null;
                String s = null;
                try {
                    result = rb.execute().body();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                try {
                    s = result.string();
                    if (s.equals("a")) {
                        isVerified = false;
                    } else {
                        isVerified = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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

    public String ServerTurn(String a){
        final String[] str = {null};
        ExecutorService service = Executors.newSingleThreadExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Call<ResponseBody> rb = gameAPI2.GetWord(a);
                ResponseBody result = null;
                String s = null;
                try {
                    result = rb.execute().body();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                try {
                    s = result.string();
                    str[0] = s;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Future future = service.submit(runnable);
        try{
            future.get();
        } catch (Exception e){
            e.printStackTrace();
        }

        if (str[0] == null || str[0].equals("NULL")){
            return "You win!";
        }

        return str[0];
    }

    public void initMyAPI(String baseUrl, String serverUrl){
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gameAPI1 = retrofit1.create(GameAPI.class);

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gameAPI2 = retrofit2.create(GameAPI.class);
    }

    public void postLog(String mAnswer){
        log.add("You : " + mAnswer);
        data.add(mAnswer);
        adapter.notifyDataSetChanged();
    }

    public void postCom(String Serverturn){
        log.add("Computer : " + Serverturn);
        data.add(Serverturn);
        adapter.notifyDataSetChanged();
    }



    public void sendscore(int mscore){
        Log.d("asdf", "sendscore");
        ExecutorService service = Executors.newSingleThreadExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Call<ResponseBody> rb = gameAPI2.PostScore(GlobalId.getInstance().getName(), mscore, level);
                ResponseBody result = null;
                try {
                    result = rb.execute().body();
                    Log.d("asdf", "sendscore2");
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

    @SuppressLint("NonConstantResourceId")
    public void m_onClick(View view){
        String mAnswer = answer.getText().toString();

        switch(view.getId()){
            case R.id.verify_button:
                CheckValidate(answer.getText().toString());

                // able to submit
                if (isVerified) {
                    postLog(mAnswer);
                    recyclerView.scrollToPosition(adapter.getItemCount()-1);
                    if(isLose){
                        log.add("Computer : You Lose!");
                        Log.d("asdf","adf");
                        recyclerView.scrollToPosition(adapter.getItemCount()-1);
                        verify.setVisibility(View.INVISIBLE);
                        submit.setVisibility(View.INVISIBLE);
                        start.setVisibility(View.VISIBLE);
                        sendscore(0);
                        isLose = false;
                        break;
                    }

                    verify.setVisibility(View.INVISIBLE);
                    submit.setVisibility(View.VISIBLE);
                }

                else{ // The word doesn't exist
                    Toast msg = Toast.makeText(getContext(), "Input is not valid", Toast.LENGTH_SHORT);
                    msg.show();
                    verify.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.INVISIBLE);
                    start.setVisibility(View.INVISIBLE);
                }

                isVerified = false;
                break;

            case R.id.submit_button:
                String com = ServerTurn(mAnswer);
                postCom(com);
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
                verify.setVisibility(View.VISIBLE);
                submit.setVisibility(View.INVISIBLE);
                answer.setText(null);
                isVerified = false;

                if (com.equals("You win!")||com.equals("You Lose!")){
                    verify.setVisibility(View.INVISIBLE);
                    start.setVisibility(View.VISIBLE);
                    if(com.equals("You win!")){
                        sendscore(log.size()/2);
                    }
                    else{
                        sendscore(0);
                    }
                }
                break;

            case R.id.start_button:
                Intent intent = new Intent(getActivity(), ChooseCharActivity.class);
                startActivityForResult(intent, 1111);

                log.clear();
                data.clear();
                answer.setText(null);
                adapter.notifyDataSetChanged();
                verify.setVisibility(View.VISIBLE);
                start.setVisibility(View.INVISIBLE);
                break;
        }
    }

}
