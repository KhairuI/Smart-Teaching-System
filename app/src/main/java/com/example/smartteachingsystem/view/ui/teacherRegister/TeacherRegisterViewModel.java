package com.example.smartteachingsystem.view.ui.teacherRegister;

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

public class TeacherRegisterViewModel extends ViewModel {

    private static final String TAG = "TeacherRegisterViewMode";
    private FirebaseDataRepository firebaseDataRepository;
    private MediatorLiveData<StateResource> onTeacher = new MediatorLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();


    @Inject
    public TeacherRegisterViewModel(FirebaseDataRepository firebaseDataRepository) {
        Log.d(TAG, "TeacherRegisterViewModel: is working....");
        this.firebaseDataRepository= firebaseDataRepository;

    }

    // set Teacher data...
    public void setTeacherData(Teacher teacher, Bitmap bitmap){
        firebaseDataRepository.setTeacherData(teacher, bitmap)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                onTeacher.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {

                onTeacher.setValue(StateResource.success());
            }

            @Override
            public void onError(@NonNull Throwable e) {

                onTeacher.setValue(StateResource.error(e.toString()));
            }
        });
    }

    // Observe set Student data save or not ....
    public LiveData<StateResource> observeSetTeacherData(){
        return onTeacher;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
