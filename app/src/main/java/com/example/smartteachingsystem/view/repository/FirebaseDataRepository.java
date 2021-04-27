package com.example.smartteachingsystem.view.repository;

import android.graphics.Bitmap;

import com.example.smartteachingsystem.view.dataSource.FirebaseDataSource;
import com.example.smartteachingsystem.view.model.Response;
import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.model.StudentApp;
import com.example.smartteachingsystem.view.model.Teacher;
import com.example.smartteachingsystem.view.model.TeacherApp;
import com.example.smartteachingsystem.view.model.Teacher_List;
import com.example.smartteachingsystem.view.model.Token;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

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

    // update teacher counseling...
    public Completable updateCounseling(String s){
         return firebaseDataSource.updateCounseling(s);
    }

    // set Token...
    public void setToken(String token){
         firebaseDataSource.setToken(token);
    }

    // get Token
    public Flowable<Token> getToken(String value){
         return firebaseDataSource.getToken(value);
    }

     // teacher get Student Token.....

    public Flowable<Token> getStudentToken(String value){
         return firebaseDataSource.getStudentToken(value);
    }

    // update student with image
    public Completable updateStudentWithImage(final Student student,final Bitmap bitmap){
         return firebaseDataSource.updateStudentWithImage(student, bitmap);
    }

    // update student without image
    public Completable updateStudentWithoutImage(final Student student){
         return firebaseDataSource.updateStudentWithoutImage(student);
    }

    // update teacher with image
    public Completable updateTeacherWithImage(final Teacher teacher, final Bitmap bitmap){
         return firebaseDataSource.updateTeacherWithImage(teacher, bitmap);
    }

    // update teacher without image
    public Completable updateTeacherWithoutImage(final Teacher teacher){
         return firebaseDataSource.updateTeacherWithoutImage(teacher);
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

    public Flowable<List<Teacher_List>> getTeacher(){
        return firebaseDataSource.getTeacher();
    }

    public FirestoreRecyclerOptions<Teacher_List> getTeacherList(){
         return firebaseDataSource.getAllTeacher();
    }

    // get student Appointment.......

    public FirestoreRecyclerOptions<TeacherApp> getAllStudentAppointment(){
        return firebaseDataSource.getAllStudentAppointment();
    }

    // get student appointment alternatively....
    public Flowable<List<TeacherApp>> getStudent(){
         return firebaseDataSource.getStudent();
    }

    // get teacher appointment alternatively....
    public Flowable<List<StudentApp>> getStudentAppointment(){
        return firebaseDataSource.getStudentAppointment();
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

    // student appointment delete......

    public Completable studentDelete(String pushKey){
         return firebaseDataSource.studentDelete(pushKey);
    }

    // teacher appointment delete......
    public Completable teacherDelete(String pushKey){
        return firebaseDataSource.teacherDelete(pushKey);
    }

}
