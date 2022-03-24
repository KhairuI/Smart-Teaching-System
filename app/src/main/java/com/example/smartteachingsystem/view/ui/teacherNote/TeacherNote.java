package com.example.smartteachingsystem.view.ui.teacherNote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.adapter.NoteAdapter;
import com.example.smartteachingsystem.view.model.Note;
import com.example.smartteachingsystem.view.model.StudentApp;
import com.example.smartteachingsystem.view.ui.teacherHome.TeacherHome;
import com.example.smartteachingsystem.view.ui.teacherHome.TeacherHomeViewModel;
import com.example.smartteachingsystem.view.utils.CheckInternet;
import com.example.smartteachingsystem.view.utils.NoInternetDialogue;
import com.example.smartteachingsystem.view.utils.StateResource;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class TeacherNote extends DaggerAppCompatActivity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener,NoteAdapter.OnItemClickListener {
    private TextView empty;
    private RecyclerView recyclerView;
    private TextInputEditText noteName,noteText;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;
    private TeacherNoteViewModel viewModel;
    private List<Note> newList= new ArrayList<>();

    // Dependency Injection

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_note);
        findSection();
        viewModel = new ViewModelProvider(getViewModelStore(),providerFactory).get(TeacherNoteViewModel.class);
        viewModel.getNotes();
        observeNotes();
        observeInsert();
        setRecycleView();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(adapter.getItemCount() != 0){
                    empty.setVisibility(View.GONE);
                }
                else {
                    empty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean Check(){

        return CheckInternet.connect(this);
    }

    private void observeInsert() {
        viewModel.observeSave().observe(this, new Observer<StateResource>() {
            @Override
            public void onChanged(StateResource stateResource) {
                if(stateResource != null){
                    switch (stateResource.status){
                        case LOADING:

                            break;
                        case SUCCESS:
                            showSnackBar("Save Successfully");
                            viewModel.getNotes();

                            break;
                        case ERROR:
                            showSnackBar(stateResource.message);
                            break;
                    }
                }
            }
        });
    }

    private void setRecycleView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    private void observeNotes() {
        viewModel.observeNoteList().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {

                newList= notes;
                progressBar.setVisibility(View.GONE);
                adapter.setList(newList);
            }
        });
    }

    private void findSection() {
        Toolbar toolbar = findViewById(R.id.noteListToolbarId);
        setSupportActionBar(toolbar);

        recyclerView= findViewById(R.id.noteListRecycleId);
        refreshLayout= findViewById(R.id.noteRefreshId);
        refreshLayout.setOnRefreshListener(this);
        empty= findViewById(R.id.noteEmptyId);
        TextView toolbarText = findViewById(R.id.noteToolbarTextId);
        toolbarText.setText("Note List");
        FloatingActionButton add = findViewById(R.id.noteInsertId);
        add.setOnClickListener(this);
        progressBar= findViewById(R.id.noteProgressId);

        ImageView back= findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherNote.this, TeacherHome.class));
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.noteInsertId){
            boolean isConnect= Check();
            if(isConnect){
                openDialogue();
            }
            else {
                NoInternetDialogue dialogue= new NoInternetDialogue();
                dialogue.show(getSupportFragmentManager(),"no_internet");
            }

        }
    }

    private void openDialogue() {

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        LayoutInflater inflater= getLayoutInflater();
        final View view= inflater.inflate(R.layout.note_dialogue,null);
        builder.setView(view).setTitle("Add Note").setCancelable(true).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(!TextUtils.isEmpty(noteName.getText().toString()) && !TextUtils.isEmpty(noteText.getText().toString())){

                    String date= currentDate();
                    String key= randomDigit();
                    String name= noteName.getText().toString();
                    String text= noteText.getText().toString();
                    Note note= new Note(key,date,name,text);
                    viewModel.insertNote(note);
                }
                else {
                    showSnackBar("Enter name and note");

                }
            }
        }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        noteName= view.findViewById(R.id.noteDialogueName);
        noteText= view.findViewById(R.id.dialogueNoteId);
        builder.create().show();

    }


    @Override
    public void onRefresh() {
        newList.clear();
        viewModel.getNotes();
        refreshLayout.setRefreshing(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.search_menu,menu);

        MenuItem menuItem= menu.findItem(R.id.searchId);
        SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(int position) {

        openViewNote(position);
    }

    private void openViewNote(int position) {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        LayoutInflater inflater= getLayoutInflater();
        final View view= inflater.inflate(R.layout.note_view_dialogue,null);
        builder.setView(view).setTitle("Note").setCancelable(true).setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        TextView name = view.findViewById(R.id.noteViewNameId);
        TextView date = view.findViewById(R.id.noteViewDateId);
        TextView text = view.findViewById(R.id.noteViewTextId);
        name.setText(newList.get(position).getName());
        date.setText(newList.get(position).getDate());
        text.setText(newList.get(position).getNoteText());
        builder.create().show();
    }

    @Override
    public void onLongItemClick(int position) {

        deleteDialogue(position);
    }

    private void deleteDialogue(int position) {
        String key= newList.get(position).getPushKey();
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Delete").setIcon(R.drawable.ic_delete).setMessage("Do you want to delete ?")
                .setCancelable(true).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                viewModel.noteDelete(key);
                observeDelete(position);

            }
        }).create().show();
    }

    private void observeDelete(int position) {
        viewModel.observeDelete().observe(this, new Observer<StateResource>() {
            @Override
            public void onChanged(StateResource stateResource) {
                if(stateResource != null){
                    switch (stateResource.status){
                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case SUCCESS:
                            progressBar.setVisibility(View.GONE);
                            newList.remove(position);
                            adapter.notifyItemRemoved(position);
                            showSnackBar("Delete successfully");

                            break;
                        case ERROR:
                            showSnackBar(stateResource.message);
                            break;
                    }
                }
            }
        });
    }

    // get current date
    public String currentDate(){
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        return currentDate.format(todayDate);
    }

    //generate a random digit.........
    private String randomDigit() {

        char[] chars= "1234567890".toCharArray();
        StringBuilder stringBuilder= new StringBuilder();
        Random random= new Random();
        for(int i=0;i<5;i++){
            char c= chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
}