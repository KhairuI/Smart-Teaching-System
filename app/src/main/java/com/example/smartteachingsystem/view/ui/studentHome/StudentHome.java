package com.example.smartteachingsystem.view.ui.studentHome;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.adapter.StudentAppointmentAdapter;
import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.ui.login.LoginActivity;
import com.example.smartteachingsystem.view.ui.profileStudent.ProfileStudent;
import com.example.smartteachingsystem.view.ui.teacherList.TeacherList;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class StudentHome extends DaggerAppCompatActivity implements View.OnClickListener,StudentAppointmentAdapter.OnItemClickListener {
    // declare all views...
    private CircleImageView profileImage;
    private TextView profileName, studentId;
    private FloatingActionButton button;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private Student newStudent;
    private StudentHomeViewModel studentHomeViewModel;


    // Dependency Injection

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;

    @Inject
    StudentAppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        findSection();
        studentHomeViewModel = new ViewModelProvider(getViewModelStore(),providerFactory).get(StudentHomeViewModel.class);
        studentHomeViewModel.getStudentInfo();
        observeStudentInfo();
        setRecycleView();
    }

    private void setRecycleView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.startListening();

    }

    private void observeStudentInfo() {
        studentHomeViewModel.observeStudentInfo().observe(this, new Observer<Student>() {
            @Override
            public void onChanged(Student student) {
                newStudent= student;
                requestManager.load(student.getImage()).into(profileImage);
                profileName.setText(student.getName());
                studentId.setText(student.getId());
            }
        });
    }

    private void findSection() {
        toolbar= findViewById(R.id.studentProfileToolbarId);
        setSupportActionBar(toolbar);
        profileImage= findViewById(R.id.studentProfileImageId);
        profileName= findViewById(R.id.studentProfileNameId);
        studentId= findViewById(R.id.studentProfileUniversityId);
        recyclerView= findViewById(R.id.studentRecycleViewId);
        button= findViewById(R.id.studentInsertId);
        button.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.editId){
            Intent intent= new Intent(StudentHome.this, ProfileStudent.class);
            intent.putExtra("student",newStudent);
            startActivity(intent);

        }
        else if(item.getItemId()==R.id.logoutId){
            studentHomeViewModel.logout();
            goToLoginActivity();

        }
        else if(item.getItemId()==R.id.aboutId){
          showSnackBar("About");

        }
        return super.onOptionsItemSelected(item);
    }
    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.studentInsertId){
            Intent intent= new Intent(StudentHome.this, TeacherList.class);
            startActivity(intent);
        }
    }
    private void goToLoginActivity() {
        Intent intent = new Intent(StudentHome.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
        String name= documentSnapshot.getString("name");
        showSnackBar(name);
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