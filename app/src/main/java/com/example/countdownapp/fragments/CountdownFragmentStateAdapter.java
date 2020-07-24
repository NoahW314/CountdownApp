package com.example.countdownapp.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class CountdownFragmentStateAdapter extends FragmentStateAdapter {
    public CountdownFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            return new DateFragment();
        }
        else if(position == 1) {
            return new TimeFragment();
        }
        else{
            throw new IllegalArgumentException(position+" is not a valid position!");
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
