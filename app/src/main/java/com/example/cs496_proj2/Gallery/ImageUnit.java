package com.example.cs496_proj2.Gallery;

import android.graphics.Bitmap;
import android.net.Uri;

public class ImageUnit {

    Bitmap imageBitmap;
    Uri imageUri;

    public ImageUnit(Uri imageUri, Bitmap imageBitmap) {
        super();

        this.imageUri = imageUri;
        this.imageBitmap = imageBitmap;
    }
}
