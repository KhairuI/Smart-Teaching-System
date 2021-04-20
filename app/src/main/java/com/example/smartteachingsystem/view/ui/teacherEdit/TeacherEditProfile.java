package com.example.smartteachingsystem.view.ui.teacherEdit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.ui.studentEdit.StudentEditViewModel;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class TeacherEditProfile extends DaggerAppCompatActivity {


    private TeacherEditViewModel viewModel;

    // Dependency Injection
    @Inject
    RequestManager requestManager;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_edit_profile);

        viewModel = new ViewModelProvider(getViewModelStore(),providerFactory)
                .get(TeacherEditViewModel.class);
    }
}