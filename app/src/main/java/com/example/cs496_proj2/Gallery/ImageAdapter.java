package com.example.cs496_proj2.Gallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.cs496_proj2.R;
import com.example.cs496_proj2.contacts.GlobalContacts;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private final ArrayList<com.example.cs496_proj2.Gallery.ImageUnit> ImgList = GlobalGallery.getInstance().getGallery();
    public RequestManager mRequestManager;

    private Fragment fragment;
   // Context mcontext;

    // Constructor
    public ImageAdapter(RequestManager mRequestManager, Fragment fm) {
        this.mRequestManager = mRequestManager;
        this.fragment = fm;
    }

     // ViewHolder: store item view
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), com.example.cs496_proj2.Gallery.ViewImage.class);
                    if (intent != null) {
                        String str_uri = ImgList.get(getAdapterPosition()).imageUri.toString();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
                    builder.setTitle("삭제하시겠습니까?")
                            .setPositiveButton("삭제",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Log.d("asdf", "position: " + getAdapterPosition());
                                    GlobalGallery.getInstance().getGallery().remove(getAdapterPosition());
                                    notifyDataSetChanged();
                                    FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                                    ft.detach(fragment).attach(fragment).commit();
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
    }

    @Override public int getItemCount() {
        return ImgList.size();
    }



}