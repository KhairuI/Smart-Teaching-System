package com.example.smartteachingsystem.view.ui.studentAppointment;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.model.TeacherApp;
import com.example.smartteachingsystem.view.repository.FirebaseDataRepository;
import com.example.smartteachingsystem.view.utils.StateResource;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StudentAppointmentViewModel extends ViewModel {
    private static final String TAG = "StudentAppointmentViewM";
    private FirebaseDataRepository firebaseDataRepository;
    private MediatorLiveData<StateResource> studentAppointment = new MediatorLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public StudentAppointmentViewModel(FirebaseDataRepository firebaseDataRepository) {
        this.firebaseDataRepository= firebaseDataRepository;
        Log.d(TAG, "StudentAppointmentViewModel: is working...");
    }

    // set student Appointment....

    public void setStudentAppointment(TeacherApp teacherApp){
        firebaseDataRepository.setStudentAppointment(teacherApp).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                studentAppointment.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {
                studentAppointment.setValue(StateResource.success());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                studentAppointment.setValue(StateResource.error(e.toString()));
            }
        });
    }

    // Observe student appointment save or not ....
    public LiveData<StateResource> observeStudentAppointment(){
        return studentAppointment;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
