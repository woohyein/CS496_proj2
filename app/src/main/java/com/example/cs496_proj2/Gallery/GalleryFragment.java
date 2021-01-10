package com.example.cs496_proj2.Gallery;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.cs496_proj2.ApiService;
import com.example.cs496_proj2.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@RequiresApi(api = Build.VERSION_CODES.Q)
public class GalleryFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    ImageAdapter adapter;
    int CAPTURE_PHOTO = 10;
    public RequestManager mGlideRequestManager;
    Fragment fg;

    Button camera;
    Button downServer;
    Button backup;

    ApiService apiService;
    String user = "user1";

    public GalleryFragment() {
    }

    public static com.example.cs496_proj2.Gallery.GalleryFragment newInstance() {
        com.example.cs496_proj2.Gallery.GalleryFragment fragment = new com.example.cs496_proj2.Gallery.GalleryFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fg = this;
        mGlideRequestManager = Glide.with(this);
        // Init image list
        try {
           GlobalGallery.getInstance().setGallery(LoadImages());
        } catch (IOException e) {
            e.printStackTrace();
        }
        initRetrofitClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // mRecyclerView Initialization
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        // Set LayoutManager
        layoutManager = new GridLayoutManager(requireContext(), 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.scrollToPosition(0);

        // Set Adapter
        adapter = new ImageAdapter(mGlideRequestManager, fg);
        mRecyclerView.setAdapter(adapter);





        // camera Button
        camera = (Button) view.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_PHOTO);
            }
        });

        // backup Button
        backup = (Button) view.findViewById(R.id.backup);
        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BackupGalleryActivity.class);
                startActivity(intent);
            }
        });

        // downServer Button
        downServer = (Button) view.findViewById(R.id.downServer);
        downServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadimage();
            }
        });

        return view;
    }

    // Load photos and make FileList of them
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public ArrayList<ImageUnit> LoadImages() throws IOException {
        ArrayList<ImageUnit> FileList = new ArrayList<>();
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
        };

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);

        while(cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            FileList.add(new ImageUnit(imageUri, UriToBitmap(imageUri)));
        }
        cursor.close();

        return FileList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == CAPTURE_PHOTO) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data"); // android 8.0.0에서 문제 발생하는 부분
                    Uri ChangedUri = BitmapToUri(bitmap);
                    GlobalGallery.getInstance().addImage(new ImageUnit(ChangedUri, bitmap));
                }
            }
//            com.example.cs496_proj2.MainActivity main = (com.example.cs496_proj2.MainActivity) getActivity();
//            assert main != null;
//            main.setViewPager(1);

            adapter.notifyDataSetChanged();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(fg).attach(fg).commit();
        }
    }

   // public Uri BitmapToUri(Context context, Bitmap bitmap){
        public Uri BitmapToUri(Bitmap bitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(requireContext().getContentResolver(), bitmap, "Title"+System.currentTimeMillis(), null);
        return Uri.parse(path);
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
                        GlobalGallery.getInstance().addImage(new ImageUnit("http://192.249.18.228:3001/uploads/" + tmp));
                    }
                    Toast.makeText(getActivity(), "" + response.body().size() + "개의 사진을 복구했습니다", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(fg).attach(fg).commit();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Log.d("asdf", "실패!!");
                t.printStackTrace();
            }
        });
    }

    public Bitmap UriToBitmap(Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);
    }

}
