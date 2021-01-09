package com.example.cs496_proj2.contacts;

import java.util.ArrayList;
import java.util.Collections;

public class GlobalContacts {
    private ArrayList<Contact> contacts;
    public ArrayList<Contact> getContacts() {
        return this.contacts;
    }
    public void setContacts(ArrayList<Contact> contacts)
    {
        this.contacts = contacts;
    }
    public void addContact(Contact contact){
        if(contacts == null){
            this.contacts = new ArrayList<Contact>();
        }
        this.contacts.add(contact);
        Collections.sort(contacts);
    }

    private static GlobalContacts instance = null;

    public static synchronized GlobalContacts getInstance(){
        if(null == instance){
            instance = new GlobalContacts();
        }
        return instance;
    }
}
