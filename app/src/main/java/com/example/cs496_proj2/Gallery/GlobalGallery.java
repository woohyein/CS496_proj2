package com.example.cs496_proj2.Gallery;

import java.util.ArrayList;

public class GlobalGallery {
    private ArrayList<ImageUnit> images;
    public ArrayList<ImageUnit> getGallery() {
        return this.images;
    }
    public void setGallery(ArrayList<ImageUnit> images)
    {
        this.images = images;
    }
    public void addImage(ImageUnit image){
        if(images == null){
            this.images = new ArrayList<ImageUnit>();
        }
        this.images.add(image);
    }

    private static GlobalGallery instance = null;

    public static synchronized GlobalGallery getInstance(){
        if(null == instance){
            instance = new GlobalGallery();
        }
        return instance;
    }
}
