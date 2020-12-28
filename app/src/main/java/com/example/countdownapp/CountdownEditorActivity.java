package com.example.countdownapp;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.countdownapp.fragments.CountdownCreationError;
import com.example.countdownapp.fragments.DateFragment;
import com.example.countdownapp.fragments.TimeFragment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.example.countdownapp.Countdown.dateFormatter;
import static com.example.countdownapp.Countdown.timeFormatter;

public class CountdownEditorActivity extends AppCompatActivity {

    private Countdown countdown;
    private Fragment currentFragment;
    private boolean isTimeSensitive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_editor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        if (bar == null) throw new AssertionError("There is no action bar!");
        bar.setDisplayHomeAsUpEnabled(true);

        int index = getIntent().getIntExtra(Countdowns.intentName, -1);
        countdown = Countdowns.getInstance().get(index);
        isTimeSensitive = countdown.isTimeSensitive;

        setFragment();
    }

    private void setFragment(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        if(currentFragment != null) trans.remove(currentFragment);

        if(isTimeSensitive){
            currentFragment = new TimeFragment(countdown);
            findViewById(R.id.change_to_date).setVisibility(View.VISIBLE);
        }
        else{
            currentFragment = new DateFragment(countdown);
            findViewById(R.id.change_to_date_time).setVisibility(View.VISIBLE);
        }

        trans.add(R.id.editor_frame, currentFragment);
        trans.commit();
    }

    public void handleTypeChange(View view) {
        if (isTimeSensitive != (view.getId() == R.id.change_to_date)) throw new AssertionError("This view cannot be changed because it is already of that type!");

        isTimeSensitive = !isTimeSensitive;
        view.setVisibility(View.GONE);
        setFragment();
    }

    //TODO: set the reminder info on the countdown
    public void handleSubmitChangesButtonClick(View view){
        Countdown countdown = null;
        //true if we have all the information needed to created a countdown object
        CountdownCreationError creationError = null;
        //Date Countdown
        if(!isTimeSensitive){
            String name = ((EditText)findViewById(R.id.name_date_text)).getText().toString();
            String dateString = ((EditText)findViewById(R.id.date_date_text)).getText().toString();
            if(name.equals("") || dateString.equals("")) creationError = CountdownCreationError.MISSING_VALUES;


            if(creationError == null) {
                LocalDate date = LocalDate.parse(dateString, dateFormatter);
                countdown = new Countdown(name, date);
            }
        }
        //Time Countdown
        else{
            String name = ((EditText)findViewById(R.id.name_time_text)).getText().toString();
            String timeString = ((EditText)findViewById(R.id.time_text)).getText().toString();
            String dateString = ((EditText)findViewById(R.id.time_date_text)).getText().toString();
            if(name.equals("") || timeString.equals("") || dateString.equals("")) creationError = CountdownCreationError.MISSING_VALUES;

            if(creationError == null) {
                LocalTime time = LocalTime.parse(timeString, timeFormatter);
                LocalDate date = LocalDate.parse(dateString, dateFormatter);

                countdown = new Countdown(name, date, time);
            }
        }

        //we only check for one error at a time
        if(creationError == null && countdown.getDateTime().isBefore(LocalDateTime.now())) creationError = CountdownCreationError.PAST_TIME;

        if(creationError == null) {
            //remove the old countdown and add the new one
            Countdowns.getInstance().remove(this.countdown);
            Countdowns.getInstance().add(countdown);
            //start the main activity again
            finish();
        }
        else{
            Toast toast = Toast.makeText(this, creationError.getErrorMessage(), Toast.LENGTH_SHORT);
            TextView v = toast.getView().findViewById(android.R.id.message);
            if( v != null) v.setGravity(Gravity.CENTER);
            toast.show();
        }
    }

    public void deleteCountdown(View view){
        new AlertDialog.Builder(this)
                .setMessage("Delete this countdown?")
                .setPositiveButton("OK", (dialog, which) -> {
                    Countdowns.getInstance().remove(this.countdown);
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return false;
    }

    /*@Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage("Discard Changes?")
                .setPositiveButton("OK", (dialog, which) -> super.onBackPressed())
                .setNegativeButton("Cancel", null)
                .show();
    }*/
}