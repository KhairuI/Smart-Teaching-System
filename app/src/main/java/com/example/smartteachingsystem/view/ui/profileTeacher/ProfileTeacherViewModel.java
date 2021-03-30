package com.example.smartteachingsystem.view.ui.profileTeacher;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class ProfileTeacherViewModel extends ViewModel {
    private static final String TAG = "ProfileTeacherViewModel";

    @Inject
    public ProfileTeacherViewModel() {
        Log.d(TAG, "ProfileTeacherViewModel: is working....");
    }
}
