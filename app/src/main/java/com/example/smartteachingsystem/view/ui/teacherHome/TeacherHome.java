package com.example.smartteachingsystem.view.ui.teacherHome;

import androidx.annotation.NonNull;
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

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.Teacher;
import com.example.smartteachingsystem.view.ui.login.LoginActivity;
import com.example.smartteachingsystem.view.view.TeacherAppointment;
import com.example.smartteachingsystem.view.view.TeacherEditProfile;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherHome extends DaggerAppCompatActivity {
    // declare all views...
    private CircleImageView teacherProfileImage;
    private TextView teacherName, teacherId;
    private RecyclerView recyclerView;
    private TeacherHomeViewModel teacherHomeViewModel;


    // Dependency Injection

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        setToolbar();
        findSection();
        teacherHomeViewModel = new ViewModelProvider(getViewModelStore(),providerFactory).get(TeacherHomeViewModel.class);
        teacherHomeViewModel.getTeacherInfo();
        observeTeacherInfo();
    }

    private void observeTeacherInfo() {
        teacherHomeViewModel.observeTeacherInfo().observe(this, new Observer<Teacher>() {
            @Override
            public void onChanged(Teacher teacher) {
                requestManager.load(teacher.getImage()).into(teacherProfileImage);
                teacherName.setText(teacher.getName());
                teacherId.setText(teacher.getId());
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
            Intent intent= new Intent(TeacherHome.this, TeacherEditProfile.class);
            startActivity(intent);

        }
        else if(item.getItemId()==R.id.logoutId){
            teacherHomeViewModel.logout();
            goToLoginActivity();
        }
        else if(item.getItemId()==R.id.aboutId){
            Intent intent= new Intent(TeacherHome.this, TeacherAppointment.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(TeacherHome.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}