package com.example.smartteachingsystem.view.ui.teacherProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.Teacher;
import com.example.smartteachingsystem.view.ui.studentProfile.StudentProfileViewModel;
import com.example.smartteachingsystem.view.view.TeacherAppointment;
import com.example.smartteachingsystem.view.view.TeacherEditProfile;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherProfile extends DaggerAppCompatActivity {
    // declare all views...
    private CircleImageView teacherProfileImage;
    private TextView teacherName, teacherId,teacherEmail,teacherDept,teacherPhone,teacherOffice;
    private RecyclerView recyclerView;
    private TeacherProfileViewModel teacherProfileViewModel;


    // Dependency Injection

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);
        setToolbar();
        findSection();
        teacherProfileViewModel= new ViewModelProvider(getViewModelStore(),providerFactory).get(TeacherProfileViewModel.class);
        teacherProfileViewModel.getTeacherInfo();
        observeTeacherInfo();
    }

    private void observeTeacherInfo() {
        teacherProfileViewModel.observeTeacherInfo().observe(this, new Observer<Teacher>() {
            @Override
            public void onChanged(Teacher teacher) {
                requestManager.load(teacher.getImage()).into(teacherProfileImage);
                teacherName.setText(teacher.getName());
                teacherId.setText(teacher.getId());
                teacherEmail.setText(teacher.getEmail());
                teacherDept.setText(teacher.getDesignation()+" Dept. of "+teacher.getDepartment());
                teacherPhone.setText(teacher.getPhone());
                teacherOffice.setText(teacher.getOffice());
            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar= findViewById(R.id.teacherProfileToolbarId);
        setSupportActionBar(toolbar);
    }

    private void findSection() {

        teacherProfileImage= findViewById(R.id.teacherProfileImageId);
        teacherName= findViewById(R.id.teacherProfileNameId);
        teacherId= findViewById(R.id.teacherProfileUniversityId);
        teacherEmail= findViewById(R.id.teacherProfileEmailId);
        teacherDept= findViewById(R.id.teacherProfileDeptId);
        teacherPhone= findViewById(R.id.teacherProfileMobileId);
        teacherOffice= findViewById(R.id.teacherProfileOfficeId);
        recyclerView= findViewById(R.id.teacherRecycleViewId);
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
            Intent intent= new Intent(TeacherProfile.this, TeacherEditProfile.class);
            startActivity(intent);

        }
        else if(item.getItemId()==R.id.logoutId){
            Toast.makeText(TeacherProfile.this,"Logout",Toast.LENGTH_SHORT).show();

        }
        else if(item.getItemId()==R.id.aboutId){
            Intent intent= new Intent(TeacherProfile.this, TeacherAppointment.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}