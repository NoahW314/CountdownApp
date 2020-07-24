package com.example.countdownapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@SuppressLint("ViewConstructor")
public class CountdownView extends FrameLayout {
    public CountdownView(Context context, Countdown countdown) {
        super(context);
        inflate(context, R.layout.countdown_view, this);

        TextView name = findViewById(R.id.name);
        TextView end = findViewById(R.id.end);
        TextView time = findViewById(R.id.time);

        name.setText(countdown.name);
        end.setText(countdown.isTimeSensitive ? formatEndDateTime(countdown) : formatEndDate(countdown));

        startTimer(countdown, time, name, end, countdown.isTimeSensitive);

        setClickable(true);
        setOnClickListener(v -> {
            Intent intent = new Intent(context, CountdownEditorActivity.class);
            intent.putExtra(Countdowns.intentName, Countdowns.getInstance().indexOf(countdown));
            context.startActivity(intent);
        });
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        setBackgroundResource(outValue.resourceId);
    }

    private String formatEndDate(Countdown countdown){
        return countdown.date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US));
    }
    private String formatEndDateTime(Countdown countdown){
        return countdown.getDateTime().format(DateTimeFormatter.ofPattern("MMMM d, yyyy, h:mm a", Locale.US));
    }

    private String formatDateTimeDiff(long millisLeft){
        int daysLeft = (int)Math.floor(millisLeft/(1000f*60*60*24));
        int hoursLeft = (int)Math.floor(millisLeft/(1000f*60*60)-daysLeft*24);
        int minutesLeft = (int)Math.ceil((float)millisLeft/(1000*60)-hoursLeft*60-daysLeft*24*60);

        if(minutesLeft == 60){minutesLeft = 0; hoursLeft++;}
        if(hoursLeft == 24){hoursLeft = 0; daysLeft++;}

        StringBuilder builder = new StringBuilder();

        if(daysLeft == 1) builder.append("1 Day, ");
        else if(daysLeft != 0) builder.append(daysLeft).append(" Days, ");

        if(hoursLeft == 1) builder.append("1 Hour, ");
        else if(hoursLeft != 0) builder.append(hoursLeft).append(" Hours, ");

        if(minutesLeft == 1) builder.append("1 Minute, ");
        else if(minutesLeft != 0) builder.append(minutesLeft).append(" Minutes, ");

        builder.delete(builder.length()-2, builder.length());
        builder.append(" Away");
        return builder.toString();
    }
    private String formatDateDiff(long millisLeft){
        int daysLeft = (int)Math.ceil(millisLeft/(1000f*60*60*24));
        if(daysLeft == 1) return "1 Day Away";
        return daysLeft+" Days Away";
    }

    private void startTimer(final Countdown countdown, final TextView time, final TextView name, final TextView end, final boolean isTime){
        Duration duration = Duration.between(LocalDateTime.now(), countdown.getDateTime());
        CountDownTimer timer = new CountDownTimer(duration.toMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(isTime){
                    time.setText(formatDateTimeDiff(millisUntilFinished));
                }
                else time.setText(formatDateDiff(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                time.setText("");
                name.setTextColor(0xFF_FF_00_00);
                end.setTextColor(0xFF_FF_00_00);

                countdown.isFinished = true;
            }
        };
        timer.start();
    }
}
