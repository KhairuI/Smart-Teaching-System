package com.example.smartteachingsystem.view.di;

import com.example.smartteachingsystem.view.di.account.AccountViewModelModule;
import com.example.smartteachingsystem.view.di.login.LoginViewModelModule;
import com.example.smartteachingsystem.view.di.profileStudent.ProfileStudentViewModelModule;
import com.example.smartteachingsystem.view.di.register.RegisterViewModelModule;
import com.example.smartteachingsystem.view.di.splash.SplashViewModelModule;
import com.example.smartteachingsystem.view.di.studentAppointment.StudentAppViewModelModule;
import com.example.smartteachingsystem.view.di.studentHome.StudentHomeModule;
import com.example.smartteachingsystem.view.di.studentHome.StudentHomeViewModelModule;
import com.example.smartteachingsystem.view.di.studentRegister.StudentRegViewModelModule;
import com.example.smartteachingsystem.view.di.teacherList.TeacherListModule;
import com.example.smartteachingsystem.view.di.teacherList.TeacherListViewModelModule;
import com.example.smartteachingsystem.view.di.teacherHome.TeacherHomeViewModelModule;
import com.example.smartteachingsystem.view.di.teacherRegister.TeacherRegViewModelModule;
import com.example.smartteachingsystem.view.ui.profileStudent.ProfileStudent;
import com.example.smartteachingsystem.view.ui.splash.SplashActivity;
import com.example.smartteachingsystem.view.ui.account.AccountActivity;
import com.example.smartteachingsystem.view.ui.login.LoginActivity;
import com.example.smartteachingsystem.view.ui.register.RegisterActivity;
import com.example.smartteachingsystem.view.ui.studentAppointment.StudentAppointment;
import com.example.smartteachingsystem.view.ui.studentHome.StudentHome;
import com.example.smartteachingsystem.view.ui.studentRegister.StudentRegister;
import com.example.smartteachingsystem.view.ui.teacherList.TeacherList;
import com.example.smartteachingsystem.view.ui.teacherHome.TeacherHome;
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
                    StudentHomeViewModelModule.class,
                    StudentHomeModule.class
            }
    )
    abstract StudentHome contributeStudentHome();

    // Teacher Home Activity
    @ContributesAndroidInjector(
            modules = {
                    TeacherHomeViewModelModule.class
            }
    )
    abstract TeacherHome contributeTeacherHome();

    // Teacher List Activity
    @ContributesAndroidInjector(
            modules = {
                    TeacherListViewModelModule.class,
                    TeacherListModule.class
            }
    )
    abstract TeacherList contributeTeacherList();

    // Student Appointment Activity
    @ContributesAndroidInjector(
            modules = {
                    StudentAppViewModelModule.class
            }
    )
    abstract StudentAppointment contributeStudentAppointment();

    // profile Student Activity

    @ContributesAndroidInjector(
            modules = {
                    ProfileStudentViewModelModule.class
            }
    )
    abstract ProfileStudent contributeProfileStudent();


}
