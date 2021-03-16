package com.example.smartteachingsystem.view.di.login;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.login.LoginViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class LoginViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    public abstract ViewModel bindLoginViewModel(LoginViewModel viewModel);
}
