package com.example.smartteachingsystem.view.di.studentAppointment;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.studentAppointment.StudentAppointmentViewModel;
import com.example.smartteachingsystem.view.ui.teacherList.TeacherListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class StudentAppViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(StudentAppointmentViewModel.class)
    public abstract ViewModel bindStudentAppointmentViewModel(StudentAppointmentViewModel viewModel);
}
