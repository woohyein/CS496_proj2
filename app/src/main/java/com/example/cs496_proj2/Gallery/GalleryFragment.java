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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.cs496_proj2.Login.MainActivity;
import com.example.cs496_proj2.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


@RequiresApi(api = Build.VERSION_CODES.Q)
public class GalleryFragment extends Fragment {

    public ArrayList<ImageUnit> FileList;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    ImageAdapter adapter;
    int CAPTURE_PHOTO = 10;
    ImageView imageview;
    public RequestManager mGlideRequestManager;

    public GalleryFragment() {
    }

    public static com.example.cs496_proj2.Gallery.GalleryFragment newInstance() {
        com.example.cs496_proj2.Gallery.GalleryFragment fragment = new com.example.cs496_proj2.Gallery.GalleryFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlideRequestManager = Glide.with(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // mRecyclerView Initialization
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        imageview = (ImageView) view.findViewById(R.id.image_view);

        // Camera Button
        ImageButton camera = (ImageButton) view.findViewById(R.id.camera);
        camera.setOnClickListener(v ->{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAPTURE_PHOTO);
        });

        // Set LayoutManager
        layoutManager = new GridLayoutManager(requireContext(), 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.scrollToPosition(0);

        // Init image list
        FileList = LoadImages();

        // Set Adapter
        adapter = new ImageAdapter(FileList, mGlideRequestManager);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    // Load photos and make FileList of them
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public ArrayList<ImageUnit> LoadImages(){
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
            FileList.add(new ImageUnit(imageUri));
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
                    FileList.add(new ImageUnit(ChangedUri));
                }
            }
            MainActivity main = (MainActivity) getActivity();
            assert main != null;
            main.setViewPager(1);
        }
    }

   // public Uri BitmapToUri(Context context, Bitmap bitmap){
        public Uri BitmapToUri( Bitmap bitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(requireContext().getContentResolver(), bitmap, "Title"+System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

}
