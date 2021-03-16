package com.example.smartteachingsystem.view.di;

import com.example.smartteachingsystem.view.di.account.AccountViewModelModule;
import com.example.smartteachingsystem.view.di.login.LoginViewModelModule;
import com.example.smartteachingsystem.view.di.register.RegisterViewModelModule;
import com.example.smartteachingsystem.view.di.splash.SplashViewModelModule;
import com.example.smartteachingsystem.view.di.studentProfile.StudentProfileViewModelModule;
import com.example.smartteachingsystem.view.di.studentRegister.StudentRegViewModelModule;
import com.example.smartteachingsystem.view.di.teacherProfile.TeacherProfileViewModelModule;
import com.example.smartteachingsystem.view.di.teacherRegister.TeacherRegViewModelModule;
import com.example.smartteachingsystem.view.ui.splash.SplashActivity;
import com.example.smartteachingsystem.view.ui.account.AccountActivity;
import com.example.smartteachingsystem.view.ui.login.LoginActivity;
import com.example.smartteachingsystem.view.ui.register.RegisterActivity;
import com.example.smartteachingsystem.view.ui.studentProfile.StudentProfile;
import com.example.smartteachingsystem.view.ui.studentRegister.StudentRegister;
import com.example.smartteachingsystem.view.ui.teacherProfile.TeacherProfile;
import com.example.smartteachingsystem.view.ui.teacherRegister.TeacherRegister;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {


    // Login Activity
    @ContributesAndroidInjector(
            modules = {
                    LoginViewModelModule.class
            })
    abstract LoginActivity contributeLoginActivity();

    // Register Activity
    @ContributesAndroidInjector(
            modules = {
                    RegisterViewModelModule.class
            })
    abstract RegisterActivity contributeRegisterActivity();

    // Account Activity
    @ContributesAndroidInjector(
            modules = {
                    AccountViewModelModule.class
            })
    abstract AccountActivity contributeAccountActivity();


    // Splash Activity
    @ContributesAndroidInjector(
            modules = {
                    SplashViewModelModule.class
            }
    )
    abstract SplashActivity contributeSplashActivity();


    // Teacher Register Activity
    @ContributesAndroidInjector(
            modules = {
                    TeacherRegViewModelModule.class
            }
    )
    abstract TeacherRegister contributeTeacherRegister();


    // Student Register Activity
    @ContributesAndroidInjector(
            modules = {
                    StudentRegViewModelModule.class
            }
    )
    abstract StudentRegister contributeStudentRegister();

    // Student Profile Activity
    @ContributesAndroidInjector(
            modules = {
                    StudentProfileViewModelModule.class
            }
    )
    abstract StudentProfile contributeStudentProfile();

    // Teacher Profile Activity
    @ContributesAndroidInjector(
            modules = {
                    TeacherProfileViewModelModule.class
            }
    )
    abstract TeacherProfile contributeTeacherProfile();


}
