package com.example.countdownapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.countdownapp.Countdown;
import com.example.countdownapp.PopupHandler;
import com.example.countdownapp.R;

public class DateFragment extends Fragment {
    private Countdown countdown;

    public DateFragment(){}
    public DateFragment(Countdown countdown){ this.countdown = countdown; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date, container, false);
        DatePicker datePicker = view.findViewById(R.id.date_picker);
        datePicker.setMinDate(System.currentTimeMillis()+86_400_000);

        EditText nameText = view.findViewById(R.id.name_date_text);
        nameText.setOnFocusChangeListener(PopupHandler::onFocusChanged);

        if(countdown != null){
            nameText.setText(countdown.name);

            datePicker.updateDate(countdown.date.getYear(), countdown.date.getMonthValue()-1, countdown.date.getDayOfMonth());
        }
        return view;
    }
}
