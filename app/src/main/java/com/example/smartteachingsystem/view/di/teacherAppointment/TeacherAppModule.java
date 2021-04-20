package com.example.smartteachingsystem.view.di.teacherAppointment;

import com.example.smartteachingsystem.view.notification.APIService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class TeacherAppModule {

    @Provides
    static APIService provideTeacherAPIService(Retrofit retrofit){
        return retrofit.create(APIService.class);
    }
}
