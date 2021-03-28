package com.example.smartteachingsystem.view.ui.studentHome;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.repository.AuthRepository;
import com.example.smartteachingsystem.view.repository.FirebaseDataRepository;
import com.google.firebase.firestore.DocumentSnapshot;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StudentHomeViewModel extends ViewModel {
    private static final String TAG="StudentProfileViewModel";
    private FirebaseDataRepository firebaseDataRepository;
    private AuthRepository authRepository;

    private MediatorLiveData<Student> onStudentProfileInfo = new MediatorLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public StudentHomeViewModel(FirebaseDataRepository firebaseDataRepository, AuthRepository authRepository) {
        Log.d(TAG, "StudentProfileViewModel: is working...");
        this.firebaseDataRepository= firebaseDataRepository;
        this.authRepository= authRepository;
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

    public LiveData<Student> observeStudentInfo(){
        return onStudentProfileInfo;
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
