package com.example.smartteachingsystem.view.di.studentAppointment;

import com.example.smartteachingsystem.view.notification.APIService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class StudentAppModule {

    @Provides
    static APIService provideAPIService(Retrofit retrofit){
        return retrofit.create(APIService.class);
    }

}
