package com.example.countdownapp;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.countdownapp.fragments.CountdownCreationError;
import com.example.countdownapp.fragments.CountdownFragmentStateAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.example.countdownapp.Countdown.dateFormatter;
import static com.example.countdownapp.Countdown.timeFormatter;
import static com.example.countdownapp.TimerCreatorActivity.TimerPage.DATE;
import static com.example.countdownapp.TimerCreatorActivity.TimerPage.TIME;

public class TimerCreatorActivity extends AppCompatActivity {
    public static final String TAG = "TimerCreatorTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timer_creator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setDisplayHomeAsUpEnabled(true);

        ViewPager2 viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new CountdownFragmentStateAdapter(this));
        TabLayout tabLayout = findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.select();
            if (position == DATE.id) tab.setText(R.string.date);
            if (position == TIME.id) tab.setText(R.string.time);
        }).attach();
    }

    public void handleSubmitButtonClick(View view){
        ViewPager2 pager = findViewById(R.id.pager);
        Countdown countdown = null;
        //true if we have all the information needed to created a countdown object
        CountdownCreationError creationError = null;
        //Date Tab
        if(pager.getCurrentItem() == DATE.id){
            String name = ((EditText)findViewById(R.id.name_date_text)).getText().toString();
            if(name.equals("")) creationError = CountdownCreationError.MISSING_VALUES;

            DatePicker datePicker = findViewById(R.id.date_picker);
            LocalDate date = LocalDate.of(datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth());

            countdown = new Countdown(name, date);
        }
        //Time Tab
        else if(pager.getCurrentItem() == TIME.id){
            String name = ((EditText)findViewById(R.id.name_time_text)).getText().toString();
            String timeString = ((EditText)findViewById(R.id.time_text)).getText().toString();
            String dateString = ((EditText)findViewById(R.id.date_text)).getText().toString();
            if(name.equals("") || timeString.equals("") || dateString.equals("")) creationError = CountdownCreationError.MISSING_VALUES;

            if(creationError == null) {
                LocalTime time = LocalTime.parse(timeString, timeFormatter);
                LocalDate date = LocalDate.parse(dateString, dateFormatter);

                countdown = new Countdown(name, date, time);
            }
        }
        else{
            throw new IllegalArgumentException("There is no position "+pager.getCurrentItem()+" for the countdown creator");
        }

        //we only check for one error at a time
        if(creationError == null && countdown.getDateTime().isBefore(LocalDateTime.now())) creationError = CountdownCreationError.PAST_TIME;

        if(creationError == null) {
            Countdowns.getInstance().add(countdown);
            finish();
        }
        else{
            Toast toast = Toast.makeText(this, creationError.getErrorMessage(), Toast.LENGTH_SHORT);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            if( v != null) v.setGravity(Gravity.CENTER);
            toast.show();
        }
    }

    public enum TimerPage{DATE(0), TIME(1);
        public int id;
        TimerPage(int id){
            this.id = id;
        }
    }
}
