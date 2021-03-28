package com.example.smartteachingsystem.view.di.teacherHome;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.view.adapter.StudentAppointmentAdapter;
import com.example.smartteachingsystem.view.adapter.TeacherAppointmentAdapter;
import com.example.smartteachingsystem.view.model.StudentApp;
import com.example.smartteachingsystem.view.model.TeacherApp;
import com.example.smartteachingsystem.view.repository.FirebaseDataRepository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import dagger.Module;
import dagger.Provides;

@Module
public class TeacherHomeModule {


    @Provides
    static FirestoreRecyclerOptions<StudentApp> provideTeacherAppointmentList(FirebaseDataRepository firebaseDataRepository){
        return firebaseDataRepository.getAllTeacherAppointment();
    }

    @Provides
    static TeacherAppointmentAdapter provideTeacherAppointmentAdapter(FirestoreRecyclerOptions<StudentApp> options, RequestManager requestManager){
        return new TeacherAppointmentAdapter(options,requestManager);
    }
}
