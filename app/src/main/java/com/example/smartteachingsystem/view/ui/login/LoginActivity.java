package com.example.smartteachingsystem.view.ui.login;

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
import com.example.smartteachingsystem.view.repository.AuthRepository;
import com.example.smartteachingsystem.view.ui.register.RegisterActivity;
import com.example.smartteachingsystem.view.utils.RxBindingHelper;
import com.example.smartteachingsystem.view.utils.StateResource;
import com.example.smartteachingsystem.view.ui.account.AccountActivity;
import com.example.smartteachingsystem.view.ui.studentHome.StudentHome;
import com.example.smartteachingsystem.view.ui.teacherHome.TeacherHome;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class LoginActivity extends DaggerAppCompatActivity implements View.OnClickListener {

    // Declare all view..
    private ProgressBar loginProgressBar;
    private TextInputEditText loginEmail, loginPassword;
    private Button loginButton;
    private TextView loginTextView;
    private LoginViewModel loginViewModel;

    //Rx variable
    Observable<Boolean> formObservable;

    // Dependency Injection
    @Inject
    ViewModelProviderFactory modelProviderFactory;

    @Inject
    AuthRepository authRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findSection();
        formValidation();
        loginViewModel= new ViewModelProvider(getViewModelStore(),modelProviderFactory).get(LoginViewModel.class);

        loginObserve();
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.loginButtonId){
            setLogin();
        }
        else if(view.getId()==R.id.loginToolbarTextId){
            goToAccountActivity();
        }

    }


    private void loginObserve() {
        loginViewModel.observeLogin().observe(this, new Observer<StateResource>() {
            @Override
            public void onChanged(StateResource stateResource) {
                if(stateResource != null){
                    switch (stateResource.status){
                        case LOADING:
                            loginProgressBar.setVisibility(View.VISIBLE);
                            break;
                        case SUCCESS:
                            loginProgressBar.setVisibility(View.INVISIBLE);
                            String role= authRepository.userRole();
                            if(role.equals("null")){
                                goToRegisterActivity();
                            }
                            else if(role.equals("student")){
                                goToStudentActivity();
                            }
                            else if(role.equals("teacher")){
                                goToTeacherActivity();
                            }
                            break;
                        case ERROR:
                            loginProgressBar.setVisibility(View.INVISIBLE);
                            showSnackBar(stateResource.message);
                            break;
                    }
                }
            }
        });
    }

    private void goToTeacherActivity() {
        Intent intent = new Intent(LoginActivity.this, TeacherHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void goToStudentActivity() {
        Intent intent = new Intent(LoginActivity.this, StudentHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }


    private void goToRegisterActivity() {

        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    // form validate
    private void formValidation() {
        Observable<String> email_observable = RxBindingHelper.getObservableFrom(loginEmail);
        Observable<String> password_observable = RxBindingHelper.getObservableFrom(loginPassword);

        formObservable= Observable.combineLatest(email_observable, password_observable, new BiFunction<String, String, Boolean>() {
            @Override
            public Boolean apply(String email, String password) throws Throwable {
                return isValidForm(email,password);
            }
        });

        formObservable.subscribe(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                loginButton.setEnabled(aBoolean);
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
            loginEmail.setError("Please enter valid email");
        }

        boolean isPassword = password.length() > 6 && !TextUtils.isEmpty(password);
        if (!isPassword) {
            loginPassword.setError("Password must be greater then 6 digit");
        }

        return isEmail && isPassword;
    }

    private void findSection() {
        loginProgressBar= findViewById(R.id.loginProgressId);
        loginProgressBar.setVisibility(View.GONE);
        loginEmail= findViewById(R.id.loginEmailEditTextId);
        loginPassword= findViewById(R.id.loginPasswordEditTextId);
        loginButton= findViewById(R.id.loginButtonId);
        loginButton.setOnClickListener(this);
        loginTextView= findViewById(R.id.loginToolbarTextId);
        loginTextView.setOnClickListener(this);

    }



    private void goToAccountActivity() {
        Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
        startActivity(intent);


    }

    private void setLogin() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();
        loginViewModel.login(email,password);
    }

}