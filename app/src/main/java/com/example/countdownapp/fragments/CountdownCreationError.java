package com.example.countdownapp.fragments;

import androidx.annotation.StringRes;

import com.example.countdownapp.R;

public enum CountdownCreationError {
    MISSING_VALUES(R.string.countdown_missing_value),
    PAST_TIME(R.string.countdown_past);

    private @StringRes int errorMessageId;
    CountdownCreationError(@StringRes int stringId){
        errorMessageId = stringId;
    }

    public @StringRes int getErrorMessage(){
        return errorMessageId;
    }
}
