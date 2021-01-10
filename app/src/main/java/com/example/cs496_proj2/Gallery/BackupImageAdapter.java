package com.example.cs496_proj2.Gallery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.cs496_proj2.ApiService;
import com.example.cs496_proj2.R;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackupImageAdapter extends RecyclerView.Adapter<BackupImageAdapter.ViewHolder> {

    RequestManager glideRequestManager;
    private ArrayList<String> mData = null;
    Context context;
    Context getAppContext;
    Activity activity;

    ApiService apiService;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    BackupImageAdapter(RequestManager glideRequestManager, ArrayList<String> list, Context context, Context getAppContext, Activity activity) {
        this.mData = list ;
        this.glideRequestManager = glideRequestManager;
        this.context = context;
        this.getAppContext = getAppContext;
        this.activity = activity;

        Log.d("asdf", "BackupImageAdapter 생성자 실행은 되냐?");
        initRetrofitClient();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), com.example.cs496_proj2.Gallery.ViewImage.class);
                    if (intent != null) {
                        String str_uri = "http://192.249.18.228:3001/uploads/"+mData.get(getAdapterPosition());
                        intent.putExtra("uri", str_uri);
                        //intent.putParcelableArrayListExtra("uri", FileList)
                        v.getContext().startActivity(intent);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // 오랫동안 눌렀을 때 이벤트가 발생됨
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("삭제하시겠습니까?")
                            .setPositiveButton("삭제",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Log.d("asdf", "position: " + getAdapterPosition());
                                    delimage(mData.get(getAdapterPosition()));

                                    Intent intent = new Intent(activity, BackupGalleryActivity.class);
                                    activity.startActivity(intent);
                                    activity.overridePendingTransition(0,0);
                                    activity.finish();
                                }
                            })
                            .setNegativeButton("취소",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {}
                            })
                            .show();

                    Log.d("asdf", "long click");
                    return false;
                }
            });
        }
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public BackupImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new BackupImageAdapter.ViewHolder(view);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(BackupImageAdapter.ViewHolder holder, int position) {
        String photoPath = mData.get(position);

        glideRequestManager
                .load("http://192.249.18.228:3001/uploads/"+photoPath)
                .into(holder.image);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    private void initRetrofitClient(){
        OkHttpClient client = new OkHttpClient.Builder().build();
        apiService = new Retrofit.Builder().baseUrl("http://192.249.18.228:3001").addConverterFactory(GsonConverterFactory.create()).client(client).build().create(ApiService.class);
    }

    public void delimage(String path){
        Call<ResponseBody> req = apiService.delImage(path);
        req.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200){
                    Log.d("asdf", "성공!!");
                    Toast.makeText(getAppContext, "사진 1장을 삭제 했습니다", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("asdf", "실패!!");
                t.printStackTrace();
            }
        });
    }
}
