package com.example.smartteachingsystem.view.ui.login;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.model.Login;
import com.example.smartteachingsystem.view.repository.AuthRepository;
import com.example.smartteachingsystem.view.utils.LoginResource;
import com.example.smartteachingsystem.view.utils.StateResource;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";
    private final AuthRepository authRepository;
    private final MediatorLiveData<StateResource> onLogin = new MediatorLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public LoginViewModel(AuthRepository authRepository) {
        this.authRepository= authRepository;
        Log.d(TAG, "LoginViewModel: is working");
    }

    // login
    public void login(String email,String password){
        authRepository.login(email, password)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                onLogin.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {
                onLogin.setValue(StateResource.success());

            }

            @Override
            public void onError(@NonNull Throwable e) {

                onLogin.setValue(StateResource.error("Not verified. Please check email"));
            }
        });

    }

    // Observe Login....
    public LiveData<StateResource> observeLogin(){
        return onLogin;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
