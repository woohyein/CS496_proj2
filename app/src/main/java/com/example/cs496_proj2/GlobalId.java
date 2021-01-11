package com.example.cs496_proj2;

public class GlobalId {
    private String id = "user1";

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private static GlobalId instance = null;

    public static synchronized GlobalId getInstance() {
        if (null == instance) {
            instance = new GlobalId();
        }
        return instance;
    }
}
