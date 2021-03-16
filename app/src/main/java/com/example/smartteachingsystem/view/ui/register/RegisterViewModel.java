package com.example.smartteachingsystem.view.ui.register;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class RegisterViewModel extends ViewModel {
    private static final String TAG = "RegisterViewModel";

    @Inject
    public RegisterViewModel() {
        Log.d(TAG, "RegisterViewModel: is working");
    }

}
