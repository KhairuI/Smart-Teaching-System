package com.example.smartteachingsystem.view.di.studentHome;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.view.adapter.StudentAppAdapter;
import com.example.smartteachingsystem.view.adapter.StudentAppointmentAdapter;
import com.example.smartteachingsystem.view.model.TeacherApp;
import com.example.smartteachingsystem.view.repository.FirebaseDataRepository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import dagger.Module;
import dagger.Provides;

@Module
public class StudentHomeModule {

    @Provides
    static FirestoreRecyclerOptions<TeacherApp> provideStudentAppointmentList(FirebaseDataRepository firebaseDataRepository){
        return firebaseDataRepository.getAllStudentAppointment();
    }

    @Provides
    static StudentAppointmentAdapter provideStudentAppointmentAdapter(FirestoreRecyclerOptions<TeacherApp> options, RequestManager requestManager){
        return new StudentAppointmentAdapter(options,requestManager);
    }

    @Provides
    static StudentAppAdapter provideStudentAppAdapter(RequestManager requestManager){
        return new StudentAppAdapter(requestManager);
    }

}
