package com.example.countdownapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

//TODO: Determine how to schedule/send a notification (especially when app is sleeping/dead. How?)
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MyMainActivity";
    private static final String countdownFileName = "countdowns";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startTimerCreatorActivity());

        //we only add the countdowns from storage if we don't have any other countdowns
        if(Countdowns.getInstance().isEmpty()){
            try { setCountdownsFromStorage(); }
            catch(IOException e){ throw new RuntimeException(e); }
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.v(TAG, "onResume");
        ListAdapter adapter = new CountdownAdapter(this, Countdowns.getInstance());
        ListView countdownLayout = findViewById(R.id.countdown_layout);
        countdownLayout.setAdapter(adapter);

        countdownLayout.setSelection(Countdowns.getInstance().getFirstFutureCountdownIndex());
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.v(TAG, "onPause");

        Log.v(TAG, Countdowns.getInstance().toStorageString());
        try{ storeCountdowns(); }
        catch(IOException e){ throw new RuntimeException(e); }
    }

    private void storeCountdowns() throws IOException{
        if(Countdowns.getInstance().isEmpty()) new File(getFilesDir(), countdownFileName).delete();
        FileOutputStream outputStream = openFileOutput(countdownFileName, Context.MODE_PRIVATE);
        outputStream.write(Countdowns.getInstance().toStorageString().getBytes());
        outputStream.close();
    }

    private void setCountdownsFromStorage() throws IOException{
        File file = new File(getFilesDir(), countdownFileName);
        file.createNewFile();
        BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput(file.getName())));
        StringBuilder stringBuilder = new StringBuilder();
        String line = reader.readLine();
        while (line != null && !line.equals("")) {
            stringBuilder.append(line).append('\n');
            line = reader.readLine();
        }

        Countdowns.getInstance().fromStorageString(stringBuilder.toString());
    }

    private void startTimerCreatorActivity(){
        Intent intent = new Intent(this, TimerCreatorActivity.class);
        startActivity(intent);
    }
}
