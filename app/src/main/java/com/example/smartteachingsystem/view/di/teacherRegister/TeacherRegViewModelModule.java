package com.example.smartteachingsystem.view.di.teacherRegister;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.studentRegister.StudentRegisterViewModel;
import com.example.smartteachingsystem.view.ui.teacherRegister.TeacherRegisterViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class TeacherRegViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TeacherRegisterViewModel.class)
    public abstract ViewModel bindTeacherRegisterViewModel(TeacherRegisterViewModel viewModel);

}
