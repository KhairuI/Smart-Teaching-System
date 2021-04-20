package com.example.smartteachingsystem.view.di.teacherEdit;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.studentEdit.StudentEditViewModel;
import com.example.smartteachingsystem.view.ui.teacherEdit.TeacherEditViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class TeacherEditViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TeacherEditViewModel.class)
    public abstract ViewModel bindTeacherEditViewModel(TeacherEditViewModel viewModel);
}
