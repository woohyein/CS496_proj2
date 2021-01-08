package com.example.cs496_proj2.Login;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cs496_proj2.CSCal.CSCalFragment;
import com.example.cs496_proj2.contacts.ContactFragment;

public class TabPagerAdapter extends FragmentStateAdapter {
    private int tabCount;

    public TabPagerAdapter(FragmentActivity fa, int count) {
        super(fa);
        tabCount = count;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
            default:
                return ContactFragment.newInstance();
            case 1:
                return com.example.cs496_proj2.Gallery.GalleryFragment.newInstance();
            case 2:
                return CSCalFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return tabCount;
    }
}
