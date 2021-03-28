package com.example.smartteachingsystem.view.di.teacherList;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.splash.SplashViewModel;
import com.example.smartteachingsystem.view.ui.teacherList.TeacherListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class TeacherListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TeacherListViewModel.class)
    public abstract ViewModel bindTeacherListViewModel(TeacherListViewModel viewModel);
}
