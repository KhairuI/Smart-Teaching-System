package com.example.smartteachingsystem.view.ui.studentProfile;

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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.ui.login.LoginViewModel;
import com.example.smartteachingsystem.view.view.StudentAppointment;
import com.example.smartteachingsystem.view.view.StudentEditProfile;
import com.example.smartteachingsystem.view.view.TeacherList;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfile extends DaggerAppCompatActivity implements View.OnClickListener {
    // declare all views...
    private CircleImageView profileImage;
    private TextView profileName, studentId,studentEmail,studentDept,studentPhone;
    private FloatingActionButton button;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private StudentProfileViewModel studentProfileViewModel;


    // Dependency Injection

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        findSection();
        studentProfileViewModel= new ViewModelProvider(getViewModelStore(),providerFactory).get(StudentProfileViewModel.class);
        studentProfileViewModel.getStudentInfo();
        observeStudentInfo();
    }

    private void observeStudentInfo() {
        studentProfileViewModel.observeStudentInfo().observe(this, new Observer<Student>() {
            @Override
            public void onChanged(Student student) {
                requestManager.load(student.getImage()).into(profileImage);
                profileName.setText(student.getName());
                studentId.setText(student.getId());
                studentEmail.setText(student.getEmail());
                studentDept.setText(student.getDepartment()+"|"+"Section: "+student.getSection());
                studentPhone.setText(student.getPhone());
            }
        });
    }

    private void findSection() {
        toolbar= findViewById(R.id.studentProfileToolbarId);
        setSupportActionBar(toolbar);
        profileImage= findViewById(R.id.studentProfileImageId);
        profileName= findViewById(R.id.studentProfileNameId);
        studentId= findViewById(R.id.studentProfileUniversityId);
        studentEmail= findViewById(R.id.studentProfileEmailId);
        studentDept= findViewById(R.id.studentProfileDeptId);
        studentPhone= findViewById(R.id.studentProfileMobileId);
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
            Intent intent= new Intent(StudentProfile.this, StudentEditProfile.class);
            startActivity(intent);

        }
        else if(item.getItemId()==R.id.logoutId){
            Toast.makeText(StudentProfile.this,"Logout",Toast.LENGTH_SHORT).show();

        }
        else if(item.getItemId()==R.id.aboutId){
            Intent intent= new Intent(StudentProfile.this, StudentAppointment.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.studentInsertId){
            Intent intent= new Intent(StudentProfile.this, TeacherList.class);
            startActivity(intent);
        }
    }
}