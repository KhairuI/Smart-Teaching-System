package com.example.smartteachingsystem.view.di.teacherProfile;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.studentProfile.StudentProfileViewModel;
import com.example.smartteachingsystem.view.ui.teacherProfile.TeacherProfileViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class TeacherProfileViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TeacherProfileViewModel.class)
    public abstract ViewModel bindTeacherProfileViewModel(TeacherProfileViewModel viewModel);
}
