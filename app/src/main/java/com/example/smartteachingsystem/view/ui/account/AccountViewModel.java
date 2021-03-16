package com.example.smartteachingsystem.view.ui.account;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.repository.AuthRepository;
import com.example.smartteachingsystem.view.utils.StateResource;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AccountViewModel extends ViewModel {

    private static final String TAG = "AccountViewModel";
    private AuthRepository authRepository;
    private MediatorLiveData<StateResource> onRegister = new MediatorLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public AccountViewModel(AuthRepository authRepository) {
        Log.d(TAG, "AccountViewModel: is working...");
        this.authRepository= authRepository;

    }

    // Create new user/ Registration with email & password....
    public void register( String email, String password){
        authRepository.register(email, password)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                onRegister.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {
                onRegister.setValue(StateResource.success());

            }

            @Override
            public void onError(@NonNull Throwable e) {

                onRegister.setValue(StateResource.error(e.getMessage()));
            }
        });

    }

    // Observe Login....
    public LiveData<StateResource> observeRegister(){
        return onRegister;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
