package com.example.smartteachingsystem.view.ui.profileTeacher;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.Teacher;
import com.example.smartteachingsystem.view.ui.teacherEdit.TeacherEditProfile;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileTeacher extends DaggerAppCompatActivity implements View.OnClickListener {

    private FloatingActionButton edit;
    private TextView toolbarName,name,id,email,dept,phone,office,counseling,update;
    private CircleImageView image;
    private Toolbar toolbar;
    private ProfileTeacherViewModel viewModel;
    private Teacher teacher;

    // Dependency Injection

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_teacher);
        findSection();

        viewModel = new ViewModelProvider(getViewModelStore(),providerFactory).get(ProfileTeacherViewModel.class);
        teacher= (Teacher) getIntent().getSerializableExtra("teacher");
        setInfo();
    }

    private void setInfo() {

        toolbarName.setText(teacher.getName());
        requestManager.load(teacher.getImage()).into(image);
        name.setText(teacher.getName()+" ("+teacher.getInitial()+")");
        id.setText(teacher.getId());
        email.setText(teacher.getEmail());
        dept.setText(teacher.getDesignation()+" Dept. of "+teacher.getDepartment());
        phone.setText(teacher.getPhone());
        office.setText(teacher.getOffice());
        counseling.setText(teacher.getCounseling());
    }

    private void findSection() {
        // set toolbar
        toolbar= findViewById(R.id.profileTeacherToolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image= findViewById(R.id.profileTeacherImageId);
        toolbarName= findViewById(R.id.profileTeacherToolbarNameId);
        name= findViewById(R.id.profileTeacherNameId);
        id= findViewById(R.id.profileTeacherUserId);
        edit= findViewById(R.id.teacherEditButtonId);
        edit.setOnClickListener(this);

        email= findViewById(R.id.profileTeacherEmailId);
        dept= findViewById(R.id.profileTeacherDeptId);
        phone= findViewById(R.id.profileTeacherMobileId);
        office= findViewById(R.id.profileTeacherOfficeId);
        counseling= findViewById(R.id.teacherCounselingScheduleId);
        update= findViewById(R.id.updateCounselingTextId);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.teacherEditButtonId){
            Intent intent= new Intent(ProfileTeacher.this, TeacherEditProfile.class);
            startActivity(intent);
        }
        else if(view.getId()==R.id.updateCounselingTextId){


        }
    }
}