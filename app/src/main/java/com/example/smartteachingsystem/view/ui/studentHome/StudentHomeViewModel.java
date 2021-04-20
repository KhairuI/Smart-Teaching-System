package com.example.smartteachingsystem.view.ui.studentHome;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.model.TeacherApp;
import com.example.smartteachingsystem.view.repository.AuthRepository;
import com.example.smartteachingsystem.view.repository.FirebaseDataRepository;
import com.example.smartteachingsystem.view.utils.Resource;
import com.example.smartteachingsystem.view.utils.StateResource;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StudentHomeViewModel extends ViewModel {
    private static final String TAG="StudentProfileViewModel";
    private FirebaseDataRepository firebaseDataRepository;
    private AuthRepository authRepository;

    private final MediatorLiveData<Student> onStudentProfileInfo = new MediatorLiveData<>();
    private final MediatorLiveData<List<TeacherApp>> onStudent = new MediatorLiveData<>();
    private final MediatorLiveData<StateResource> onStudentDelete = new MediatorLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public StudentHomeViewModel(FirebaseDataRepository firebaseDataRepository, AuthRepository authRepository) {
        Log.d(TAG, "StudentProfileViewModel: is working...");
        this.firebaseDataRepository= firebaseDataRepository;
        this.authRepository= authRepository;
    }

    // student Appointment delete .....

    public void studentDelete(String pushKey){
        firebaseDataRepository.studentDelete(pushKey)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                onStudentDelete.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {
                onStudentDelete.setValue(StateResource.success());

            }

            @Override
            public void onError(@NonNull Throwable e) {

                onStudentDelete.setValue(StateResource.error(e.toString()));
            }
        });

    }


    // set student Token.....
    public void setToken(String token){
        firebaseDataRepository.setToken(token);
    }

    public void getStudentInfo(){
        firebaseDataRepository.getStudentInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).toObservable().subscribe(new Observer<DocumentSnapshot>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(@NonNull DocumentSnapshot documentSnapshot) {
                Student student= documentSnapshot.toObject(Student.class);
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

    public void getStudent(){
        firebaseDataRepository.getStudent().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .toObservable().subscribe(new Observer<List<TeacherApp>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(@NonNull List<TeacherApp> teacherApps) {

                onStudent.setValue(teacherApps);
            }

            @Override
            public void onError(@NonNull Throwable e) {


            }

            @Override
            public void onComplete() {


            }
        });
    }

    public LiveData<Student> observeStudentInfo(){
        return onStudentProfileInfo;
    }
    public LiveData<List<TeacherApp>> observeStudent(){
        return onStudent;
    }

    // student delete observer.....

    public LiveData<StateResource> observeStudentDelete(){
        return onStudentDelete;
    }



    // logout
    public void logout(){
        authRepository.logOut();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
