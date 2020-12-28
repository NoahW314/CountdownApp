package com.example.countdownapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import java.util.ArrayList;

public class ReminderView extends FrameLayout implements AdapterView.OnItemSelectedListener{
    /*Reminder Ideas:
    Amount of Time/Days before end of countdown
        Elements:
            Number input for Days (or Date Picker) / Number input for Time (or Date+Time Picker)
                Picker Dialogs
                Edit Text
                Spinner (with common values)
                AutoCompleteTextView (~ Spinner+EditText Combo)
            Notification Type (Alarm? or Notify)
                Spinner
                Switch thingy
                Check box?
                Look for other binary selector


    At regular intervals between now and end of countdown
    At regular intervals between point and end of countdown
    At custom intervals between now and end of countdown
    */
    public ReminderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        inflate(context, R.layout.reminder_view, this);

        Spinner reminderTypeSpinner = findViewById(R.id.reminder_type);
        ArrayList<String> reminderTypes = new ArrayList<>();
        for(ReminderTypes reminderType : ReminderTypes.values()){
            reminderTypes.add(reminderType.string);
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item, reminderTypes);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        reminderTypeSpinner.setOnItemSelectedListener(this);
        reminderTypeSpinner.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0:
                findViewById(R.id.time_before_layout).setVisibility(GONE);
                findViewById(R.id.regular_intervals_layout).setVisibility(GONE);
                findViewById(R.id.custom_points_layout).setVisibility(GONE);
                break;
            case 1:
                findViewById(R.id.time_before_layout).setVisibility(VISIBLE);
                findViewById(R.id.regular_intervals_layout).setVisibility(GONE);
                findViewById(R.id.custom_points_layout).setVisibility(GONE);
                break;
            case 2:
                findViewById(R.id.time_before_layout).setVisibility(GONE);
                findViewById(R.id.regular_intervals_layout).setVisibility(VISIBLE);
                findViewById(R.id.custom_points_layout).setVisibility(GONE);
                break;
            case 3:
                findViewById(R.id.time_before_layout).setVisibility(GONE);
                findViewById(R.id.regular_intervals_layout).setVisibility(GONE);
                findViewById(R.id.custom_points_layout).setVisibility(VISIBLE);
                break;
            default:
                throw new IllegalArgumentException("The position "+position+" is invalid.");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public enum ReminderTypes{
        NONE("None"), TIME_BEFORE("Time Before"), REGULAR_INTERVALS("Regular Intervals"), CUSTOM_POINTS("Custom Points");
        public String string;
        ReminderTypes(String string){
            this.string = string;
        }
    }
}
