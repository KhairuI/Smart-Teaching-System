package com.example.smartteachingsystem.view.ui.studentEdit;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.model.Teacher;
import com.example.smartteachingsystem.view.repository.FirebaseDataRepository;
import com.example.smartteachingsystem.view.utils.StateResource;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StudentEditViewModel extends ViewModel {
    private static final String TAG = "StudentEditViewModel";
    private FirebaseDataRepository firebaseDataRepository;
    private final MediatorLiveData<StateResource> onStudentUpdate = new MediatorLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public StudentEditViewModel(FirebaseDataRepository firebaseDataRepository) {
        Log.d(TAG, "StudentEditViewModel: is working....");
        this.firebaseDataRepository= firebaseDataRepository;

    }


    // student update with image...
    public void updateWithImage(Student student, Bitmap bitmap){
        firebaseDataRepository.updateStudentWithImage(student, bitmap) .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                onStudentUpdate.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {
                onStudentUpdate.setValue(StateResource.success());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                onStudentUpdate.setValue(StateResource.error(e.toString()));
            }
        });

    }
    // update without image...
    public void updateWithoutImage(Student student) {
        firebaseDataRepository.updateStudentWithoutImage(student).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                onStudentUpdate.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {
                onStudentUpdate.setValue(StateResource.success());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                onStudentUpdate.setValue(StateResource.error(e.toString()));
            }
        });
    }

    // Observe set update teacher save or not ....
    public LiveData<StateResource> observeUpdateStudent(){
        return onStudentUpdate;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
