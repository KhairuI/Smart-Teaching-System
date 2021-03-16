package com.example.smartteachingsystem.view.ui.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.ui.login.LoginActivity;
import com.example.smartteachingsystem.view.ui.login.LoginViewModel;
import com.example.smartteachingsystem.view.ui.register.RegisterActivity;
import com.example.smartteachingsystem.view.utils.RxBindingHelper;
import com.example.smartteachingsystem.view.utils.StateResource;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class AccountActivity extends DaggerAppCompatActivity implements View.OnClickListener {

    // Declare all view..
    private ProgressBar accountProgressBar;
    private TextInputEditText accountEmail, accountPassword;
    private Button accountButton,loginNowButton;
    private TextView accountText;
    private AccountViewModel accountViewModel;

    //Rx variable
    Observable<Boolean> formObservable;

    // Dependency Injection
    @Inject
    ViewModelProviderFactory modelProviderFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        findSection();

        formValidation();
        accountViewModel= new ViewModelProvider(getViewModelStore(),modelProviderFactory).get(AccountViewModel.class);

        loginObserve();

    }

    private void loginObserve() {
        accountViewModel.observeRegister().observe(this, new Observer<StateResource>() {
            @Override
            public void onChanged(StateResource stateResource) {
                if(stateResource!= null){
                    switch (stateResource.status){
                        case LOADING:
                            accountProgressBar.setVisibility(View.VISIBLE);
                            break;
                        case SUCCESS:
                            accountProgressBar.setVisibility(View.INVISIBLE);
                            accountButton.setEnabled(false);
                            showSnackBar("Sent verification mail. Please check email");
                            loginNowButton.setEnabled(true);
                            break;
                        case ERROR:
                            accountProgressBar.setVisibility(View.INVISIBLE);
                            showSnackBar(stateResource.message);
                            break;
                    }
                }
            }
        });

    }

    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }

    private void formValidation() {

        Observable<String> email_observable = RxBindingHelper.getObservableFrom(accountEmail);
        Observable<String> password_observable = RxBindingHelper.getObservableFrom(accountPassword);

        formObservable= Observable.combineLatest(email_observable, password_observable, new BiFunction<String, String, Boolean>() {
            @Override
            public Boolean apply(String email, String password) throws Throwable {
                return isValidForm(email,password);
            }
        });

        formObservable.subscribe(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                accountButton.setEnabled(aBoolean);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    private Boolean isValidForm(String email, String password) {

        boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.isEmpty();
        if (!isEmail) {
            accountEmail.setError("Please enter valid email");
        }

        boolean isPassword = password.length() > 6 && !TextUtils.isEmpty(password);
        if (!isPassword) {
            accountPassword.setError("Password must be greater then 6 digit");
        }

        return isEmail && isPassword;
    }

    private void findSection() {
        accountProgressBar= findViewById(R.id.createAccountProgressId);
        accountProgressBar.setVisibility(View.GONE);
        accountEmail= findViewById(R.id.accountEmailEditTextId);
        accountPassword= findViewById(R.id.accountPasswordEditTextId);
        accountButton= findViewById(R.id.createAccountButtonId);
        accountButton.setOnClickListener(this);
        loginNowButton= findViewById(R.id.loginNowButtonId);
        loginNowButton.setOnClickListener(this);
        accountText= findViewById(R.id.accountToolbarTextId);
        accountText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.accountToolbarTextId){
            goToLoginActivity();
        }
        else if(view.getId()==R.id.createAccountButtonId){
            setRegistration();
        }
        else if(view.getId()==R.id.loginNowButtonId){
            goToLoginActivity();
        }
    }

    private void setRegistration() {
        String email = accountEmail.getText().toString().trim();
        String password = accountPassword.getText().toString().trim();
        accountViewModel.register(email,password);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
        startActivity(intent);

    }
}