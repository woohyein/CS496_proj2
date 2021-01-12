package com.example.cs496_proj2.CSCal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.cs496_proj2.GlobalId;
import com.example.cs496_proj2.R;
import com.example.cs496_proj2.contacts.Contact;
import com.example.cs496_proj2.contacts.ContactFragment;
import com.example.cs496_proj2.contacts.GlobalContacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ScoreListActivity extends AppCompatActivity {

    ArrayList<String> playerList = new ArrayList<String>();
    ArrayList<String> scoreList = new ArrayList<String>();
    int level = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_list);
        Intent intent = getIntent();
        level = intent.getExtras().getInt("level");
        Log.d("asdf", "" + level);

        getPlayerScore();
    }

    public void getPlayerScore() {
        new ScoreListActivity.JSONTask().execute("http://192.249.18.228:3003/take");//AsyncTask 시작시킴
        new ScoreListActivity.JSONTask().execute("http://192.249.18.228:3003/receive");//AsyncTask 시작시킴
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            if(urls[0].equals("http://192.249.18.228:3003/receive")) {
                try {
                    HttpURLConnection con = null;
                    BufferedReader reader = null;

                    try{
                        //URL url = new URL("http://192.168.25.16:3000/users");
                        URL url = new URL(urls[0]);
                        //연결을 함
                        con = (HttpURLConnection) url.openConnection();

                        con.setRequestMethod("POST");//POST방식으로 보냄
                        con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                        con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송

                        con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                        con.setDoOutput(false);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                        con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                        con.connect();

                        //서버로 부터 데이터를 받음
                        InputStream stream = con.getInputStream();

                        reader = new BufferedReader(new InputStreamReader(stream));

                        StringBuffer buffer = new StringBuffer();

                        String line = "";
                        while((line = reader.readLine()) != null){
                            buffer.append(line);
                        }

                        // Log.d("asdf", buffer.toString());
                        jsonParsing(buffer.toString());

                        return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                    } catch (MalformedURLException e){
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(con != null){
                            con.disconnect();
                        }
                        try {
                            if(reader != null){
                                reader.close();//버퍼를 닫아줌
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if(urls[0].equals("http://192.249.18.228:3003/take")){
                try {
                    //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("level", level);

                    HttpURLConnection con = null;
                    BufferedReader reader = null;

                    try {
                        //URL url = new URL("http://192.168.25.16:3000/users");
                        URL url = new URL(urls[0]);
                        //연결을 함
                        con = (HttpURLConnection) url.openConnection();

                        con.setRequestMethod("POST");//POST방식으로 보냄
                        con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                        con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송


                        con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                        con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                        con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                        con.connect();

                        //서버로 보내기위해서 스트림 만듬
                        OutputStream outStream = con.getOutputStream();
                        //버퍼를 생성하고 넣음
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                        writer.write(jsonObject.toString());
                        writer.flush();
                        writer.close();//버퍼를 받아줌

                        //서버로 부터 데이터를 받음
                        InputStream stream = con.getInputStream();

                        reader = new BufferedReader(new InputStreamReader(stream));

                        StringBuffer buffer = new StringBuffer();

                        String line = "";
                        while((line = reader.readLine()) != null){
                            buffer.append(line);
                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (con != null) {
                            con.disconnect();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    private void jsonParsing(String json)
    {
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray player_scoreArray = jsonObject.getJSONArray("player_score");
            for(int i=0; i<player_scoreArray.length(); i++) {
                JSONObject player_scoreObject = player_scoreArray.getJSONObject(i);

                String tmpPlayer = player_scoreObject.getString("player");
                String tmpScore = player_scoreObject.getString("score");

                Log.d("asdf", tmpPlayer + " / " + tmpScore);

                playerList.add(tmpPlayer);
                scoreList.add(tmpScore);
            }

            viewMake();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void viewMake() {
        int i=0;
        for(i=playerList.size(); i<10; i++){
            playerList.add("-");
            scoreList.add("-");
        }
        i=0;

        TextView player1 = findViewById(R.id.player1);
        TextView score1 = findViewById(R.id.score1);
        player1.setText("1st:   "+ playerList.get(i));
        score1.setText(scoreList.get(i));
        i++;

        TextView player2 = findViewById(R.id.player2);
        TextView score2 = findViewById(R.id.score2);
        player2.setText("2nd:   "+ playerList.get(i));
        score2.setText(scoreList.get(i));
        i++;

        TextView player3 = findViewById(R.id.player3);
        TextView score3 = findViewById(R.id.score3);
        player3.setText("3rd:   "+ playerList.get(i));
        score3.setText(scoreList.get(i));
        i++;

        TextView player4 = findViewById(R.id.player4);
        TextView score4 = findViewById(R.id.score4);
        player4.setText("4th:   "+ playerList.get(i));
        score4.setText(scoreList.get(i));
        i++;

        TextView player5 = findViewById(R.id.player5);
        TextView score5 = findViewById(R.id.score5);
        player5.setText("5th:   "+ playerList.get(i));
        score5.setText(scoreList.get(i));
        i++;

        TextView player6 = findViewById(R.id.player6);
        TextView score6 = findViewById(R.id.score6);
        player6.setText("6th:   "+ playerList.get(i));
        score6.setText(scoreList.get(i));
        i++;

        TextView player7 = findViewById(R.id.player7);
        TextView score7 = findViewById(R.id.score7);
        player7.setText("7th:   "+ playerList.get(i));
        score7.setText(scoreList.get(i));
        i++;

        TextView player8 = findViewById(R.id.player8);
        TextView score8 = findViewById(R.id.score8);
        player8.setText("8th:   "+ playerList.get(i));
        score8.setText(scoreList.get(i));
        i++;

        TextView player9 = findViewById(R.id.player9);
        TextView score9 = findViewById(R.id.score9);
        player9.setText("9th:   "+ playerList.get(i));
        score9.setText(scoreList.get(i));
        i++;

        TextView player10 = findViewById(R.id.player10);
        TextView score10 = findViewById(R.id.score10);
        player10.setText("10th:  "+ playerList.get(i));
        score10.setText(scoreList.get(i));
    }
}