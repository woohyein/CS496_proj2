package com.example.cs496_proj2;

public class GlobalId {
    private String id = "user1";
    private String name = "name1";

    public String getId() {
        return this.id;
    }

    public String getName() { return this.name; }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {this.name = name;}

    private static GlobalId instance = null;

    public static synchronized GlobalId getInstance() {
        if (null == instance) {
            instance = new GlobalId();
        }
        return instance;
    }
}
