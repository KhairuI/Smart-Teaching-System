package com.example.smartteachingsystem.view.di.studentEdit;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.studentEdit.StudentEditViewModel;
import com.example.smartteachingsystem.view.ui.studentRegister.StudentRegisterViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class StudentEditViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(StudentEditViewModel.class)
    public abstract ViewModel bindStudentEditViewModel(StudentEditViewModel viewModel);
}
