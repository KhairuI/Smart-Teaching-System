package com.example.smartteachingsystem.view.ui.studentEdit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.ui.profileStudent.ProfileStudentViewModel;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.textfield.TextInputEditText;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class StudentEditProfile extends DaggerAppCompatActivity {

    // Declare all view..
    private Spinner studentEditSpinner;
    private TextView studentEditDept,toolbarText;
    private CircleImageView studentEditImage;
    private TextInputEditText studentEditName,studentEditId,studentEditEmail,studentEditMobile,studentEditSection;
    private Button studentEditUpdateButton;
    private ProgressBar studentEditProgress;
    private Student student;
    private StudentEditViewModel viewModel;
    private Toolbar toolbar;

    // Dependency Injection
    @Inject
    RequestManager requestManager;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit_profile);
        findSection();
        setSpinners();
        viewModel = new ViewModelProvider(getViewModelStore(),providerFactory)
                .get(StudentEditViewModel.class);
        student= (Student) getIntent().getSerializableExtra("editStudent");
        setInfo();
    }

    private void setSpinners() {
        //set the spinner
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this,R.layout.custom_spinner,getResources()
                .getStringArray(R.array.department));
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        studentEditSpinner.setAdapter(adapter);
        studentEditSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value= adapterView.getItemAtPosition(i).toString();
                studentEditDept.setText(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setInfo() {

        requestManager.load(student.getImage()).into(studentEditImage);
        studentEditName.setText(student.getName());
        studentEditId.setText(student.getId());
        studentEditEmail.setText(student.getEmail());
        studentEditDept.setText(student.getDepartment());
        studentEditMobile.setText(student.getPhone());
        studentEditSection.setText(student.getSection());
        toolbarText.setText("Edit Profile");
    }

    private void findSection() {
        // set toolbar
        toolbar= findViewById(R.id.studentEditProfileToolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        studentEditSpinner= findViewById(R.id.studentEditSpinnerId);
        toolbarText= findViewById(R.id.studentEditToolbarTextId);
        studentEditDept= findViewById(R.id.studentEditDeptId);
        studentEditImage= findViewById(R.id.studentEditImageId);
        studentEditName= findViewById(R.id.studentEditNameId);
        studentEditId= findViewById(R.id.studentEditUniversityId);
        studentEditEmail= findViewById(R.id.studentEditEmailId);
        studentEditMobile= findViewById(R.id.studentEditMobileId);
        studentEditSection= findViewById(R.id.studentEditSectionId);
        studentEditUpdateButton= findViewById(R.id.studentEditUpdateButtonId);
        studentEditProgress= findViewById(R.id.studentEditProgressId);
        studentEditProgress.setVisibility(View.GONE);
    }
}