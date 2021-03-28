package com.example.smartteachingsystem.view.dataSource;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.model.StudentApp;
import com.example.smartteachingsystem.view.model.Teacher;
import com.example.smartteachingsystem.view.model.TeacherApp;
import com.example.smartteachingsystem.view.model.Teacher_List;
import com.example.smartteachingsystem.view.utils.DataConverter;
import com.example.smartteachingsystem.view.utils.Nodes;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
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
    private Student student;

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
                final DocumentReference db_reference= fireStore.collection(Nodes.STUDENTS_PROFILE).document(currentUid)
                        .collection("profile").document("profile_key");
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
                final DocumentReference db_reference= fireStore.collection(Nodes.TEACHERS_PROFILE).document(currentUid)
                        .collection("profile").document("profile_key");
                final DocumentReference all_teachers= fireStore.collection(Nodes.ALL_TEACHERS).document(currentUid);
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
                                    Map<String,String> teacherMap= new HashMap<>();
                                    teacherMap.put("name",teacher.getName());
                                    teacherMap.put("id",teacher.getId());
                                    teacherMap.put("email",teacher.getEmail());
                                    teacherMap.put("phone",teacher.getPhone());
                                    teacherMap.put("department",teacher.getDepartment());
                                    teacherMap.put("designation",teacher.getDesignation());
                                    teacherMap.put("image",imageUrl);
                                    teacherMap.put("office",teacher.getOffice());
                                    teacherMap.put("counseling",teacher.getCounseling());
                                    teacherMap.put("initial",teacher.getInitial());

                                    db_reference.set(teacherMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            fireStore.collection(Nodes.USERS_ROLE).document(currentUid).update("role","teacher")
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            teacherMap.put("uId",currentUid);
                                                            all_teachers.set(teacherMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                DocumentReference reference= fireStore.collection(Nodes.TEACHERS_PROFILE).document(currentUid)
                        .collection("profile").document("profile_key");
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
                DocumentReference reference= fireStore.collection(Nodes.STUDENTS_PROFILE).document(currentUid)
                        .collection("profile").document("profile_key");
                final ListenerRegistration registration= reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(e!= null){
                            emitter.onError(e);
                        }
                        if(documentSnapshot != null){
                            student= documentSnapshot.toObject(Student.class);
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

    // **** Retrieve teacher list here......

    public Flowable<FirestoreRecyclerOptions<Teacher_List> > getTeacher(){
        return Flowable.create(new FlowableOnSubscribe<FirestoreRecyclerOptions<Teacher_List>>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<FirestoreRecyclerOptions<Teacher_List>> emitter) throws Throwable {
               Query query= fireStore.collection(Nodes.ALL_TEACHERS);
                final ListenerRegistration registration= query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if(e!= null){
                            emitter.onError(e);
                        }
                        if(value != null){
                            FirestoreRecyclerOptions<Teacher_List> options= new FirestoreRecyclerOptions.Builder<Teacher_List>()
                                    .setQuery(query, Teacher_List.class).build();
                            emitter.onNext(options);
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

    // make query....
    private Query getQuery(){
        return fireStore.collection(Nodes.ALL_TEACHERS);
    }

    // get all teacher list....

    public FirestoreRecyclerOptions<Teacher_List> getAllTeacher(){
        return new FirestoreRecyclerOptions.Builder<Teacher_List>().setQuery(getQuery(), Teacher_List.class).build();
    }
  //   ******//

    // get Student appointment list from student directory.....
    private Query getStudentQuery(){
        return fireStore.collection(Nodes.STUDENTS_PROFILE).document(currentUid).collection("appointment");
    }

    // get all student appointment list....

    public FirestoreRecyclerOptions<TeacherApp> getAllStudentAppointment(){
        return new FirestoreRecyclerOptions.Builder<TeacherApp>().setQuery(getStudentQuery(), TeacherApp.class).build();
    }

    //   ******//

    // get teacher appointment list from teacher directory.....

    private Query getTeacherQuery(){
        return fireStore.collection(Nodes.TEACHERS_PROFILE).document(currentUid).collection("appointment");
    }

    // get all student appointment list....

    public FirestoreRecyclerOptions<StudentApp> getAllTeacherAppointment(){
        return new FirestoreRecyclerOptions.Builder<StudentApp>().setQuery(getTeacherQuery(), StudentApp.class).build();
    }

    //

    // save appointment when student request for appointment.
    // Appointment save in both teacher & student directory..

    public Completable setStudentAppointment(TeacherApp teacherApp){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {
                final DocumentReference student_reference= fireStore.collection(Nodes.STUDENTS_PROFILE).document(currentUid)
                        .collection("appointment").document(teacherApp.getPushKey());
                final DocumentReference teacher_reference= fireStore.collection(Nodes.TEACHERS_PROFILE).document(teacherApp.getuId())
                        .collection("appointment").document(teacherApp.getPushKey());

                // get student data.....
                DocumentReference reference= fireStore.collection(Nodes.STUDENTS_PROFILE).document(currentUid)
                        .collection("profile").document("profile_key");


                // make map for student directory......
                Map<String,String> studentMap= new HashMap<>();
                studentMap.put("status",teacherApp.getStatus());
                studentMap.put("message",teacherApp.getMessage());
                studentMap.put("name",teacherApp.getName());
                studentMap.put("id",teacherApp.getId());
                studentMap.put("uId",teacherApp.getuId());
                studentMap.put("pushKey",teacherApp.getPushKey());
                studentMap.put("email",teacherApp.getEmail());
                studentMap.put("dept",teacherApp.getDept());
                studentMap.put("image",teacherApp.getImage());
                studentMap.put("office",teacherApp.getOffice());
                studentMap.put("initial",teacherApp.getInitial());
                studentMap.put("designation",teacherApp.getDesignation());
                studentMap.put("phone",teacherApp.getPhone());

                student_reference.set(studentMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {

                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot= task.getResult();
                                    student= documentSnapshot.toObject(Student.class);

                                    // now make map for teacher directory....
                                    Map<String,String> teacherMap= new HashMap<>();
                                    teacherMap.put("status","Request");
                                    teacherMap.put("message",teacherApp.getMessage());
                                    teacherMap.put("uId",currentUid);
                                    teacherMap.put("pushKey",teacherApp.getPushKey());
                                    teacherMap.put("name",student.getName());
                                    teacherMap.put("id",student.getId());
                                    teacherMap.put("email",student.getEmail());
                                    teacherMap.put("dept",student.getDepartment());
                                    teacherMap.put("image",student.getImage());
                                    teacherMap.put("section",student.getSection());
                                    teacherMap.put("phone",student.getPhone());

                                    teacher_reference.set(teacherMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                else {
                                    emitter.onError(task.getException());
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
}
