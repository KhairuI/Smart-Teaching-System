package com.example.smartteachingsystem.view.dataSource;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.smartteachingsystem.view.model.ChatUser;
import com.example.smartteachingsystem.view.model.Message;
import com.example.smartteachingsystem.view.model.StudentChatUser;
import com.example.smartteachingsystem.view.model.StudentMessage;
import com.example.smartteachingsystem.view.model.Note;
import com.example.smartteachingsystem.view.model.Response;
import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.model.StudentApp;
import com.example.smartteachingsystem.view.model.Teacher;
import com.example.smartteachingsystem.view.model.TeacherApp;
import com.example.smartteachingsystem.view.model.TeacherChatUser;
import com.example.smartteachingsystem.view.model.TeacherMessage;
import com.example.smartteachingsystem.view.model.Teacher_List;
import com.example.smartteachingsystem.view.model.Token;
import com.example.smartteachingsystem.view.utils.DataConverter;
import com.example.smartteachingsystem.view.utils.Nodes;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private final FirebaseFirestore fireStore;
    private final StorageReference storageReference;
    private final String currentUid;
    private Student student;

    @Inject
    public FirebaseDataSource(FirebaseAuthSource firebaseAuthSource, FirebaseFirestore fireStore, StorageReference storageReference) {
        this.fireStore = fireStore;
        this.storageReference = storageReference;
        currentUid= firebaseAuthSource.getCurrentUid();
    }

    // load student chat history
    public Flowable<List<StudentChatUser> > getHistoryList(){
        final List<StudentChatUser> historyLists= new ArrayList<>();
        return Flowable.create(new FlowableOnSubscribe<List<StudentChatUser>>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<List<StudentChatUser>> emitter) throws Throwable {
                fireStore.collection(Nodes.CHAT).document(Nodes.USERS).collection(currentUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        historyLists.clear();
                        for(DocumentSnapshot dc:task.getResult()){

                            StudentChatUser studentChatUser = dc.toObject(StudentChatUser.class);
                            historyLists.add(studentChatUser);
                        }
                        emitter.onNext(historyLists);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        emitter.onError(e);
                    }
                });
            }
        },BackpressureStrategy.BUFFER);
    }


    // load teacher chat history
    public Flowable<List<TeacherChatUser> > getTeacherHistoryList(){
        final List<TeacherChatUser> historyLists= new ArrayList<>();
        return Flowable.create(new FlowableOnSubscribe<List<TeacherChatUser>>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<List<TeacherChatUser>> emitter) throws Throwable {
                fireStore.collection(Nodes.CHAT).document(Nodes.USERS).collection(currentUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        historyLists.clear();
                        for(DocumentSnapshot dc:task.getResult()){

                            TeacherChatUser teacherChatUser = dc.toObject(TeacherChatUser.class);
                            historyLists.add(teacherChatUser);
                        }
                        emitter.onNext(historyLists);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        emitter.onError(e);
                    }
                });
            }
        },BackpressureStrategy.BUFFER);
    }


    // get all message....

    public Flowable<QuerySnapshot> getMessageList(String uId){
        return Flowable.create(new FlowableOnSubscribe<QuerySnapshot>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<QuerySnapshot> emitter) throws Throwable {

                CollectionReference reference = fireStore.collection("chat").document("message").collection(uId);
                final ListenerRegistration registration = reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            emitter.onError(error);
                        }
                        if(value != null){
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

    // send message from student site....
    public Completable sendStudentMessage(Message message,StudentChatUser studentChatUser, TeacherChatUser teacherChatUser){

        return  Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {

                WriteBatch messageBatch = fireStore.batch();
                WriteBatch userBatch = fireStore.batch();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                // make message reference...
                final DocumentReference my_message_ref= fireStore.collection("chat").document("message")
                        .collection(currentUid+"_"+ studentChatUser.getReceiverUid()).document(timestamp.toString().trim());

                final DocumentReference partner_message_ref= fireStore.collection("chat").document("message")
                        .collection(studentChatUser.getReceiverUid()+"_"+ currentUid).document(timestamp.toString().trim());
                messageBatch.set(my_message_ref, message);
                messageBatch.set(partner_message_ref, message);

                // make user reference...
                final DocumentReference my_ref= fireStore.collection("chat").document("users")
                        .collection(currentUid).document(studentChatUser.getReceiverUid());

                final DocumentReference partner_ref= fireStore.collection("chat").document("users")
                        .collection(studentChatUser.getReceiverUid()).document(currentUid);
                userBatch.set(my_ref, studentChatUser);
                userBatch.set(partner_ref, teacherChatUser);

                // now start to import...

                messageBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        userBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                emitter.onComplete();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull @NotNull Exception e) {
                                emitter.onError(e);
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull @NotNull Exception e) {
                        emitter.onError(e);
                    }
                });

            }
        });
    }



    // send message from Teacher site....
    public Completable sendTeacherMessage(Message message, TeacherChatUser teacherChatUser, StudentChatUser studentChatUser){

        return  Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {

                WriteBatch messageBatch = fireStore.batch();
                WriteBatch userBatch = fireStore.batch();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                // make message reference...
                final DocumentReference my_message_ref= fireStore.collection("chat").document("message")
                        .collection(currentUid+"_"+ teacherChatUser.getReceiverUid()).document(timestamp.toString().trim());

                final DocumentReference partner_message_ref= fireStore.collection("chat").document("message")
                        .collection(teacherChatUser.getReceiverUid()+"_"+ currentUid).document(timestamp.toString().trim());

                messageBatch.set(my_message_ref, message);
                messageBatch.set(partner_message_ref, message);

                // make user reference...
                final DocumentReference my_ref= fireStore.collection("chat").document("users")
                        .collection(currentUid).document(teacherChatUser.getReceiverUid());

                final DocumentReference partner_ref= fireStore.collection("chat").document("users")
                        .collection(teacherChatUser.getReceiverUid()).document(currentUid);
                userBatch.set(my_ref, teacherChatUser);
                userBatch.set(partner_ref, studentChatUser);

                // now start to import...

                messageBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        userBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                emitter.onComplete();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull @NotNull Exception e) {
                                emitter.onError(e);
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull @NotNull Exception e) {
                        emitter.onError(e);
                    }
                });

            }
        });
    }

    // added teacher Note......
    public Completable insertNote(Note note){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {
                final DocumentReference db_reference= fireStore.collection(Nodes.TEACHERS_PROFILE).document(currentUid)
                        .collection("note").document(note.getPushKey());

                // make note map
                Map<String,String> noteMap= new HashMap<>();
                noteMap.put("pushKey",note.getPushKey());
                noteMap.put("date",note.getDate());
                noteMap.put("name",note.getName());
                noteMap.put("noteText",note.getNoteText());
                db_reference.set(noteMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        });
    }

    // get all teacher  note here.....
    public Flowable<List<Note> > getAllNote(){
        final List<Note> noteList= new ArrayList<>();

        return Flowable.create(new FlowableOnSubscribe<List<Note>>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<List<Note>> emitter) throws Throwable {
                fireStore.collection(Nodes.TEACHERS_PROFILE).document(currentUid).collection("note").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                                noteList.clear();
                                for(DocumentSnapshot dc:task.getResult()){

                                    Note note= dc.toObject(Note.class);
                                    noteList.add(note);
                                }
                                emitter.onNext(noteList);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        emitter.onError(e);
                    }
                });
            }
        },BackpressureStrategy.BUFFER);
    }


    // update teacher counseling...
    public Completable updateCounseling(String s){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {
                final DocumentReference db_reference= fireStore.collection(Nodes.TEACHERS_PROFILE).document(currentUid)
                        .collection("profile").document("profile_key");
                final DocumentReference all_teachers= fireStore.collection(Nodes.ALL_TEACHERS).document(currentUid);
                db_reference.update("counseling",s).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        all_teachers.update("counseling",s).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        });
    }

    // teacher get Student Token.....
    public Flowable<Token> getStudentToken(String uId){

        return Flowable.create(new FlowableOnSubscribe<Token>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Token> emitter) throws Throwable {
                fireStore.collection(Nodes.TEACHERS_PROFILE).document(currentUid)
                        .collection("profile").document("profile_key").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot documentSnapshot= task.getResult();
                        if(documentSnapshot!=null){
                            String name= documentSnapshot.getString("name");

                            fireStore.collection(Nodes.TOKENS).document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot documentSnapshot= task.getResult();
                                    if(documentSnapshot!=null){
                                        String userToken= documentSnapshot.getString("token");
                                        Token token = new Token(name,userToken);
                                        emitter.onNext(token);
                                    }
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
        },BackpressureStrategy.BUFFER);

    }


    // get user Token....
    public Flowable<Token> getToken(String uId){


        return Flowable.create(new FlowableOnSubscribe<Token>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Token> emitter) throws Throwable {
                fireStore.collection(Nodes.STUDENTS_PROFILE).document(currentUid)
                        .collection("profile").document("profile_key").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot documentSnapshot= task.getResult();
                        if(documentSnapshot!=null){
                            String name= documentSnapshot.getString("name");

                            fireStore.collection(Nodes.TOKENS).document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot documentSnapshot= task.getResult();
                                    if(documentSnapshot!=null){
                                        String userToken= documentSnapshot.getString("token");
                                        Token token = new Token(name,userToken);
                                        emitter.onNext(token);
                                    }
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
        },BackpressureStrategy.BUFFER);

    }

    // set user token.....

    public void setToken(String token){
        Map<String,String> tokenMap= new HashMap<>();
        tokenMap.put("token",token);
        fireStore.collection(Nodes.TOKENS).document(currentUid).set(tokenMap);
    }

    // update teacher Data without Image.......
    public Completable updateTeacherWithoutImage(final Teacher teacher){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {
                final DocumentReference db_reference= fireStore.collection(Nodes.TEACHERS_PROFILE).document(currentUid)
                        .collection("profile").document("profile_key");
                final DocumentReference all_teachers= fireStore.collection(Nodes.ALL_TEACHERS).document(currentUid);

                // update teacher profile...
                db_reference.update("name",teacher.getName(),
                        "id",teacher.getId(),
                        "email",teacher.getEmail(),
                        "department",teacher.getDepartment(),
                        "designation",teacher.getDesignation(),
                        "initial",teacher.getInitial(),
                        "office",teacher.getOffice(),
                        "phone",teacher.getPhone()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // now update all teacher...
                        all_teachers.update("name",teacher.getName(),
                                "id",teacher.getId(),
                                "email",teacher.getEmail(),
                                "department",teacher.getDepartment(),
                                "designation",teacher.getDesignation(),
                                "initial",teacher.getInitial(),
                                "office",teacher.getOffice(),
                                "phone",teacher.getPhone()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        });

    }

    // update teacher Data with Image.......
    public Completable updateTeacherWithImage(final Teacher teacher, final Bitmap bitmap){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {
                // create all paths..
                final StorageReference reference= storageReference.child(Nodes.TEACHERS_PROFILE).child(currentUid+".jpg");
                final DocumentReference db_reference= fireStore.collection(Nodes.TEACHERS_PROFILE).document(currentUid)
                        .collection("profile").document("profile_key");
                final DocumentReference all_teachers= fireStore.collection(Nodes.ALL_TEACHERS).document(currentUid);

                // upload image
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

                                    // update teacher profile...
                                    db_reference.update("name",teacher.getName(),
                                            "id",teacher.getId(),
                                            "email",teacher.getEmail(),
                                            "department",teacher.getDepartment(),
                                            "designation",teacher.getDesignation(),
                                            "image",imageUrl,
                                            "initial",teacher.getInitial(),
                                            "office",teacher.getOffice(),
                                            "phone",teacher.getPhone())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // now update all teacher...
                                            all_teachers.update("name",teacher.getName(),
                                                    "id",teacher.getId(),
                                                    "email",teacher.getEmail(),
                                                    "department",teacher.getDepartment(),
                                                    "designation",teacher.getDesignation(),
                                                    "image",imageUrl,
                                                    "initial",teacher.getInitial(),
                                                    "office",teacher.getOffice(),
                                                    "phone",teacher.getPhone()).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    // update student without image...
    public Completable updateStudentWithoutImage(final Student student){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {
                // create path..
                final DocumentReference db_reference= fireStore.collection(Nodes.STUDENTS_PROFILE).document(currentUid)
                        .collection("profile").document("profile_key");
                db_reference.update("name",student.getName(),
                        "department",student.getDepartment(),
                        "email",student.getEmail(),
                        "id",student.getId(),
                        "phone",student.getPhone(),
                        "section",student.getSection()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        });
    }

    // update student with image...

    public Completable updateStudentWithImage(final Student student,final Bitmap bitmap){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {
                // create path..
                final StorageReference reference= storageReference.child(Nodes.STUDENTS_PROFILE).child(currentUid+".jpg");
                final DocumentReference db_reference= fireStore.collection(Nodes.STUDENTS_PROFILE).document(currentUid)
                        .collection("profile").document("profile_key");
                reference.putBytes(DataConverter.convertImage2ByteArray(bitmap)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<Uri> task) {
                                // here get image url...
                                String imageUrl= task.getResult().toString();

                                db_reference.update("name",student.getName(),
                                        "department",student.getDepartment(),
                                        "email",student.getEmail(),
                                        "id",student.getId(),
                                        "image",imageUrl,
                                        "phone",student.getPhone(),
                                        "section",student.getSection())
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
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        emitter.onError(e);
                    }
                });

            }
        });
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

    // retrieve teacher profile data in chatting activity

    public Flowable<DocumentSnapshot> getTeacherInfoInChatting(String uId){
        return Flowable.create(new FlowableOnSubscribe<DocumentSnapshot>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<DocumentSnapshot> emitter) throws Throwable {
                DocumentReference reference= fireStore.collection(Nodes.ALL_TEACHERS).document(uId);
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

    public Flowable<DocumentSnapshot> getStudentChattingInfo(String uId){
        return Flowable.create(new FlowableOnSubscribe<DocumentSnapshot>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<DocumentSnapshot> emitter) throws Throwable {
                DocumentReference reference= fireStore.collection(Nodes.STUDENTS_PROFILE).document(uId)
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

    // retrieve student chat Info



    // **** Retrieve teacher list  alternatively here......

    public Flowable<List<Teacher_List> > getTeacher(){
        final List<Teacher_List> teacherLists= new ArrayList<>();
        return Flowable.create(new FlowableOnSubscribe<List<Teacher_List>>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<List<Teacher_List>> emitter) throws Throwable {
                fireStore.collection(Nodes.ALL_TEACHERS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        teacherLists.clear();
                        for(DocumentSnapshot dc:task.getResult()){

                            Teacher_List teacherList= dc.toObject(Teacher_List.class);
                            teacherLists.add(teacherList);
                        }
                        emitter.onNext(teacherLists);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        emitter.onError(e);
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

    // get student appointment alternatively.....
    public Flowable<List<TeacherApp>> getStudent(){
        final List<TeacherApp> appList= new ArrayList<>();
        return Flowable.create(new FlowableOnSubscribe<List<TeacherApp>>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<List<TeacherApp>> emitter) throws Throwable {

                fireStore.collection(Nodes.STUDENTS_PROFILE).document(currentUid).collection("appointment").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        appList.clear();
                        for(DocumentSnapshot dc:task.getResult()){

                            TeacherApp teacherApp= dc.toObject(TeacherApp.class);
                            appList.add(teacherApp);
                        }
                        emitter.onNext(appList);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        emitter.onError(e);
                    }
                });
            }
        },BackpressureStrategy.BUFFER);
    }


    //   ******//

    //  get teacher appointment alternatively.....

    public Flowable<List<StudentApp>> getStudentAppointment(){
        final List<StudentApp> appList= new ArrayList<>();
        return Flowable.create(new FlowableOnSubscribe<List<StudentApp>>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<List<StudentApp>> emitter) throws Throwable {

                fireStore.collection(Nodes.TEACHERS_PROFILE).document(currentUid).collection("appointment").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        appList.clear();
                        for(DocumentSnapshot dc:task.getResult()){

                            StudentApp studentApp= dc.toObject(StudentApp.class);
                            appList.add(studentApp);
                        }
                        emitter.onNext(appList);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        emitter.onError(e);
                    }
                });
            }
        },BackpressureStrategy.BUFFER);
    }

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

    // save teacher response. This response save in both teacher & student directory...

    public Completable setTeacherResponse(Response response){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {

                final DocumentReference teacher_reference= fireStore.collection(Nodes.TEACHERS_PROFILE).document(currentUid)
                        .collection("appointment").document(response.getPushKey());

                final DocumentReference student_reference= fireStore.collection(Nodes.STUDENTS_PROFILE).document(response.getuId())
                        .collection("appointment").document(response.getPushKey());

                teacher_reference.update("message",response.getMessage(),"status",response.getStatus())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                student_reference.update("message",response.getMessage(),"status", response.getStatus())
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
        });
    }

    // student appointment delete......

    public Completable studentDelete(String pushKey){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {

                final DocumentReference student_reference= fireStore.collection(Nodes.STUDENTS_PROFILE).document(currentUid)
                        .collection("appointment").document(pushKey);
                student_reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
        });
    }

    // teacher note delete....

    public Completable noteDelete(String pushKey){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {

                final DocumentReference teacher_reference= fireStore.collection(Nodes.TEACHERS_PROFILE).document(currentUid)
                        .collection("note").document(pushKey);
                teacher_reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
        });
    }


    // teacher appointment delete......

    public Completable teacherDelete(String pushKey){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {

                final DocumentReference teacher_reference= fireStore.collection(Nodes.TEACHERS_PROFILE).document(currentUid)
                        .collection("appointment").document(pushKey);
                teacher_reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
        });
    }

    // chat history delete......

    public Completable chatDelete(String uId){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {

                final DocumentReference chat_ref= fireStore.collection(Nodes.CHAT).document(Nodes.USERS)
                        .collection(currentUid).document(uId);
                chat_ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
        });
    }
}
