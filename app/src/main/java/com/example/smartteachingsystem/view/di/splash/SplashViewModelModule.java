package com.example.smartteachingsystem.view.di.splash;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.register.RegisterViewModel;
import com.example.smartteachingsystem.view.ui.splash.SplashViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class SplashViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel.class)
    public abstract ViewModel bindSplashViewModel(SplashViewModel viewModel);
}
