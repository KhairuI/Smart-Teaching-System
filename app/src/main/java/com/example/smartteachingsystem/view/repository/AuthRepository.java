package com.example.smartteachingsystem.view.repository;

import com.example.smartteachingsystem.view.dataSource.FirebaseAuthSource;
import com.example.smartteachingsystem.view.model.Login;
import com.example.smartteachingsystem.view.utils.LoginResource;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class AuthRepository {

    FirebaseAuthSource firebaseAuthSource;

    public AuthRepository(FirebaseAuthSource firebaseAuthSource) {
        this.firebaseAuthSource = firebaseAuthSource;
    }

    // get current user
    public FirebaseUser getCurrentUser() {
        return firebaseAuthSource.getCurrentUser();
    }

    // get current user Id
    public String getCurrentUid() {
        return firebaseAuthSource.getCurrentUid();
    }

    // login
    public Completable login(String email, String password){
        return firebaseAuthSource.login(email, password);
    }

    // check user Role....
    public String userRole(){
        return firebaseAuthSource.userRole();
    }

    // check user Role in splash ....
    public String splashRole(){
        return firebaseAuthSource.splashRole();
    }

    // check user Role in splash ....
    public Flowable<DocumentSnapshot> getUserRole(){
        return firebaseAuthSource.getUserRole();
    }

    // Create new user/ Registration with email & password....
    public Completable register(String email, String password){
        return firebaseAuthSource.register(email,password);
    }

    //logout...
    public void logOut(){
        firebaseAuthSource.logout();
    }
}
