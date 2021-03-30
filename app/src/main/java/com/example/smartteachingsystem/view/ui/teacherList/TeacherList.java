package com.example.smartteachingsystem.view.ui.teacherList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.adapter.TeacherAdapter;
import com.example.smartteachingsystem.view.model.Teacher_List;
import com.example.smartteachingsystem.view.ui.splash.SplashViewModel;
import com.example.smartteachingsystem.view.ui.studentAppointment.StudentAppointment;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class TeacherList extends DaggerAppCompatActivity implements TeacherAdapter.OnItemClickListener {
    // Declare all views..
    private RecyclerView teacherList;
  //  private Toolbar toolbar;
  //  private TeacherAdapter teacherAdapter;
    private TeacherListViewModel teacherListViewModel;

    // Dependency Injection
    @Inject
    TeacherAdapter adapter;

    @Inject
    ViewModelProviderFactory modelProviderFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        findSection();
        teacherListViewModel= new ViewModelProvider(getViewModelStore(),modelProviderFactory).get(TeacherListViewModel.class);
        //teacherListViewModel.getTeacher();
        setRecycleView();
       // observeList();
    }

    private void observeList() {
        teacherListViewModel.observeTeacher().observe(this, new Observer<FirestoreRecyclerOptions<Teacher_List>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Teacher_List> list) {
                /*teacherAdapter= new TeacherAdapter(list);
                teacherList.setAdapter(teacherAdapter);
                teacherAdapter.startListening();*/
            }
        });
    }

    private void setRecycleView() {
        teacherList.setHasFixedSize(true);
        teacherList.setLayoutManager(new LinearLayoutManager(this));
        if(adapter!= null){
            teacherList.setAdapter(adapter);
            adapter.setOnItemClickListener(this);
            adapter.startListening();
        }


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
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void findSection() {
        teacherList= findViewById(R.id.teacherListRecycleId);
       /* toolbar= findViewById(R.id.teacherListToolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
        String uId= documentSnapshot.getString("uId");
        String name= documentSnapshot.getString("name");
        String id= documentSnapshot.getString("id");
        String email= documentSnapshot.getString("email");
        String phone= documentSnapshot.getString("phone");
        String department= documentSnapshot.getString("department");
        String designation= documentSnapshot.getString("designation");
        String image= documentSnapshot.getString("image");
        String office= documentSnapshot.getString("office");
        String counseling= documentSnapshot.getString("counseling");
        String initial= documentSnapshot.getString("initial");
        Teacher_List list= new Teacher_List(uId,name,id,email,phone,department,designation,image,office,counseling,initial);

        // sent data in Student Appointment Activity....
        Intent intent= new Intent(TeacherList.this, StudentAppointment.class);
        intent.putExtra("key",list);
        startActivity(intent);


    }

    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}