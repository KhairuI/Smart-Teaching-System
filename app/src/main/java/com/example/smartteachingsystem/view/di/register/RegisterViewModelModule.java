package com.example.smartteachingsystem.view.di.register;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.register.RegisterViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class RegisterViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    public abstract ViewModel bindRegisterViewModel(RegisterViewModel viewModel);
}
