package com.example.cs496_proj2.Gallery;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs496_proj2.R;
import com.github.chrisbanes.photoview.PhotoView;

public class ViewImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        // photo zoom
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        Bundle extras = getIntent().getExtras();
        String s = extras.getString("uri");
        Uri myUri = Uri.parse(s);
        photoView.setImageURI(myUri);
    }
}