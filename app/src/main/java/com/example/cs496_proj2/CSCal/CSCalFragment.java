package com.example.cs496_proj2.CSCal;

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
import com.example.cs496_proj2.MainActivity;
import com.example.cs496_proj2.R;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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
    ArrayList<String> log;
    Button submit;
    EditText answer;
    GameAPI gameAPI;
    String baseUrl = "http://192.249.18.209:3001";
    final int[] flag = {0};
    Boolean isVerified = false;

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

        // Init layout & clickListener
        submit = view.findViewById(R.id.submit_button);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_game);
        answer = (EditText) view.findViewById(R.id.answer);
        log = new ArrayList<String>();
        initMyAPI(baseUrl);

        // Set layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Initialize adapter
        adapter = new com.example.cs496_proj2.CSCal.GameAdapter(log, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (adapter.getItemCount() != 0){
            //recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-1);
            recyclerView.scrollToPosition(adapter.getItemCount()-1);
        }



        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set clickListeners
        submit.setOnClickListener(v -> {
            String mAnswer = answer.getText().toString();
            CheckValidate(mAnswer);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            if (isVerified) {
                log.add("You : " + mAnswer);
                adapter.notifyDataSetChanged();
                // Time Delay
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        log.add("Computer : "+ ServerTurn(mAnswer.charAt(mAnswer.length()-1)));
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
            else{
                Toast msg = Toast.makeText(getContext(), "Input is not valid", Toast.LENGTH_SHORT);
                msg.show();
            }

            answer.setText(null);
            isVerified = false;
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void CheckValidate(String answer) {
        isVerified = true;
        // check null
        if (answer == null || (answer.length() == 0)) {
            isVerified = false;
        }

        // check blank char
        for (int i = 0; i < answer.length(); i++) {
            if (Character.isWhitespace(answer.charAt(i))) {
                isVerified = false;
            }
        }

        // check whether the first char matches the last char of former word
 /*       if (log.size() != 0){
            String last = log.get(log.size() - 1);
            if (last.charAt(last.length()-1) != answer.charAt(0)){
                isVerified = false;
            }
        }*/

        // check from wikipedia
        getwebcalls(answer);

    }

    private void getwebcalls(String answer){
        Call<ResponseBody> rb = gameAPI.CheckVal(answer);
        rb.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    isVerified = true;
                    Log.d("adf", "success");
                }
                else{
                    Log.d("123", "code: "+response.code());
                    isVerified = false;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("er", "Server failed " + t.getMessage());
                isVerified = false;
            }
        });
    }

    // Server's turn & Save what we did (in server)
    public String ServerTurn(char a){
        String str = null;

        if (str == null){ // Out of word
            return "You win!";
        }

        return str;
    }

    public void initMyAPI(String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gameAPI = retrofit.create(GameAPI.class);
    }
}
