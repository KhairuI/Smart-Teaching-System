package com.example.smartteachingsystem.view.di.teacherNote;

import com.example.smartteachingsystem.view.adapter.NoteAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class TeacherNoteModule {

    @Provides
    static NoteAdapter provideNoteAdapter(){
        return new NoteAdapter();
    }
}
