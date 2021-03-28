package com.example.smartteachingsystem.view.ui.splash;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.repository.AuthRepository;
import com.google.firebase.firestore.DocumentSnapshot;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SplashViewModel extends ViewModel {
    private static final String TAG = "SplashViewModel";
    private AuthRepository authRepository;
    private MediatorLiveData<String> onRoleInfo = new MediatorLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();


    @Inject
    public SplashViewModel(AuthRepository authRepository) {
        Log.d(TAG, "SplashViewModel: is working....");
        this.authRepository = authRepository;
    }


    public void getUserRole(){
        authRepository.getUserRole()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).toObservable().subscribe(new Observer<DocumentSnapshot>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);

            }

            @Override
            public void onNext(@NonNull DocumentSnapshot documentSnapshot) {

                String role= documentSnapshot.getString("role");
                onRoleInfo.setValue(role);
            }

            @Override
            public void onError(@NonNull Throwable e) {

                Log.d(TAG, "onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public LiveData<String> observedRole(){
        return onRoleInfo;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
