package com.example.cs496_proj2.contacts;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs496_proj2.R;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Fragment fragment;
    private ArrayList<Contact> mData;

    // Constructor
    ContactAdapter(ArrayList<Contact> list, Fragment fm) {
        mData = list ;
        fragment = fm;
    }

    @Override
    public int getItemCount() {
        if (mData.isEmpty())
            return 0;
        return mData.size();
    }

    // ViewHolder: store item view
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameView, numView;
        ImageButton callButton;

        ViewHolder(View itemView) {
            // Init Views and Button
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameView = itemView.findViewById(R.id.nameTextView);
            numView = itemView.findViewById(R.id.numTextView);
            callButton = (ImageButton) itemView.findViewById(R.id.callButton);

            // Click event for itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Contact ct = mData.get(pos);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(ContactsContract.Contacts.getLookupUri(ct.id, ct.lookup));
                        fragment.startActivityForResult(intent, 0);
                    }
                }
            });

            // Click event for callButton
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL,
                                                Uri.parse("tel:" + numView.getText()));
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public com.example.cs496_proj2.contacts.ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        com.example.cs496_proj2.contacts.ContactAdapter.ViewHolder vh = new com.example.cs496_proj2.contacts.ContactAdapter.ViewHolder(v) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(com.example.cs496_proj2.contacts.ContactAdapter.ViewHolder holder, int position) {
        Contact element = mData.get(position) ;
        if (element.image != null)
            holder.imageView.setImageURI(element.image);
        holder.nameView.setText(element.fullName);
        holder.numView.setText(element.phone);
    }

    public int findText (String text) {
        for (int i = 0; i < getItemCount(); i++) {
            Contact element = mData.get(i);
            if (element.fullName.toLowerCase().contains(text)
                    || element.phone.toString().contains(text)) {
                return i;
            }
        }
        return 0;
    }
}
