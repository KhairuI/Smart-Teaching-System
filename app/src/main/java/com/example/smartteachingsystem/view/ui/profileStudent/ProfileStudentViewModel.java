package com.example.smartteachingsystem.view.ui.profileStudent;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class ProfileStudentViewModel extends ViewModel {

    private static final String TAG = "ProfileStudentViewModel";

    @Inject
    public ProfileStudentViewModel() {
        Log.d(TAG, "ProfileStudentViewModel: is working.....");
    }
}
