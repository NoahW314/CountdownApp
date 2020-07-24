package com.example.countdownapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.countdownapp.Countdown;
import com.example.countdownapp.PopupHandler;
import com.example.countdownapp.R;

import static com.example.countdownapp.Countdown.dateFormatter;
import static com.example.countdownapp.Countdown.timeFormatter;

public class TimeFragment extends Fragment {
    private Countdown countdown;

    public TimeFragment(){}
    public TimeFragment(Countdown countdown){ this.countdown = countdown; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time, container, false);

        EditText timeText = view.findViewById(R.id.time_text);
        EditText dateText = view.findViewById(R.id.date_text);
        final EditText nameText = view.findViewById(R.id.name_time_text);

        timeText.setOnClickListener(v -> PopupHandler.popupTime(v, nameText));
        dateText.setOnClickListener(v -> PopupHandler.popupDate(v, nameText));

        nameText.setOnFocusChangeListener(PopupHandler::onFocusChanged);
        timeText.setOnFocusChangeListener(PopupHandler::onFocusChanged);
        dateText.setOnFocusChangeListener(PopupHandler::onFocusChanged);


        if(countdown != null){

            nameText.setText(countdown.name);
            if(countdown.time != null) timeText.setText(countdown.time.format(timeFormatter));
            dateText.setText(countdown.date.format(dateFormatter));
        }
        return view;
    }
}
