package com.example.smartteachingsystem.view.di;

import com.example.smartteachingsystem.view.di.account.AccountViewModelModule;
import com.example.smartteachingsystem.view.di.login.LoginViewModelModule;
import com.example.smartteachingsystem.view.di.profileStudent.ProfileStudentViewModelModule;
import com.example.smartteachingsystem.view.di.profileTeacher.ProfileTeacherViewModelModule;
import com.example.smartteachingsystem.view.di.register.RegisterViewModelModule;
import com.example.smartteachingsystem.view.di.splash.SplashViewModelModule;
import com.example.smartteachingsystem.view.di.studentAppointment.StudentAppModule;
import com.example.smartteachingsystem.view.di.studentAppointment.StudentAppViewModelModule;
import com.example.smartteachingsystem.view.di.studentEdit.StudentEditViewModelModule;
import com.example.smartteachingsystem.view.di.studentHome.StudentHomeModule;
import com.example.smartteachingsystem.view.di.studentHome.StudentHomeViewModelModule;
import com.example.smartteachingsystem.view.di.studentRegister.StudentRegViewModelModule;
import com.example.smartteachingsystem.view.di.teacherAppointment.TeacherAppModule;
import com.example.smartteachingsystem.view.di.teacherAppointment.TeacherAppViewModelModule;
import com.example.smartteachingsystem.view.di.teacherEdit.TeacherEditViewModelModule;
import com.example.smartteachingsystem.view.di.teacherHome.TeacherHomeModule;
import com.example.smartteachingsystem.view.di.teacherList.TeacherListModule;
import com.example.smartteachingsystem.view.di.teacherList.TeacherListViewModelModule;
import com.example.smartteachingsystem.view.di.teacherHome.TeacherHomeViewModelModule;
import com.example.smartteachingsystem.view.di.teacherNote.TeacherNoteModule;
import com.example.smartteachingsystem.view.di.teacherNote.TeacherNoteViewModelModule;
import com.example.smartteachingsystem.view.di.teacherRegister.TeacherRegViewModelModule;
import com.example.smartteachingsystem.view.ui.profileStudent.ProfileStudent;
import com.example.smartteachingsystem.view.ui.profileTeacher.ProfileTeacher;
import com.example.smartteachingsystem.view.ui.splash.SplashActivity;
import com.example.smartteachingsystem.view.ui.account.AccountActivity;
import com.example.smartteachingsystem.view.ui.login.LoginActivity;
import com.example.smartteachingsystem.view.ui.register.RegisterActivity;
import com.example.smartteachingsystem.view.ui.studentAppointment.StudentAppointment;
import com.example.smartteachingsystem.view.ui.studentEdit.StudentEditProfile;
import com.example.smartteachingsystem.view.ui.studentHome.StudentHome;
import com.example.smartteachingsystem.view.ui.studentRegister.StudentRegister;
import com.example.smartteachingsystem.view.ui.teacherAppointment.TeacherAppointment;
import com.example.smartteachingsystem.view.ui.teacherEdit.TeacherEditProfile;
import com.example.smartteachingsystem.view.ui.teacherList.TeacherList;
import com.example.smartteachingsystem.view.ui.teacherHome.TeacherHome;
import com.example.smartteachingsystem.view.ui.teacherNote.TeacherNote;
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
                    TeacherHomeViewModelModule.class,
                    TeacherHomeModule.class
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
                    StudentAppViewModelModule.class,
                    StudentAppModule.class
            }
    )
    abstract StudentAppointment contributeStudentAppointment();

    // Teacher Appointment Activity
    @ContributesAndroidInjector(
            modules = {
                    TeacherAppViewModelModule.class,
                    TeacherAppModule.class
            }
    )
    abstract TeacherAppointment contributeTeacherAppointment();


    // profile Student Activity

    @ContributesAndroidInjector(
            modules = {
                    ProfileStudentViewModelModule.class
            }
    )
    abstract ProfileStudent contributeProfileStudent();

    // profile Teacher Activity

    @ContributesAndroidInjector(
            modules = {
                    ProfileTeacherViewModelModule.class
            }
    )
    abstract ProfileTeacher contributeProfileTeacher();

    // Student Edit Profile Activity

    @ContributesAndroidInjector(
            modules = {
                    StudentEditViewModelModule.class
            }
    )
    abstract StudentEditProfile contributeStudentEditProfile();

    // Teacher Edit Profile Activity

    @ContributesAndroidInjector(
            modules = {
                    TeacherEditViewModelModule.class
            }
    )
    abstract TeacherEditProfile contributeTeacherEditProfile();

    // Teacher Note list Activity

    @ContributesAndroidInjector(
            modules = {
                    TeacherNoteViewModelModule.class,
                    TeacherNoteModule.class
            }
    )
    abstract TeacherNote contributeTeacherNote();


}
