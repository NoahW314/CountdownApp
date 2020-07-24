package com.example.countdownapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CountdownAdapter extends ArrayAdapter<Countdown> {
    public CountdownAdapter(@NonNull Context context, @NonNull List<Countdown> objects) {
        super(context, 0, objects);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Countdown countdown = getItem(position);
        if(countdown == null){
            throw new IllegalArgumentException("There is no countdown in position "+position);
        }
        return new CountdownView(getContext(), countdown);
    }
}
