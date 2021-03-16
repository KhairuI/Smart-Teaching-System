package com.example.smartteachingsystem.view.di;

import androidx.lifecycle.ViewModelProvider;

import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {
    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory modelProviderFactory);
}
