package com.example.smartteachingsystem.view.dataSource;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.model.Teacher;
import com.example.smartteachingsystem.view.utils.DataConverter;
import com.example.smartteachingsystem.view.utils.Nodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

public class FirebaseDataSource {
    private static final String TAG = "FirebaseDataSource";

    private FirebaseAuthSource firebaseAuthSource;
    private FirebaseFirestore fireStore;
    private StorageReference storageReference;
    private String currentUid;

    @Inject
    public FirebaseDataSource(FirebaseAuthSource firebaseAuthSource, FirebaseFirestore fireStore, StorageReference storageReference) {
        this.firebaseAuthSource = firebaseAuthSource;
        this.fireStore = fireStore;
        this.storageReference = storageReference;
        currentUid= firebaseAuthSource.getCurrentUid();
    }

    // set student data....
    public Completable setStudentData(final Student student, final Bitmap bitmap){

        return  Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {

                final StorageReference reference= storageReference.child(Nodes.STUDENTS_PROFILE).child(currentUid+".jpg");
                final DocumentReference db_reference= fireStore.collection(Nodes.STUDENTS_PROFILE).document(currentUid);
                reference.putBytes(DataConverter.convertImage2ByteArray(bitmap)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<Uri> task) {
                                if(!task.isSuccessful()){
                                    emitter.onError(task.getException());
                                }
                                else {
                                    // here get image url...
                                    String imageUrl= task.getResult().toString();

                                    // add all data in map...
                                    Map<String,String> studentMap= new HashMap<>();
                                    studentMap.put("name",student.getName());
                                    studentMap.put("id",student.getId());
                                    studentMap.put("email",student.getEmail());
                                    studentMap.put("phone",student.getPhone());
                                    studentMap.put("department",student.getDepartment());
                                    studentMap.put("section",student.getSection());
                                    studentMap.put("image",imageUrl);

                                    db_reference.set(studentMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            fireStore.collection(Nodes.USERS_ROLE).document(currentUid).update("role","student")
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
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

    // set Teacher data....
    public Completable setTeacherData(final Teacher teacher, final Bitmap bitmap){

        return  Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {

                final StorageReference reference= storageReference.child(Nodes.TEACHERS_PROFILE).child(currentUid+".jpg");
                final DocumentReference db_reference= fireStore.collection(Nodes.TEACHERS_PROFILE).document(currentUid);
                reference.putBytes(DataConverter.convertImage2ByteArray(bitmap)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<Uri> task) {
                                if(!task.isSuccessful()){
                                    emitter.onError(task.getException());
                                }
                                else {
                                    // here get image url...
                                    String imageUrl= task.getResult().toString();

                                    // add all data in map...
                                    Map<String,String> studentMap= new HashMap<>();
                                    studentMap.put("name",teacher.getName());
                                    studentMap.put("id",teacher.getId());
                                    studentMap.put("email",teacher.getEmail());
                                    studentMap.put("phone",teacher.getPhone());
                                    studentMap.put("department",teacher.getDepartment());
                                    studentMap.put("designation",teacher.getDesignation());
                                    studentMap.put("image",imageUrl);
                                    studentMap.put("office",teacher.getOffice());
                                    studentMap.put("counseling",teacher.getCounseling());

                                    db_reference.set(studentMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            fireStore.collection(Nodes.USERS_ROLE).document(currentUid).update("role","teacher")
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
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

    // retrieve teacher profile data

    public Flowable<DocumentSnapshot> getTeacherInfo(){
        return Flowable.create(new FlowableOnSubscribe<DocumentSnapshot>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<DocumentSnapshot> emitter) throws Throwable {
                DocumentReference reference= fireStore.collection(Nodes.TEACHERS_PROFILE).document(currentUid);
                final ListenerRegistration registration= reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(e!= null){
                            emitter.onError(e);
                        }
                        if(documentSnapshot != null){
                            emitter.onNext(documentSnapshot);
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
        }, BackpressureStrategy.BUFFER);
    }

    // retrieve student profile data

    public Flowable<DocumentSnapshot> getStudentInfo(){
        return Flowable.create(new FlowableOnSubscribe<DocumentSnapshot>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<DocumentSnapshot> emitter) throws Throwable {
                DocumentReference reference= fireStore.collection(Nodes.STUDENTS_PROFILE).document(currentUid);
                final ListenerRegistration registration= reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(e!= null){
                            emitter.onError(e);
                        }
                        if(documentSnapshot != null){
                            emitter.onNext(documentSnapshot);
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
        }, BackpressureStrategy.BUFFER);
    }
}
