package com.example.cs496_proj2.contacts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs496_proj2.R;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Fragment fragment;
    private ArrayList<Contact> mData = GlobalContacts.getInstance().getContacts();

    // Constructor
    ContactAdapter(Fragment fm) {
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // 오랫동안 눌렀을 때 이벤트가 발생됨
                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
                    builder.setTitle("삭제하시겠습니까?")
                            .setPositiveButton("삭제",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Log.d("asdf", "position: " + getAdapterPosition());
                                    GlobalContacts.getInstance().getContacts().remove(getAdapterPosition());
                                    notifyDataSetChanged();
                                    FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                                    ft.detach(fragment).attach(fragment).commit();
                                }
                            })
                            .setNegativeButton("취소",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {}
                            })
                            .show();

                    Log.d("asdf", "long click");
                    return false;
                }
            });

            // Click event for callButton
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + numView.getText()));
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
