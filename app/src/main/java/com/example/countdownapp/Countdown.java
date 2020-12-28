package com.example.countdownapp;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Countdown {
    public static final String TAG = "Countdown";

    //TODO: add a settings page to change between military and standard time??
    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm", Locale.US);
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");

    //TODO: add fields for reminder info
    public String name;
    public LocalDate date;
    public LocalTime time;
    public boolean isTimeSensitive;
    public boolean isFinished;

    /**Copies the countdown object*/
    public Countdown(Countdown countdown){
        this.name = countdown.name;
        this.date = countdown.date;
        this.time = countdown.time;
        this.isTimeSensitive = countdown.isTimeSensitive;
        this.isFinished = countdown.isFinished;
    }
    public Countdown(String name, LocalDate date){
        this.name = name;
        this.date = date;
        this.isTimeSensitive = false;
        this.isFinished = date.isBefore(LocalDate.now());
    }
    public Countdown(String name, LocalDate date, LocalTime time){
        this.name = name;
        this.date = date;
        this.time = time;
        this.isTimeSensitive = true;
        this.isFinished = getDateTime().isBefore(LocalDateTime.now());
    }

    public LocalDateTime getDateTime(){
        if(isTimeSensitive) return LocalDateTime.of(date, time);
        else return LocalDateTime.of(date, LocalTime.now());
    }

    public String toStorageString(){
        if(isTimeSensitive) return name+";"+date.format(dateFormatter)+";"+time.format(timeFormatter);
        return name+";"+date.format(dateFormatter)+";";
    }
    public static Countdown fromStorageString(String string){
        String[] parts = string.split(";");
        String name = parts[0];
        LocalDate date = LocalDate.parse(parts[1], dateFormatter);
        boolean isTimeSensitive = parts.length == 3;
        if(isTimeSensitive){
            LocalTime time = LocalTime.parse(parts[2], timeFormatter);
            return new Countdown(name, date, time);
        }
        else if(parts.length == 2){
            return new Countdown(name, date);
        }
        else{
            throw new IllegalArgumentException("The string '"+string+"' doesn't match the format for a countdown storage string!");
        }
    }
}
