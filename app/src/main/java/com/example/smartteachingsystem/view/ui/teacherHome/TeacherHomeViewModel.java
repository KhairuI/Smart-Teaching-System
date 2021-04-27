package com.example.smartteachingsystem.view.ui.teacherHome;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.model.StudentApp;
import com.example.smartteachingsystem.view.model.Teacher;
import com.example.smartteachingsystem.view.model.TeacherApp;
import com.example.smartteachingsystem.view.repository.AuthRepository;
import com.example.smartteachingsystem.view.repository.FirebaseDataRepository;
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

public class TeacherHomeViewModel extends ViewModel {
    private static final String TAG = "TeacherProfileViewModel";
    private FirebaseDataRepository firebaseDataRepository;
    private AuthRepository authRepository;

    private MediatorLiveData<Teacher> onTeacherProfileInfo = new MediatorLiveData<>();
    private MediatorLiveData<List<StudentApp>> onTeacher = new MediatorLiveData<>();
    private final MediatorLiveData<StateResource> onTeacherDelete = new MediatorLiveData<>();
    private final MediatorLiveData<StateResource> onTeacherCounseling = new MediatorLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public TeacherHomeViewModel(FirebaseDataRepository firebaseDataRepository, AuthRepository authRepository) {
        Log.d(TAG, "TeacherProfileViewModel: is working.....");
        this.firebaseDataRepository= firebaseDataRepository;
        this.authRepository= authRepository;
    }

    // update counseling....
    public void updateCounseling(String s){

        firebaseDataRepository.updateCounseling(s).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                onTeacherCounseling.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {
                onTeacherCounseling.setValue(StateResource.success());
            }

            @Override
            public void onError(@NonNull Throwable e) {

                onTeacherCounseling.setValue(StateResource.error(e.toString()));
            }
        });
    }

    // teacher Appointment delete .....

    public void teacherDelete(String pushKey){
        firebaseDataRepository.teacherDelete(pushKey)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                onTeacherDelete.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {
                onTeacherDelete.setValue(StateResource.success());

            }

            @Override
            public void onError(@NonNull Throwable e) {

                onTeacherDelete.setValue(StateResource.error(e.toString()));
            }
        });

    }

    // set student Token.....
    public void setToken(String token){
        firebaseDataRepository.setToken(token);
    }

    public void getTeacherInfo(){
        firebaseDataRepository.getTeacherInfo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).toObservable().subscribe(new Observer<DocumentSnapshot>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(@NonNull DocumentSnapshot documentSnapshot) {
                Teacher teacher= documentSnapshot.toObject(Teacher.class);
                onTeacherProfileInfo.setValue(teacher);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    // get student appointment alternatively....

    public void getStudentAppointment(){
        firebaseDataRepository.getStudentAppointment().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .toObservable().subscribe(new Observer<List<StudentApp>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(@NonNull List<StudentApp> teacherApps) {

                onTeacher.setValue(teacherApps);
            }

            @Override
            public void onError(@NonNull Throwable e) {


            }

            @Override
            public void onComplete() {


            }
        });
    }

    public LiveData<Teacher> observeTeacherInfo(){
        return onTeacherProfileInfo;
    }

    // observe student Appointment.....
    public LiveData<List<StudentApp>> observeStudentAppointment(){
        return onTeacher;
    }

    // teacher delete observer.....

    public LiveData<StateResource> observeTeacherDelete(){
        return onTeacherDelete;
    }

    // teacher counseling update observe....
    public LiveData<StateResource> observeCounseling(){
        return onTeacherCounseling;
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
