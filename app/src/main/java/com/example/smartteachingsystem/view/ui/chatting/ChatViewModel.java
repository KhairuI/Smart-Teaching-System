package com.example.smartteachingsystem.view.ui.chatting;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.model.ChatUser;
import com.example.smartteachingsystem.view.model.Message;
import com.example.smartteachingsystem.view.model.StudentChatUser;
import com.example.smartteachingsystem.view.model.StudentMessage;
import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.model.TeacherChatUser;
import com.example.smartteachingsystem.view.model.TeacherMessage;
import com.example.smartteachingsystem.view.model.Teacher_List;
import com.example.smartteachingsystem.view.repository.FirebaseDataRepository;
import com.example.smartteachingsystem.view.utils.StateResource;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChatViewModel extends ViewModel {
    private static final String TAG = "ChatViewModel";
    private final FirebaseDataRepository firebaseDataRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MediatorLiveData<Teacher_List> onTeacherProfileInfo = new MediatorLiveData<>();
    private final MediatorLiveData<Student> onStudentProfileInfo = new MediatorLiveData<>();
    private final MediatorLiveData<List<Message>> getAllMessage = new MediatorLiveData<>();
    private final MediatorLiveData<List<StudentChatUser>> getAllHistory = new MediatorLiveData<>();
    private final MediatorLiveData<List<TeacherChatUser>> getAllTeacherHistory = new MediatorLiveData<>();
    private final MediatorLiveData<Message> getNewMessage = new MediatorLiveData<>();
    private final MediatorLiveData<StateResource> chatDelete = new MediatorLiveData<>();
    private int counter = 0;
    private final List<Message> studentMessageList = new ArrayList<>();

    @Inject
    public ChatViewModel(FirebaseDataRepository firebaseDataRepository) {
        this.firebaseDataRepository= firebaseDataRepository;
        Log.d(TAG, "ChatViewModel: is working...");
    }

    // student Appointment delete .....

    public void chatDelete(String uId){
        firebaseDataRepository.chatDelete(uId)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                chatDelete.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {
                chatDelete.setValue(StateResource.success());

            }

            @Override
            public void onError(@NonNull Throwable e) {

                chatDelete.setValue(StateResource.error(e.toString()));
            }
        });

    }

    // get student chat history
    public void getHistoryList(){
        firebaseDataRepository.getHistoryList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).toObservable()
                .subscribe(new Observer<List<StudentChatUser>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<StudentChatUser> chatUsers) {
                        getAllHistory.setValue(chatUsers);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    // get teacher chat history
    public void getTeacherHistoryList(){
        firebaseDataRepository.getTeacherHistoryList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).toObservable()
                .subscribe(new Observer<List<TeacherChatUser>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<TeacherChatUser> chatUsers) {
                        getAllTeacherHistory.setValue(chatUsers);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    // get all message list
    public void getMessageList(String uId){
        firebaseDataRepository.getMessageList(uId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).toObservable()
                .subscribe(new Observer<QuerySnapshot>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull QuerySnapshot queryDocumentSnapshots) {

                        if(queryDocumentSnapshots != null){
                            for(DocumentChange dc: queryDocumentSnapshots.getDocumentChanges()){
                                if(dc.getType() == DocumentChange.Type.ADDED){
                                    if(counter==0){
                                        Message message = dc.getDocument().toObject(Message.class);
                                        studentMessageList.add(message);
                                    }
                                    else {
                                        Message message = dc.getDocument().toObject(Message.class);
                                        getNewMessage.setValue(message);
                                    }
                                }
                            }
                            if(counter==0){
                                getAllMessage.setValue(studentMessageList);
                                counter++;
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    // retrieve teacher profile data in chatting activity
    public void getTeacherInfoInChatting(String uId){
        firebaseDataRepository.getTeacherInfoInChatting(uId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .toObservable().subscribe(new Observer<DocumentSnapshot>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(@NonNull DocumentSnapshot documentSnapshot) {

                Teacher_List teacherList= documentSnapshot.toObject(Teacher_List.class);
                onTeacherProfileInfo.setValue(teacherList);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    // retrieve student profile data in chatting activity
    public void getStudentInfoInChatting(String uId){
        firebaseDataRepository.getStudentChattingInfo(uId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .toObservable().subscribe(new Observer<DocumentSnapshot>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(@NonNull DocumentSnapshot documentSnapshot) {

                Student student = documentSnapshot.toObject(Student.class);
                onStudentProfileInfo.setValue(student);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    // send message from student site.....
    public void sendStudentMessage(Message message,StudentChatUser studentChatUser, TeacherChatUser teacherChatUser){
        firebaseDataRepository.sendStudentMessage(message, studentChatUser, teacherChatUser).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG, "onComplete: message send successfully");

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError:"+e.toString());
                    }
                });
    }

    // send message from teacher site.....
    public void sendTeacherMessage(Message message, TeacherChatUser teacherChatUser, StudentChatUser studentChatUser){
        firebaseDataRepository.sendTeacherMessage(message,teacherChatUser, studentChatUser).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG, "onComplete: message send successfully");

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError:"+e.toString());
                    }
                });
    }

    // observe teacher info in chatting activity
    public LiveData<Teacher_List> observeTeacherInfo(){
        return onTeacherProfileInfo;
    }

    // observe student info in chatting activity
    public LiveData<Student> observeStudentInfo(){
        return onStudentProfileInfo;
    }

    // observe all message
    public LiveData<List<Message>> observeAllMessage(){
        return getAllMessage;
    }

    // observe all message
    public LiveData<Message> observeNewMessage(){
        return getNewMessage;
    }

    // observe student chat history list
    public LiveData<List<StudentChatUser>> observeChatHistory(){
        return getAllHistory;
    }

    // observe student chat history list
    public LiveData<List<TeacherChatUser>> observeTeacherChatHistory(){
        return getAllTeacherHistory;
    }

    // chat delete
    public LiveData<StateResource> observeChatDelete(){
        return chatDelete;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
