package com.example.smartteachingsystem.view.di.studentRegister;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.splash.SplashViewModel;
import com.example.smartteachingsystem.view.ui.studentRegister.StudentRegisterViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class StudentRegViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(StudentRegisterViewModel.class)
    public abstract ViewModel bindStudentRegisterViewModel(StudentRegisterViewModel viewModel);


}
