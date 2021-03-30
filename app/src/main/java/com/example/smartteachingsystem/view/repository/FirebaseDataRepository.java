package com.example.smartteachingsystem.view.repository;

import android.graphics.Bitmap;

import com.example.smartteachingsystem.view.dataSource.FirebaseDataSource;
import com.example.smartteachingsystem.view.model.Response;
import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.model.StudentApp;
import com.example.smartteachingsystem.view.model.Teacher;
import com.example.smartteachingsystem.view.model.TeacherApp;
import com.example.smartteachingsystem.view.model.Teacher_List;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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

    // **** Retrieve teacher list here.

    public Flowable<FirestoreRecyclerOptions<Teacher_List>> getTeacher(){
        return firebaseDataSource.getTeacher();
    }

    public FirestoreRecyclerOptions<Teacher_List> getTeacherList(){
         return firebaseDataSource.getAllTeacher();
    }

    // get student Appointment.......

    public FirestoreRecyclerOptions<TeacherApp> getAllStudentAppointment(){
        return firebaseDataSource.getAllStudentAppointment();
    }

    // get teacher appointment....
    public FirestoreRecyclerOptions<StudentApp> getAllTeacherAppointment(){
        return firebaseDataSource.getAllTeacherAppointment();
    }


    // save appointment when student request for appointment.
    // Appointment save in both teacher & student directory..
    public Completable setStudentAppointment(TeacherApp teacherApp){
         return firebaseDataSource.setStudentAppointment(teacherApp);
    }

    // save teacher response. This response save in both teacher & student directory...
    public Completable setTeacherResponse(Response response){
         return firebaseDataSource.setTeacherResponse(response);
    }

}
