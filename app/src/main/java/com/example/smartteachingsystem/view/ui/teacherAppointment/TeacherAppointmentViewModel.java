package com.example.smartteachingsystem.view.ui.teacherAppointment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.model.Response;
import com.example.smartteachingsystem.view.repository.FirebaseDataRepository;
import com.example.smartteachingsystem.view.utils.StateResource;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TeacherAppointmentViewModel extends ViewModel {

    private final FirebaseDataRepository firebaseDataRepository;
    private final MediatorLiveData<StateResource> teacherAppointment = new MediatorLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();

    private static final String TAG="TeacherAppointmentViewM";

    @Inject
    public TeacherAppointmentViewModel(FirebaseDataRepository firebaseDataRepository) {
        Log.d(TAG, "TeacherAppointmentViewModel: is working....");
        this.firebaseDataRepository= firebaseDataRepository;
    }

    // set teacher response....
    public void teacherResponse(Response response){

        firebaseDataRepository.setTeacherResponse(response).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                teacherAppointment.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {
                teacherAppointment.setValue(StateResource.success());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                teacherAppointment.setValue(StateResource.error(e.toString()));
            }
        });
    }

    // Observe teacher response save or not ....
    public LiveData<StateResource> observeTeacherResponse(){
        return teacherAppointment;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
