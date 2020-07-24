package com.example.countdownapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.IntegerRes;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.countdownapp.Countdown.dateFormatter;
import static com.example.countdownapp.Countdown.timeFormatter;

public class PopupHandler{
    private static void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static void popupDate(View view, View clearFocus){
        clearFocus.clearFocus();
        final EditText text = (EditText)view;
        String currentDate = text.getText().toString();
        LocalDate date = LocalDate.now();
        if(!currentDate.equals("")){
            date = LocalDate.parse(text.getText().toString(), dateFormatter);
        }

        DatePickerDialog dialog = new DatePickerDialog(text.getContext(), (view1, year, month, dayOfMonth) ->
                text.setText(LocalDate.of(year, month+1, dayOfMonth).format(dateFormatter)), date.getYear(), date.getMonthValue()-1, date.getDayOfMonth());

        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();

        hideKeyboard(view);
    }

    public static void popupTime(View view, View clearFocus){
        clearFocus.clearFocus();
        final EditText text = (EditText)view;
        String currentTime = text.getText().toString();
        LocalTime time = LocalTime.of(12, 0);
        if(!currentTime.equals("")){
            time = LocalTime.parse(currentTime, timeFormatter);
        }

        TimePickerDialog dialog = new TimePickerDialog(text.getContext(), (view1, hourOfDay, minute) ->
                text.setText(LocalTime.of(hourOfDay, minute).format(timeFormatter)), time.getHour(), time.getMinute(),false);
        dialog.show();

        hideKeyboard(view);
    }

    public static void onFocusChanged(View v, boolean hasFocus) {
        if(!hasFocus){
            hideKeyboard(v);
        }
    }
}
