package com.example.smartteachingsystem.view.di.teacherNote;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.teacherNote.TeacherNoteViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class TeacherNoteViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TeacherNoteViewModel.class)
    public abstract ViewModel bindTeacherNoteViewModel(TeacherNoteViewModel viewModel);
}
