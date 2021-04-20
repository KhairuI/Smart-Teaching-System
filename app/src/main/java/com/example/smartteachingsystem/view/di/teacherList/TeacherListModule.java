package com.example.smartteachingsystem.view.di.teacherList;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.view.adapter.AllTeacherAdapter;
import com.example.smartteachingsystem.view.adapter.TeacherAdapter;
import com.example.smartteachingsystem.view.model.Teacher_List;
import com.example.smartteachingsystem.view.repository.FirebaseDataRepository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import dagger.Module;
import dagger.Provides;

@Module
public class TeacherListModule {

    @Provides
    static FirestoreRecyclerOptions<Teacher_List> provideList(FirebaseDataRepository firebaseDataRepository){
        return firebaseDataRepository.getTeacherList();
    }

    @Provides
    static TeacherAdapter provideAdapter(FirestoreRecyclerOptions<Teacher_List> options, RequestManager requestManager){
        return new TeacherAdapter(options,requestManager);
    }

    @Provides
    static AllTeacherAdapter provideAllTeacherAdapter(RequestManager requestManager){
        return new AllTeacherAdapter(requestManager);
    }

}
