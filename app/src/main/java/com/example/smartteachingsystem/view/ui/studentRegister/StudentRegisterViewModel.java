package com.example.smartteachingsystem.view.ui.studentRegister;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.repository.FirebaseDataRepository;
import com.example.smartteachingsystem.view.utils.StateResource;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StudentRegisterViewModel extends ViewModel {

    private static final String TAG = "StudentRegisterViewMode";
    private FirebaseDataRepository firebaseDataRepository;
    private MediatorLiveData<StateResource> onStudent = new MediatorLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public StudentRegisterViewModel(FirebaseDataRepository firebaseDataRepository) {
        Log.d(TAG, "StudentRegisterViewModel: is working....");
        this.firebaseDataRepository= firebaseDataRepository;
    }

    // set Student data...
    public void setStudentData(Student studentData, Bitmap bitmap){
        firebaseDataRepository.setStudentData(studentData, bitmap)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                onStudent.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {

                onStudent.setValue(StateResource.success());
            }

            @Override
            public void onError(@NonNull Throwable e) {

                onStudent.setValue(StateResource.error(e.toString()));
            }
        });
    }

    // Observe set Student data save or not ....
    public LiveData<StateResource> observeSetStudentData(){
        return onStudent;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
