package com.example.smartteachingsystem.view.ui.teacherList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.adapter.AllTeacherAdapter;
import com.example.smartteachingsystem.view.adapter.TeacherAdapter;
import com.example.smartteachingsystem.view.model.Teacher_List;
import com.example.smartteachingsystem.view.ui.splash.SplashViewModel;
import com.example.smartteachingsystem.view.ui.studentAppointment.StudentAppointment;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class TeacherList extends DaggerAppCompatActivity implements AllTeacherAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    // Declare all views..
    private RecyclerView teacherList;
    private Toolbar toolbar;
    private ProgressBar progressBar;
  //  private TeacherAdapter teacherAdapter;
    private TeacherListViewModel teacherListViewModel;
    private List<Teacher_List> newList= new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;

    // Dependency Injection
   /* @Inject
    TeacherAdapter adapter;*/

    @Inject
    AllTeacherAdapter adapter;

    @Inject
    ViewModelProviderFactory modelProviderFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        findSection();
        teacherListViewModel= new ViewModelProvider(getViewModelStore(),modelProviderFactory).get(TeacherListViewModel.class);
        teacherListViewModel.getTeacher();
        observeList();
        setRecycleView();
    }

    private void observeList() {
        teacherListViewModel.observeTeacher().observe(this, new Observer<List<Teacher_List>>() {
            @Override
            public void onChanged(List<Teacher_List> list) {
                newList= list;
                progressBar.setVisibility(View.GONE);
                adapter.setList(newList);

            }
        });
    }

    private void setRecycleView() {
        teacherList.setHasFixedSize(true);
        teacherList.setLayoutManager(new LinearLayoutManager(this));
        teacherList.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
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

    private void findSection() {

        toolbar= findViewById(R.id.teacherListToolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        teacherList= findViewById(R.id.teacherListRecycleId);
        progressBar= findViewById(R.id.allTeacherProgressId);
        refreshLayout= findViewById(R.id.refreshId);
        refreshLayout.setOnRefreshListener(this);

    }

    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(int position) {
        Teacher_List list= newList.get(position);
        // sent data in Student Appointment Activity....
        Intent intent= new Intent(TeacherList.this, StudentAppointment.class);
        intent.putExtra("key",list);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        newList.clear();
        teacherListViewModel.getTeacher();
        refreshLayout.setRefreshing(false);


    }
}