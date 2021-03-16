package com.example.smartteachingsystem.view.di.studentProfile;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.studentProfile.StudentProfileViewModel;
import com.example.smartteachingsystem.view.ui.studentRegister.StudentRegisterViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class StudentProfileViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(StudentProfileViewModel.class)
    public abstract ViewModel bindStudentProfileViewModel(StudentProfileViewModel viewModel);
}
