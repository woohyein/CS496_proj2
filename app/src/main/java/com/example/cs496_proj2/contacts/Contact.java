package com.example.cs496_proj2.contacts;

import android.net.Uri;

public class Contact implements Comparable<Contact> {
    String phone, fullName;
    Uri image = null;

    public Contact(String ph, String fn, String bytes) {
        phone = ph;
        fullName = fn;
        if (bytes != null)  image = Uri.parse(bytes);
    }

        /* Useful Functions */
    public boolean isStartWith (String str) {
        return phone.startsWith(str);
    }
    public String getMsg() {
        return ("name=" + fullName + ", phone=" + phone);
    }

    @Override
    public int compareTo(Contact o) {
        return this.fullName.compareTo(o.fullName);
    }
}
