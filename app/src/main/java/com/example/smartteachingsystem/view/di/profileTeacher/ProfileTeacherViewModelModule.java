package com.example.smartteachingsystem.view.di.profileTeacher;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.profileTeacher.ProfileTeacherViewModel;
import com.example.smartteachingsystem.view.ui.teacherHome.TeacherHomeViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ProfileTeacherViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TeacherHomeViewModel.class)
    public abstract ViewModel bindProfileTeacherViewModel(TeacherHomeViewModel viewModel);
}
