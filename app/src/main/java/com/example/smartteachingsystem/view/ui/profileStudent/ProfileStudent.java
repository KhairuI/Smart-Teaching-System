package com.example.smartteachingsystem.view.ui.profileStudent;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.ui.studentEdit.StudentEditProfile;
import com.example.smartteachingsystem.view.ui.studentHome.StudentHomeViewModel;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileStudent extends DaggerAppCompatActivity implements View.OnClickListener {

    private FloatingActionButton edit;
    private TextView toolbarName,name,id,email,dept,phone;
    private CircleImageView image;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private StudentHomeViewModel viewModel;
    private Student student;

    // Dependency Injection

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_student);
        findSection();
        viewModel = new ViewModelProvider(getViewModelStore(),providerFactory).get(StudentHomeViewModel.class);
        viewModel.getStudentInfo();
        observeStudentInfo();
    }

    private void observeStudentInfo() {
        viewModel.observeStudentInfo().observe(this, new Observer<Student>() {
            @Override
            public void onChanged(Student newStudent) {
                progressBar.setVisibility(View.GONE);
                student= newStudent;
                toolbarName.setText(student.getName());
                requestManager.load(student.getImage()).into(image);
                name.setText(student.getName());
                id.setText(student.getId());
                email.setText(student.getEmail());
                dept.setText("Dept. of "+student.getDepartment()+" | "+student.getSection());
                phone.setText(student.getPhone());

            }
        });
    }


    private void findSection() {
        // set toolbar
        toolbar= findViewById(R.id.profileStudentToolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarName= findViewById(R.id.profileStudentToolbarNameId);
        name= findViewById(R.id.profileStudentNameId);
        id= findViewById(R.id.profileStudentUserId);
        email= findViewById(R.id.profileStudentEmailId);
        dept= findViewById(R.id.profileStudentDeptId);
        phone= findViewById(R.id.profileStudentMobileId);
        edit= findViewById(R.id.studentEditButtonId);
        edit.setOnClickListener(this);
        image= findViewById(R.id.profileStudentImageId);
        progressBar= findViewById(R.id.profileStudentProgressId);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.studentEditButtonId){
            Intent intent= new Intent(ProfileStudent.this,StudentEditProfile.class);
            intent.putExtra("editStudent",student);
            startActivity(intent);
        }

    }
}