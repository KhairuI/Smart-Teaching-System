package com.example.smartteachingsystem.view.di.teacherAppointment;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.studentAppointment.StudentAppointmentViewModel;
import com.example.smartteachingsystem.view.ui.teacherAppointment.TeacherAppointmentViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class TeacherAppViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TeacherAppointmentViewModel.class)
    public abstract ViewModel bindTeacherAppointmentViewModel(TeacherAppointmentViewModel viewModel);

}
