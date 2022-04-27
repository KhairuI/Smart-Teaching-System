package com.example.smartteachingsystem.view.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.ui.studentRegister.StudentRegister;
import com.example.smartteachingsystem.view.ui.teacherRegister.TeacherRegister;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class RegisterActivity extends DaggerAppCompatActivity {

    private TextView student, teacher;


    // Dependency Injection
    @Inject
    ViewModelProviderFactory modelProviderFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findSection();
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(RegisterActivity.this, StudentRegister.class);
                startActivity(intent);

            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(RegisterActivity.this, TeacherRegister.class);
                startActivity(intent);

            }
        });
    }

    private void findSection() {
        student= findViewById(R.id.studentTextId);
        teacher= findViewById(R.id.teacherTextId);
    }
}