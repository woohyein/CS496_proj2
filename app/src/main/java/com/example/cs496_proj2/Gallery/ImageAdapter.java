package com.example.cs496_proj2.Gallery;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.cs496_proj2.R;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private final ArrayList<com.example.cs496_proj2.Gallery.ImageUnit> ImgList;
    public RequestManager mRequestManager;
   // Context mcontext;

     // ViewHolder: store item view
    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    // Constructor
    public ImageAdapter(ArrayList<com.example.cs496_proj2.Gallery.ImageUnit> ImgList, RequestManager mRequestManager) {
        this.ImgList = ImgList;
        this.mRequestManager = mRequestManager;
    }

    @NonNull
    @Override public com.example.cs496_proj2.Gallery.ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        com.example.cs496_proj2.Gallery.ImageUnit photo = ImgList.get(position);

        mRequestManager
                .load(photo.imageUri)
                .into(holder.image);

       // holder.image.setImageURI(photo.imageUri);
        holder.image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), com.example.cs496_proj2.Gallery.ViewImage.class);
                String str_uri = photo.imageUri.toString();
                intent.putExtra("uri", str_uri);
                //intent.putParcelableArrayListExtra("uri", FileList)
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override public int getItemCount() {
        return ImgList.size();
    }



}