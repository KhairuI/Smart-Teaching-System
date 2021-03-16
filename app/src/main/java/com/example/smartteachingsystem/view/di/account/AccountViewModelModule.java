package com.example.smartteachingsystem.view.di.account;

import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.di.ViewModelKey;
import com.example.smartteachingsystem.view.ui.account.AccountViewModel;
import com.example.smartteachingsystem.view.ui.login.LoginViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AccountViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel.class)
    public abstract ViewModel bindAccountViewModel(AccountViewModel viewModel);
}
