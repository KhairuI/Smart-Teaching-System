package com.example.smartteachingsystem.view.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smartteachingsystem.R;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentEditProfile extends AppCompatActivity {

    // Declare all view..
    private Spinner studentEditSpinner;
    private TextView studentEditDept;
    private CircleImageView studentEditImage;
    private TextInputEditText studentEditName,studentEditId,studentEditEmail,studentEditMobile,studentEditSection;
    private Button studentEditUpdateButton;
    private ProgressBar studentEditProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit_profile);
        findSection();
    }

    private void findSection() {
        studentEditSpinner= findViewById(R.id.studentEditSpinnerId);
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