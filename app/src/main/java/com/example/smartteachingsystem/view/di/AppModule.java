package com.example.smartteachingsystem.view.di;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.dataSource.FirebaseAuthSource;
import com.example.smartteachingsystem.view.dataSource.FirebaseDataSource;
import com.example.smartteachingsystem.view.repository.AuthRepository;
import com.example.smartteachingsystem.view.repository.FirebaseDataRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    // get Firebase Auth Instance
    @Singleton
    @Provides
    static FirebaseAuth getAuthInstance(){
        return FirebaseAuth.getInstance();
    }


    // get Firebase FireStore Auth Instance
    @Singleton
    @Provides
    static FirebaseFirestore provideFirebaseInstance(){
        return FirebaseFirestore.getInstance();
    }

    // get Firebase Storage Instance
    @Singleton
    @Provides
    static StorageReference provideStorageReference(){
        return FirebaseStorage.getInstance().getReference();
    }

    @Singleton
    @Provides
    static FirebaseAuthSource getAuthSource(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore){
        return new FirebaseAuthSource(firebaseAuth,firebaseFirestore);
    }

    @Singleton
    @Provides
    static AuthRepository provideAuthRepository(FirebaseAuthSource authSource){
        return  new AuthRepository(authSource);
    }

    @Provides
    static FirebaseDataSource provideFirebaseDataSource(FirebaseAuthSource firebaseAuthSource, FirebaseFirestore fireStore, StorageReference storageReference){
        return new FirebaseDataSource(firebaseAuthSource,fireStore,storageReference);
    }

    @Provides
    static FirebaseDataRepository provideFirebaseDataRepository(FirebaseDataSource firebaseDataSource){
        return new FirebaseDataRepository(firebaseDataSource);
    }

    @Singleton
    @Provides
    static RequestOptions provideRequestOptions(){
        return RequestOptions.placeholderOf(R.drawable.profile).error(R.drawable.profile);
    }

    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions){
        return Glide.with(application).setDefaultRequestOptions(requestOptions);
    }
}
