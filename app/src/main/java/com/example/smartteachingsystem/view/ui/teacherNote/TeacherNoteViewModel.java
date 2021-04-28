package com.example.smartteachingsystem.view.ui.teacherNote;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartteachingsystem.view.model.Note;
import com.example.smartteachingsystem.view.model.Teacher_List;
import com.example.smartteachingsystem.view.repository.AuthRepository;
import com.example.smartteachingsystem.view.repository.FirebaseDataRepository;
import com.example.smartteachingsystem.view.utils.StateResource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TeacherNoteViewModel extends ViewModel {

    private static final String TAG = "TeacherNoteViewModel";
    private final FirebaseDataRepository firebaseDataRepository;
    private final MediatorLiveData<StateResource> onNoteSave = new MediatorLiveData<>();
    private final MediatorLiveData<StateResource> onNoteDelete = new MediatorLiveData<>();
    private final MediatorLiveData<List<Note>> onNoteList = new MediatorLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public TeacherNoteViewModel(FirebaseDataRepository firebaseDataRepository) {
        Log.d(TAG, "TeacherNoteViewModel: is working.....");
        this.firebaseDataRepository= firebaseDataRepository;
    }

    public void noteDelete(String s){
        firebaseDataRepository.noteDelete(s).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                onNoteDelete.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {
                onNoteDelete.setValue(StateResource.success());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                onNoteDelete.setValue(StateResource.error(e.toString()));
            }
        });
    }

    public void getNotes(){
        firebaseDataRepository.getAllNote().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .toObservable().subscribe(new Observer<List<Note>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(@NonNull List<Note> notes) {
                onNoteList.setValue(notes);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void insertNote(Note note){
        firebaseDataRepository.insertNote(note).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable.add(d);
                onNoteSave.setValue(StateResource.loading());
            }

            @Override
            public void onComplete() {
                onNoteSave.setValue(StateResource.success());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                onNoteSave.setValue(StateResource.error(e.toString()));
            }
        });
    }

    // Observe note Save....
    public LiveData<StateResource> observeSave(){
        return onNoteSave;
    }

    // Observe note delete....
    public LiveData<StateResource> observeDelete(){
        return onNoteDelete;
    }


    public LiveData<List<Note>> observeNoteList(){
        return onNoteList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
