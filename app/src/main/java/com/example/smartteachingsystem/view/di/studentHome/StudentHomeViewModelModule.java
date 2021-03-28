package com.example.smartteachingsystem.view.di.studentHome;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.studentHome.StudentHomeViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class StudentHomeViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(StudentHomeViewModel.class)
    public abstract ViewModel bindStudentHomeViewModel(StudentHomeViewModel viewModel);
}
