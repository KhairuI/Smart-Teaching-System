package com.example.smartteachingsystem.view.ui.studentEdit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.example.smartteachingsystem.view.ui.profileStudent.ProfileStudent;
import com.example.smartteachingsystem.view.ui.profileStudent.ProfileStudentViewModel;
import com.example.smartteachingsystem.view.ui.profileTeacher.ProfileTeacher;
import com.example.smartteachingsystem.view.ui.teacherEdit.TeacherEditProfile;
import com.example.smartteachingsystem.view.utils.DataConverter;
import com.example.smartteachingsystem.view.utils.RxBindingHelper;
import com.example.smartteachingsystem.view.utils.StateResource;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function5;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class StudentEditProfile extends DaggerAppCompatActivity implements View.OnClickListener {

    // Declare all view..
    private Spinner studentEditSpinner;
    private TextView studentEditDept,toolbarText;
    private CircleImageView studentEditImage;
    private TextInputEditText studentEditName,studentEditId,studentEditEmail,studentEditMobile,studentEditSection;
    private Button studentEditUpdateButton,addDeptButton;
    private ProgressBar studentEditProgress;
    private Student student;
    private StudentEditViewModel viewModel;
    private Toolbar toolbar;
    private Uri insertImageUri= null;
    private Bitmap bitmap;

    //Rx variable
    Observable<Boolean> formObservable;

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
        formValidation();
        student= (Student) getIntent().getSerializableExtra("editStudent");
        setInfo();

        viewModel = new ViewModelProvider(getViewModelStore(),providerFactory)
                .get(StudentEditViewModel.class);
        updateObserver();
    }

    private void updateObserver() {
        viewModel.observeUpdateStudent().observe(this, new Observer<StateResource>() {
            @Override
            public void onChanged(StateResource stateResource) {
                if(stateResource!= null){
                    switch (stateResource.status){
                        case LOADING:
                            studentEditProgress.setVisibility(View.VISIBLE);
                            break;
                        case SUCCESS:
                            studentEditProgress.setVisibility(View.INVISIBLE);
                            finish();
                            break;
                        case ERROR:
                            studentEditProgress.setVisibility(View.INVISIBLE);
                            showSnackBar(stateResource.message);
                            break;
                    }
                }
            }
        });
    }

    private void goToStudentProfile() {
        Intent intent= new Intent(StudentEditProfile.this, ProfileStudent.class);
        startActivity(intent);
        finish();
    }

    // form validate.....
    private void formValidation() {
        Observable<String> name_observable = RxBindingHelper.getObservableFrom(studentEditName);
        Observable<String> id_observable = RxBindingHelper.getObservableFrom(studentEditId);
        Observable<String> email_observable = RxBindingHelper.getObservableFrom(studentEditEmail);
        Observable<String> phone_observable = RxBindingHelper.getObservableFrom(studentEditMobile);
        Observable<String> section_observable = RxBindingHelper.getObservableFrom(studentEditSection);

        formObservable= Observable.combineLatest(name_observable, id_observable, email_observable, phone_observable, section_observable, new Function5<String, String, String, String, String, Boolean>() {
            @Override
            public Boolean apply(String name, String id, String email, String phone, String section) throws Throwable {
                return isValidForm(name, id, email, phone, section);
            }
        });

        formObservable.subscribe(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                studentEditUpdateButton.setEnabled(aBoolean);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    private Boolean isValidForm(String name, String id,String email, String phone, String section) {
        boolean isName = !name.isEmpty();
        if(!isName){
            studentEditName.setError("Please enter name");
        }
        boolean isId = !id.isEmpty();
        if(!isId){
            studentEditId.setError("Please enter university Id");
        }

        boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.isEmpty();
        if (!isEmail) {
            studentEditEmail.setError("Please enter valid email");
        }

        boolean isPhone = phone.length() == 11 && !TextUtils.isEmpty(phone);
        if (!isPhone) {
            studentEditMobile.setError("Phone number should be 11 digit");
        }

        boolean isSection = !section.isEmpty();
        if(!isSection){
            studentEditSection.setError("Please enter university Id");
        }

        return isName && isId && isEmail && isPhone && isSection;
    }

    private void setSpinners() {
        //set the spinner
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this,R.layout.custom_spinner,getResources()
                .getStringArray(R.array.department));
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        studentEditSpinner.setAdapter(adapter);
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
        addDeptButton= findViewById(R.id.editStudentAddDeptId);

        studentEditUpdateButton.setOnClickListener(this);
        addDeptButton.setOnClickListener(this);
        studentEditImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.editStudentAddDeptId){

            studentEditDept.setText(studentEditSpinner.getSelectedItem().toString());
        }
        else if(view.getId()==R.id.studentEditUpdateButtonId){

            String name= studentEditName.getText().toString();
            String id= studentEditId.getText().toString();
            String email= studentEditEmail.getText().toString();
            String phone= studentEditMobile.getText().toString();
            String dept= studentEditDept.getText().toString();
            String section= studentEditSection.getText().toString();
            Student student= new Student(name,id,email,phone,dept,section,"image");
            if(bitmap==null){
                viewModel.updateWithoutImage(student);
            }
            else {
                viewModel.updateWithImage(student,bitmap);
            }

        }
        else if(view.getId()==R.id.studentEditImageId){
            showGallery();
        }
    }

    private void showGallery() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

            }
            else {
                imagePick();
            }

        }
        else {
            imagePick();
        }
    }

    private void imagePick() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                assert result != null;
                insertImageUri= result.getUri();
                if(insertImageUri!= null){
                    studentEditImage.setImageURI(insertImageUri);
                    convertToByte(insertImageUri);
                }

            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                Exception error = result.getError();
                showSnackBar(error.toString());
            }
        }


    }


    private void convertToByte(Uri insertImageUri) {
        File imageFile = new File(DataConverter.getRealPathFromURI(insertImageUri,this));
        try {

            bitmap = new Compressor(getApplication()).compressToBitmap(imageFile);
        }
        catch (Exception e){
            showSnackBar(e.toString());
        }
    }

    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }
}