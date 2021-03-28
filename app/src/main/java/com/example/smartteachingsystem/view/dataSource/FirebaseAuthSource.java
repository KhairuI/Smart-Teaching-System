package com.example.smartteachingsystem.view.dataSource;

import androidx.annotation.Nullable;

import com.example.smartteachingsystem.view.model.Login;
import com.example.smartteachingsystem.view.utils.Nodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableEmitter;
import io.reactivex.rxjava3.core.CompletableOnSubscribe;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.functions.Cancellable;

public class FirebaseAuthSource {
    private static final String TAG = "FirebaseAuthSource";

    public FirebaseAuth firebaseAuth;
    public FirebaseFirestore fireStore;
    public  String role;
    public  String value;

    @Inject
    public FirebaseAuthSource(FirebaseAuth firebaseAuth, FirebaseFirestore fireStore) {
        this.firebaseAuth = firebaseAuth;
        this.fireStore = fireStore;
    }

    //get current user uid
    public String getCurrentUid() {
        return firebaseAuth.getCurrentUser().getUid();
    }

    //get current user
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    // Login here....
    public Completable login(final String email, final String password){

        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            try {

                                if(getCurrentUser().isEmailVerified()){
                                    fireStore.collection(Nodes.USERS_ROLE).document(getCurrentUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){

                                               DocumentSnapshot ds= task.getResult();
                                               role= ds.getString("role");
                                                emitter.onComplete();
                                            }
                                            else {
                                                emitter.onError(task.getException());
                                            }
                                        }
                                    });

                                }
                                else {

                                    emitter.onError(task.getException());
                                }

                            }catch (NullPointerException e){
                                emitter.onError(e);
                            }
                        }
                        else {
                            emitter.onError(task.getException());
                        }
                    }
                });
            }
        });

    }

    // check user Role....
    public String userRole( ){
        return  role;
    }

    // get user role here

    public Flowable<DocumentSnapshot> getUserRole(){
        return Flowable.create(new FlowableOnSubscribe<DocumentSnapshot>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<DocumentSnapshot> emitter) throws Throwable {
                DocumentReference reference= fireStore.collection(Nodes.USERS_ROLE).document(firebaseAuth.getCurrentUser().getUid());
                final ListenerRegistration registration= reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
                        if(e!= null){
                            emitter.onError(e);
                        }
                        if(value!= null){
                            emitter.onNext(value);
                        }
                    }
                });
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Throwable {
                        registration.remove();
                    }
                });
            }
        },BackpressureStrategy.BUFFER);
    }

    // get user role here
    public String splashRole(){

        fireStore.collection(Nodes.USERS_ROLE).document(getCurrentUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds= task.getResult();
                    if (ds != null) {
                        value= ds.getString("role");
                    }
                }
            }
        });
       return value;
    }

    // Create new user/ Registration with email & password....

    public Completable register( final String email, final String password){

        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //emitter.onComplete();
                        getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Map<String,String> roleMap= new HashMap<>();
                                roleMap.put("role","null");
                                fireStore.collection(Nodes.USERS_ROLE).document(getCurrentUid()).set(roleMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        emitter.onComplete();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                                        emitter.onError(e);
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception e) {
                                emitter.onError(e);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        emitter.onError(e);
                    }
                });
            }
        });
    }

    //logout
    public void logout(){
        firebaseAuth.signOut();
    }

}
