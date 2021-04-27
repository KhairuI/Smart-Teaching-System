package com.example.smartteachingsystem.view.ui.profileTeacher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.Teacher;
import com.example.smartteachingsystem.view.ui.teacherEdit.TeacherEditProfile;
import com.example.smartteachingsystem.view.ui.teacherHome.TeacherHomeViewModel;
import com.example.smartteachingsystem.view.utils.StateResource;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileTeacher extends DaggerAppCompatActivity implements View.OnClickListener {

    private FloatingActionButton edit;
    private TextInputEditText updateEditText;
    private TextView toolbarName,name,id,email,dept,phone,office,counseling,update;
    private CircleImageView image;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private TeacherHomeViewModel viewModel;
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

        viewModel = new ViewModelProvider(getViewModelStore(),providerFactory).get(TeacherHomeViewModel.class);
        viewModel.getTeacherInfo();
        observeTeacherInfo();
        observeCounseling();

    }

    private void observeCounseling() {
        viewModel.observeCounseling().observe(this, new Observer<StateResource>() {
            @Override
            public void onChanged(StateResource stateResource) {
                if(stateResource!= null){
                    switch (stateResource.status){
                        case LOADING:

                            break;
                        case SUCCESS:
                            showSnackBar("Update Successfully");
                            viewModel.getTeacherInfo();

                            break;
                        case ERROR:

                            showSnackBar(stateResource.message);
                            break;
                    }
                }
            }
        });
    }

    private void observeTeacherInfo() {
        viewModel.observeTeacherInfo().observe(this, new Observer<Teacher>() {
            @Override
            public void onChanged(Teacher newTeacher) {
                progressBar.setVisibility(View.GONE);
                teacher= newTeacher;
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
        });
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
        progressBar= findViewById(R.id.teacherProfileProgressId);
        update.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.teacherEditButtonId){
            Intent intent= new Intent(ProfileTeacher.this, TeacherEditProfile.class);
            intent.putExtra("editTeacher",teacher);
            startActivity(intent);
        }
        else if(view.getId()==R.id.updateCounselingTextId){

            openDialogue();

        }
    }

    private void openDialogue() {

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        LayoutInflater inflater= getLayoutInflater();
        final View view= inflater.inflate(R.layout.update_counseling,null);
        builder.setView(view).setTitle("Update Counseling").setCancelable(true).setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(!TextUtils.isEmpty(updateEditText.getText().toString())){
                    viewModel.updateCounseling(updateEditText.getText().toString());
                }
                else {
                    showSnackBar("Enter Text");
                }
            }
        }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        updateEditText= view.findViewById(R.id.updateTeacherCounselingId);
        builder.create().show();
    }

    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }
}