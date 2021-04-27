package com.example.smartteachingsystem.view.ui.teacherEdit;

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
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.Teacher;
import com.example.smartteachingsystem.view.ui.profileTeacher.ProfileTeacher;
import com.example.smartteachingsystem.view.ui.studentEdit.StudentEditViewModel;
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
import io.reactivex.rxjava3.functions.Function6;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class TeacherEditProfile extends DaggerAppCompatActivity implements View.OnClickListener {

    // Declare all view..
    private CircleImageView teacherImage;
    private TextInputEditText teacherName,teacherId,teacherEmail,teacherMobile,teacherOffice,teacherInitial;
    private Spinner deptSpinner,designationSpinner;
    private TextView teacherEditDept,teacherEditDesignation,toolbarText;
    private Button teacherUpdateButton,addDept,addDesignation;
    private ProgressBar teacherProgress;
    private Toolbar toolbar;
    private Uri insertImageUri= null;
    private Bitmap bitmap;
    private Teacher teacher;
    //Rx variable
    Observable<Boolean> formObservable;

    private TeacherEditViewModel viewModel;

    // Dependency Injection
    @Inject
    RequestManager requestManager;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_edit_profile);
        findSection();
        setSpinners();
        formValidation();
        teacher= (Teacher) getIntent().getSerializableExtra("editTeacher");
        setInfo();
        viewModel = new ViewModelProvider(getViewModelStore(),providerFactory)
                .get(TeacherEditViewModel.class);
       updateObserver();
    }

    private void updateObserver() {
        viewModel.observeUpdateTeacher().observe(this, new Observer<StateResource>() {
            @Override
            public void onChanged(StateResource stateResource) {
                if(stateResource!= null){
                    switch (stateResource.status){
                        case LOADING:
                            teacherProgress.setVisibility(View.VISIBLE);
                            break;
                        case SUCCESS:
                            teacherProgress.setVisibility(View.INVISIBLE);
                            finish();
                            break;
                        case ERROR:
                            teacherProgress.setVisibility(View.INVISIBLE);
                            showSnackBar(stateResource.message);
                            break;
                    }
                }
            }
        });
    }

    private void goToTeacherProfile() {
        Intent intent= new Intent(TeacherEditProfile.this, ProfileTeacher.class);
        startActivity(intent);
    }


    private void setInfo() {
        requestManager.load(teacher.getImage()).into(teacherImage);
        teacherName.setText(teacher.getName());
        teacherId.setText(teacher.getId());
        teacherEmail.setText(teacher.getEmail());
        teacherMobile.setText(teacher.getPhone());
        teacherOffice.setText(teacher.getOffice());
        teacherInitial.setText(teacher.getInitial());
        teacherEditDept.setText(teacher.getDepartment());
        teacherEditDesignation.setText(teacher.getDesignation());
        toolbarText.setText("Edit Profile");

    }

    private void setSpinners() {

        //designation spinner...
        ArrayAdapter<String> adapter1= new ArrayAdapter<>(this,R.layout.custom_spinner,getResources()
                .getStringArray(R.array.designation));
        adapter1.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        designationSpinner.setAdapter(adapter1);

        //department spinner...

        ArrayAdapter<String> adapter= new ArrayAdapter<>(this,R.layout.custom_spinner,getResources()
                .getStringArray(R.array.department));
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        deptSpinner.setAdapter(adapter);


    }

    private void findSection() {
        toolbar= findViewById(R.id.teacherEditProfileToolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        teacherImage= findViewById(R.id.teacherEditImageId);
        deptSpinner= findViewById(R.id.deptTeacherEditSpinnerId);
        designationSpinner= findViewById(R.id.DesignationTeacherEditSpinnerId);
        teacherName= findViewById(R.id.teacherEditNameId);
        teacherEmail= findViewById(R.id.teacherEditEmailId);
        teacherMobile= findViewById(R.id.teacherEditMobileId);
        teacherOffice= findViewById(R.id.teacherEditOfficeId);
        teacherId= findViewById(R.id.teacherEditUniversityId);
        teacherInitial= findViewById(R.id.teacherEditInitialId);
        teacherEditDept= findViewById(R.id.teacherEditDeptId);
        teacherEditDesignation= findViewById(R.id.teacherEditDesignationId);
        teacherUpdateButton= findViewById(R.id.teacherUpdateButtonId);
        teacherProgress= findViewById(R.id.teacherEditProgressId);
        teacherProgress.setVisibility(View.GONE);
        toolbarText= findViewById(R.id.teacherEditToolbarTextId);
        addDept= findViewById(R.id.editAddDeptId);
        addDesignation= findViewById(R.id.editAddDesignationId);

        teacherUpdateButton.setOnClickListener(this);
        addDept.setOnClickListener(this);
        addDesignation.setOnClickListener(this);
        teacherImage.setOnClickListener(this);

    }

    // form validation....
    private void formValidation() {
        Observable<String> name_observable = RxBindingHelper.getObservableFrom(teacherName);
        Observable<String> id_observable = RxBindingHelper.getObservableFrom(teacherId);
        Observable<String> email_observable = RxBindingHelper.getObservableFrom(teacherEmail);
        Observable<String> phone_observable = RxBindingHelper.getObservableFrom(teacherMobile);
        Observable<String> office_observable = RxBindingHelper.getObservableFrom(teacherOffice);
        Observable<String> initial_observable = RxBindingHelper.getObservableFrom(teacherInitial);

        formObservable= Observable.combineLatest(name_observable, id_observable, email_observable, phone_observable, office_observable, initial_observable, new Function6<String, String, String, String, String, String, Boolean>() {
            @Override
            public Boolean apply(String name, String id, String email, String phone, String office, String initial) throws Throwable {
                return isValidForm(name,id,email,phone,office,initial);
            }
        });

        formObservable.subscribe(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                teacherUpdateButton.setEnabled(aBoolean);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private Boolean isValidForm(String name, String id, String email, String phone,String office,String initial) {
        boolean isName = !name.isEmpty();
        if(!isName){
            teacherName.setError("Please enter name");
        }
        boolean isId = !id.isEmpty();
        if(!isId){
            teacherId.setError("Please enter university Id");
        }

        boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.isEmpty();
        if (!isEmail) {
            teacherEmail.setError("Please enter valid email");
        }

        boolean isPhone = phone.length() == 11 && !TextUtils.isEmpty(phone);
        if (!isPhone) {
            teacherMobile.setError("Phone number should be 11 digit");
        }

        boolean isOffice = !office.isEmpty();
        if (!isOffice) {
            teacherOffice.setError("Please enter office room");
        }
        boolean isInitial = !initial.isEmpty();
        if (!isInitial) {
            teacherInitial.setError("Please enter office room");
        }

        return isName && isId && isEmail && isPhone && isOffice && isInitial;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.teacherEditImageId){
            showGallery();
        }
        else if(view.getId()==R.id.editAddDeptId){

            teacherEditDept.setText(deptSpinner.getSelectedItem().toString());
        }
        else if(view.getId()==R.id.editAddDesignationId){
            teacherEditDesignation.setText(designationSpinner.getSelectedItem().toString());
        }
        else if(view.getId()==R.id.teacherUpdateButtonId){
            // get all data...
            String name= teacherName.getText().toString();
            String id= teacherId.getText().toString();
            String email= teacherEmail.getText().toString();
            String mobile= teacherMobile.getText().toString();
            String designation= teacherEditDesignation.getText().toString();
            String department= teacherEditDept.getText().toString();
            String office= teacherOffice.getText().toString();
            String initial= teacherInitial.getText().toString();
            Teacher teacher= new Teacher(name,id,email,mobile,department,designation,"image",office,"Not upload yet",initial);

            if(bitmap==null){
                viewModel.updateWithoutImage(teacher);
            }
            else {
                viewModel.updateWithImage(teacher,bitmap);
            }
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
                    teacherImage.setImageURI(insertImageUri);
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