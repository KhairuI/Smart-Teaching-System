package com.example.smartteachingsystem.view.repository;

import android.graphics.Bitmap;

import com.example.smartteachingsystem.view.dataSource.FirebaseDataSource;
import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.model.Teacher;
import com.google.firebase.firestore.DocumentSnapshot;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class FirebaseDataRepository {
    private static final String TAG = "FirebaseDataRepository";
     FirebaseDataSource firebaseDataSource;

     @Inject
    public FirebaseDataRepository(FirebaseDataSource firebaseDataSource) {
        this.firebaseDataSource = firebaseDataSource;
    }

    // set student data...
    public Completable setStudentData(Student student, Bitmap bitmap){
         return firebaseDataSource.setStudentData(student, bitmap);
    }

    // set Teacher data...
    public Completable setTeacherData(Teacher teacher, Bitmap bitmap){
        return firebaseDataSource.setTeacherData(teacher, bitmap);
    }

    // retrieve teacher profile data

    public Flowable<DocumentSnapshot> getTeacherInfo(){
         return firebaseDataSource.getTeacherInfo();
    }

    // retrieve student profile data
    public Flowable<DocumentSnapshot> getStudentInfo(){
        return firebaseDataSource.getStudentInfo();
    }

}
