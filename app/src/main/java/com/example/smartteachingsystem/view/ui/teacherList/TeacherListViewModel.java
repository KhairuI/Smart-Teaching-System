package com.example.smartteachingsystem.view.ui.teacherList;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.model.Teacher_List;
import com.example.smartteachingsystem.view.repository.FirebaseDataRepository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TeacherListViewModel extends ViewModel {
    private static final String TAG = "TeacherListViewModel";
    private FirebaseDataRepository firebaseDataRepository;

    private MediatorLiveData<List<Teacher_List>> onTeacher = new MediatorLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public TeacherListViewModel(FirebaseDataRepository firebaseDataRepository) {
        Log.d(TAG, "TeacherListViewModel: is working........");
        this.firebaseDataRepository= firebaseDataRepository;
    }

    public void getTeacher(){
        firebaseDataRepository.getTeacher().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .toObservable().subscribe(new Observer<List<Teacher_List>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(@NonNull List<Teacher_List> teacherList) {

                onTeacher.setValue(teacherList);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public LiveData<List<Teacher_List>> observeTeacher(){
        return onTeacher;
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }


}
