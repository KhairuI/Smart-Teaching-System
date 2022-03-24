package com.example.smartteachingsystem.view.repository;

import android.graphics.Bitmap;

import com.example.smartteachingsystem.view.dataSource.FirebaseDataSource;
import com.example.smartteachingsystem.view.model.ChatUser;
import com.example.smartteachingsystem.view.model.Message;
import com.example.smartteachingsystem.view.model.StudentChatUser;
import com.example.smartteachingsystem.view.model.StudentMessage;
import com.example.smartteachingsystem.view.model.Note;
import com.example.smartteachingsystem.view.model.Response;
import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.model.StudentApp;
import com.example.smartteachingsystem.view.model.Teacher;
import com.example.smartteachingsystem.view.model.TeacherApp;
import com.example.smartteachingsystem.view.model.TeacherChatUser;
import com.example.smartteachingsystem.view.model.TeacherMessage;
import com.example.smartteachingsystem.view.model.Teacher_List;
import com.example.smartteachingsystem.view.model.Token;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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


    // get student Chat history
    public Flowable<List<StudentChatUser>> getHistoryList(){
         return firebaseDataSource.getHistoryList();
    }

    // get teacher Chat history
    public Flowable<List<TeacherChatUser>> getTeacherHistoryList(){
        return firebaseDataSource.getTeacherHistoryList();
    }

    // get message list....
    public Flowable<QuerySnapshot> getMessageList(String uId){
        return firebaseDataSource.getMessageList(uId);
    }

    //send message from student site
    public Completable sendStudentMessage(Message message,StudentChatUser studentChatUser, TeacherChatUser teacherChatUser){
         return firebaseDataSource.sendStudentMessage(message, studentChatUser, teacherChatUser);
    }

    //send message from teacher site
    public Completable sendTeacherMessage(Message message, TeacherChatUser teacherChatUser, StudentChatUser studentChatUser){
        return firebaseDataSource.sendTeacherMessage(message, teacherChatUser,studentChatUser);
    }


    // delete note....
    public Completable noteDelete(String pushKey){
         return firebaseDataSource.noteDelete(pushKey);
    }
    // insert note......
    public Completable insertNote(Note note){
         return firebaseDataSource.insertNote(note);
    }
    // get all note
    public Flowable<List<Note> > getAllNote(){
         return firebaseDataSource.getAllNote();
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

    // retrieve teacher profile data in chatting activity
    public Flowable<DocumentSnapshot> getTeacherInfoInChatting(String uId){
        return firebaseDataSource.getTeacherInfoInChatting(uId);
    }


    // retrieve teacher profile data

    public Flowable<DocumentSnapshot> getTeacherInfo(){
         return firebaseDataSource.getTeacherInfo();
    }

    // retrieve student profile data
    public Flowable<DocumentSnapshot> getStudentInfo(){
        return firebaseDataSource.getStudentInfo();
    }

    // get student chatting data
    public Flowable<DocumentSnapshot> getStudentChattingInfo(String uId){
        return firebaseDataSource.getStudentChattingInfo(uId);
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

    // chat history delete......
    public Completable chatDelete(String uid){
        return firebaseDataSource.chatDelete(uid);
    }


}
