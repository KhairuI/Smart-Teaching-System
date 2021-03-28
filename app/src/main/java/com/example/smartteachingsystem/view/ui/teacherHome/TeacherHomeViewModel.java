package com.example.smartteachingsystem.view.ui.teacherHome;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.model.Teacher;
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

public class TeacherHomeViewModel extends ViewModel {
    private static final String TAG = "TeacherProfileViewModel";
    private FirebaseDataRepository firebaseDataRepository;
    private AuthRepository authRepository;

    private MediatorLiveData<Teacher> onTeacherProfileInfo = new MediatorLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public TeacherHomeViewModel(FirebaseDataRepository firebaseDataRepository, AuthRepository authRepository) {
        Log.d(TAG, "TeacherProfileViewModel: is working.....");
        this.firebaseDataRepository= firebaseDataRepository;
        this.authRepository= authRepository;
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

    public LiveData<Teacher> observeTeacherInfo(){
        return onTeacherProfileInfo;
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
