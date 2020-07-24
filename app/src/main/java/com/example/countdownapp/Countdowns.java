package com.example.countdownapp;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Countdowns extends ArrayList<Countdown> {

    public static final String intentName = "index";

    private static Countdowns theInstance = new Countdowns();
    public static Countdowns getInstance() {return theInstance;}
    private Countdowns(){}

    public int getFirstFutureCountdownIndex(){
        int index = -1;
        for(int i = 0; i < this.size(); i++){
            if(!this.get(i).isFinished){
                index = i;
                break;
            }
        }
        return index;
    }

    public String toStorageString(){
        StringBuilder builder = new StringBuilder();
        for(Countdown c : this){
            builder.append(c.toStorageString()).append("\n");
        }
        if(builder.length()==0) return builder.toString();
        return builder.toString().substring(0, builder.length()-1);
    }
    public void fromStorageString(String string){
        if(string.equals("")) return;
        String[] countdownStrings = string.split("\n");
        for(String countdownString : countdownStrings){
            addNoSort(Countdown.fromStorageString(countdownString));
        }
        sort();
    }

    private void addNoSort(Countdown countdown){
        super.add(countdown);
    }

    @Override
    public boolean add(Countdown countdown){
        boolean changed = super.add(countdown);
        if(changed) sort();
        return changed;
    }
    @Override
    public void add(int index, Countdown countdown){
        super.add(index, countdown);
        sort();
    }
    @Override
    public boolean addAll(@NonNull Collection<? extends Countdown> countdowns){
        boolean changed = super.addAll(countdowns);
        if(changed) sort();
        return changed;
    }
    @Override
    public boolean addAll(int index, @NonNull Collection<? extends Countdown> countdowns){
        boolean changed = super.addAll(index, countdowns);
        if(changed) sort();
        return changed;
    }

    @Override
    public boolean remove(Object o){
        boolean changed = super.remove(o);
        if(changed) sort();
        return changed;
    }
    @Override
    public boolean removeAll(@NonNull Collection<?> c){
        boolean changed = super.removeAll(c);
        if(changed) sort();
        return changed;
    }

    private void sort(){
        Collections.sort(this, (o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime()));
    }
}
