package com.example.cs496_proj2.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs496_proj2.MainActivity;
import com.example.cs496_proj2.R;

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
import java.util.LinkedHashSet;

public class ContactFragment extends Fragment {
    View view;

    public ArrayList<com.example.cs496_proj2.contacts.Contact> contacts;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private com.example.cs496_proj2.contacts.ContactAdapter adapter;

    public ContactFragment() {
        // Required empty public constructor
    }

    public static com.example.cs496_proj2.contacts.ContactFragment newInstance() {
        // no arguments
        com.example.cs496_proj2.contacts.ContactFragment fragment = new com.example.cs496_proj2.contacts.ContactFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // RecyclerView Initialization
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                                        new LinearLayoutManager(getActivity()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Set LayoutManager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);

        // Init contact list
        contacts = getContacts();

        // Set Adapter
        adapter = new com.example.cs496_proj2.contacts.ContactAdapter(contacts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Init SearchView
        SearchView searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int pos = adapter.findText(newText);
                recyclerView.scrollToPosition(pos);
                return true;
            }
        });

        // Init addButton
        ImageButton addButton = (ImageButton) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(intent, 0);
            }
        });

        // goServerButton
        ImageButton goServer = (ImageButton) view.findViewById(R.id.goServer);
        goServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute("http://192.249.18.228:3000/remove");//AsyncTask 시작시킴
                new JSONTask().execute("http://192.249.18.228:3000/send");//AsyncTask 시작시킴
                // new JSONTask().execute("http://192.249.18.228:3000/receive");//AsyncTask 시작시킴
                Toast.makeText(getActivity(), "" + contacts.size() +  "개의 연락처를 동기화 했습니다", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MainActivity main = (MainActivity) getActivity();
        main.setViewPager(0);
    }

    private ArrayList<com.example.cs496_proj2.contacts.Contact> getContacts() {
        // Init Cursor
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Photo.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
        };
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor cursor = requireActivity().getContentResolver().query(
                uri, projection, null, null, sortOrder);

        // Read Data
        LinkedHashSet<com.example.cs496_proj2.contacts.Contact> hasList = new LinkedHashSet<com.example.cs496_proj2.contacts.Contact>();
        ArrayList<com.example.cs496_proj2.contacts.Contact> contacts;

        if (cursor.moveToFirst()) {
            do {
                String phone = cursor.getString(0);
                String fullName = cursor.getString(1);
                String image = cursor.getString(2);
                long person = cursor.getLong(3);
                String lookup = cursor.getString(4);

                com.example.cs496_proj2.contacts.Contact contact = new com.example.cs496_proj2.contacts.Contact(phone, fullName, image, person, lookup);

                if (contact.isStartWith("01")) {
                    hasList.add(contact);
                    Log.d("<<CONTACTS>>", contact.getMsg());
                }

            } while (cursor.moveToNext());
        }

        contacts = new ArrayList<com.example.cs496_proj2.contacts.Contact>(hasList);
        for (int i = 0; i < contacts.size(); i++) {
            contacts.get(i).setId(i);
        }

        if (cursor != null) {
            cursor.close();
        }
        return contacts;
    }






    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            if(urls[0].equals("http://192.249.18.228:3000/send")){
                for(Contact thisContact: contacts) {
                    try {
                        //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.accumulate("user", "user1");
                        jsonObject.accumulate("phone", thisContact.phone);
                        jsonObject.accumulate("fullName", thisContact.fullName);
                        jsonObject.accumulate("lookup", thisContact.lookup);
                        jsonObject.accumulate("personId", thisContact.personId);
                        if(thisContact.image != null) jsonObject.accumulate("image", thisContact.image.toString());
                        else jsonObject.accumulate("image", "null");
                        jsonObject.accumulate("id", thisContact.id);

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
            } else if(urls[0].equals("http://192.249.18.228:3000/receive")) {
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
            } else if(urls[0].equals("http://192.249.18.228:3000/remove")){
                try {
                    //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("user", "user1");

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
            Toast.makeText(getContext().getApplicationContext(),"OK!", Toast.LENGTH_SHORT);
        }
    }
}
