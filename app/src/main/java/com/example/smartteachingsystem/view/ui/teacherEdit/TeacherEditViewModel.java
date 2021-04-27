package com.example.smartteachingsystem.view.ui.teacherEdit;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

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

public class TeacherEditViewModel extends ViewModel {
    private static final String TAG = "TeacherEditViewModel";
    private FirebaseDataRepository firebaseDataRepository;
    private MediatorLiveData<StateResource> onTeacherUpdate = new MediatorLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public TeacherEditViewModel(FirebaseDataRepository firebaseDataRepository) {
        Log.d(TAG, "TeacherEditViewModel: viewModel is continue");
        this.firebaseDataRepository= firebaseDataRepository;
    }

    // update with image...
    public void updateWithImage(Teacher teacher, Bitmap bitmap){
        firebaseDataRepository.updateTeacherWithImage(teacher, bitmap) .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                onTeacherUpdate.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {
                onTeacherUpdate.setValue(StateResource.success());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                onTeacherUpdate.setValue(StateResource.error(e.toString()));
            }
        });

    }
    // update without image...
    public void updateWithoutImage(Teacher teacher) {
        firebaseDataRepository.updateTeacherWithoutImage(teacher).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                onTeacherUpdate.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {
                onTeacherUpdate.setValue(StateResource.success());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                onTeacherUpdate.setValue(StateResource.error(e.toString()));
            }
        });
    }


    // Observe update Teacher save or not ....
    public LiveData<StateResource> observeUpdateTeacher(){
        return onTeacherUpdate;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
