package com.example.cs496_proj2.CSCal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs496_proj2.CSCal.CSCalFragment;
import com.example.cs496_proj2.CSCal.GameAdapter;
import com.example.cs496_proj2.R;
import com.example.cs496_proj2.contacts.Contact;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder>{

    private Fragment fragment;
    private ArrayList<String> mData;

    GameAdapter(ArrayList<String> list, Fragment fm) {
        mData = list ;
        fragment = fm;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView logView;

        ViewHolder(View itemView) {
            // Init Views and Button
            super(itemView);
            logView = itemView.findViewById(R.id.logtext);
        }
    }

    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item,
                parent, false);
        GameAdapter.ViewHolder vh = new GameAdapter.ViewHolder(v);
        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapter.ViewHolder holder, int position) {
        String element = mData.get(position) ;
        holder.logView.setText(element);
    }

    @Override
    public int getItemCount() {
        if (mData.isEmpty())
            return 0;
        return mData.size();
    }

}
