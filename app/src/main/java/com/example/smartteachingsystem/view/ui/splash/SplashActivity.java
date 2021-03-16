package com.example.smartteachingsystem.view.ui.splash;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.smartteachingsystem.view.repository.AuthRepository;
import com.example.smartteachingsystem.view.ui.login.LoginActivity;
import com.example.smartteachingsystem.view.ui.register.RegisterActivity;
import com.example.smartteachingsystem.view.ui.studentProfile.StudentProfile;
import com.example.smartteachingsystem.view.ui.teacherProfile.TeacherProfile;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class SplashActivity extends DaggerAppCompatActivity {


    // Dependency Injection
    @Inject
    ViewModelProviderFactory modelProviderFactory;

    @Inject
    AuthRepository authRepository;

    private SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashViewModel= new ViewModelProvider(getViewModelStore(),modelProviderFactory).get(SplashViewModel.class);
        splashViewModel.getUserRole(authRepository.getCurrentUid());

        if(authRepository.getCurrentUser() != null){
            observeRole();

        }
        else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void observeRole() {
        splashViewModel.observedRole().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                switch (s) {
                    case "null":
                        goToRegisterActivity();
                        break;
                    case "teacher":
                        goToTeacherActivity();

                        break;
                    case "student":
                        goToStudentActivity();
                        break;
                }
            }
        });
    }

    private void goToStudentActivity() {
        startActivity(new Intent(this, StudentProfile.class));
        finish();
    }

    private void goToTeacherActivity() {
        startActivity(new Intent(this, TeacherProfile.class));
        finish();
    }

    private void goToRegisterActivity() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }
}