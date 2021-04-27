package com.example.smartteachingsystem.view.di.profileStudent;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.profileStudent.ProfileStudentViewModel;
import com.example.smartteachingsystem.view.ui.studentHome.StudentHomeViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ProfileStudentViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(StudentHomeViewModel.class)
    public abstract ViewModel bindProfileStudentViewModel(StudentHomeViewModel viewModel);
}
