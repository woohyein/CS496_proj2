package com.example.cs496_proj2.contacts;

import android.net.Uri;

public class Contact {
    String phone, fullName, lookup;
    long personId;
    Uri image = null;
    int id;

    public Contact(String ph, String fn, String bytes, long pid, String key) {
        phone = ph;
        fullName = fn;
        if (bytes != null)  image = Uri.parse(bytes);
        personId = pid;
        lookup = key;
    }

        /* Useful Functions */
    public boolean isStartWith (String str) {
        return phone.startsWith(str);
    }
    public String getMsg() {
        return ("name=" + fullName + ", phone=" + phone);
    }
    public void setId(int i) {
        id = i;
    }
}
