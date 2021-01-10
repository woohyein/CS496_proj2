package com.example.cs496_proj2.Gallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.cs496_proj2.ApiService;
import com.example.cs496_proj2.R;
import com.example.cs496_proj2.contacts.GlobalContacts;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewImage extends AppCompatActivity {

    String user = "user1";

    ApiService apiService;
    Bitmap pickedImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        initRetrofitClient();

        // photo zoom
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        Bundle extras = getIntent().getExtras();
        String s = extras.getString("uri");
        Uri myUri = Uri.parse(s);
//        photoView.setImageURI(myUri);
        Glide.with(this).load(s).into(photoView);
        Log.d("asdf", s);



        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 오랫동안 눌렀을 때 이벤트가 발생됨 <- 이전에 다이얼로그로 백업 여부 확인
                Log.d("asdf", "image long click");
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewImage.this);
                builder.setTitle("백업하시겠습니까?")
                        .setPositiveButton("백업",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    UriToBitmap(myUri);
                                    postimage();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("취소",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {}
                        })
                        .show();
                return false;
            }
        });
    }

    private void initRetrofitClient(){
        OkHttpClient client = new OkHttpClient.Builder().build();
        apiService = new Retrofit.Builder().baseUrl("http://192.249.18.228:3001").client(client).build().create(ApiService.class);
    }

    public void postimage() throws IOException {
        File filesDir = getApplicationContext().getFilesDir();
        File file = new File(filesDir, "image" + ".png");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pickedImg.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] b = baos.toByteArray();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(b);
        fos.flush();
        fos.close();

        RequestBody reqfile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqfile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), user);

        Call<ResponseBody> req = apiService.postImage(body, name);

        req.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200){
                    Log.d("asdf", "성공!!");
                }
                Toast.makeText(getApplicationContext(), "사진 1장을 백업 했습니다", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("asdf", "실패!!");
                Toast.makeText(getApplicationContext(), "사진 백업을 실패 했습니다", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    public void UriToBitmap(Uri uri) throws IOException {
        pickedImg = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
    }

}