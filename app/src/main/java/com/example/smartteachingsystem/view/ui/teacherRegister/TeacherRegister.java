package com.example.smartteachingsystem.view.ui.teacherRegister;

import androidx.annotation.Nullable;
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

import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.Teacher;
import com.example.smartteachingsystem.view.utils.DataConverter;
import com.example.smartteachingsystem.view.utils.RxBindingHelper;
import com.example.smartteachingsystem.view.utils.StateResource;
import com.example.smartteachingsystem.view.ui.teacherProfile.TeacherProfile;
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
import io.reactivex.rxjava3.functions.Function4;
import io.reactivex.rxjava3.functions.Function5;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class TeacherRegister extends DaggerAppCompatActivity implements View.OnClickListener {

    // Declare all view..
    private CircleImageView teacherImage;
    private TextInputEditText teacherName,teacherId,teacherEmail,teacherMobile,teacherOffice;
    private Spinner deptSpinner,designationSpinner;
    private TextView teacherDept,teacherDesignation;
    private Button teacherRegisterButton;
    private ProgressBar teacherProgress;
    private Uri insertImageUri= null;
    private Bitmap bitmap;
    private TeacherRegisterViewModel teacherRegisterViewModel;

    //Rx variable
    Observable<Boolean> formObservable;

    // Dependency Injection
    @Inject
    ViewModelProviderFactory modelProviderFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);
        setToolbar();
        findSection();
        setSpinners();
        formValidation();

        teacherRegisterViewModel= new ViewModelProvider(getViewModelStore(),modelProviderFactory).get(TeacherRegisterViewModel.class);

        studentObserve();

    }

    private void studentObserve() {
        teacherRegisterViewModel.observeSetTeacherData().observe(this, new Observer<StateResource>() {
            @Override
            public void onChanged(StateResource stateResource) {
                if(stateResource!= null){
                    switch (stateResource.status){
                        case LOADING:
                            teacherProgress.setVisibility(View.VISIBLE);
                            break;
                        case SUCCESS:
                            teacherProgress.setVisibility(View.INVISIBLE);
                            goToTeacherProfile();
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
        Intent intent = new Intent(TeacherRegister.this, TeacherProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void formValidation() {
        Observable<String> name_observable = RxBindingHelper.getObservableFrom(teacherName);
        Observable<String> id_observable = RxBindingHelper.getObservableFrom(teacherId);
        Observable<String> email_observable = RxBindingHelper.getObservableFrom(teacherEmail);
        Observable<String> phone_observable = RxBindingHelper.getObservableFrom(teacherMobile);
        Observable<String> office_observable = RxBindingHelper.getObservableFrom(teacherOffice);

       formObservable= Observable.combineLatest(name_observable, id_observable, email_observable, phone_observable,office_observable, new Function5<String, String, String,String, String, Boolean>() {
           @Override
           public Boolean apply(String name, String id, String email, String phone,String office) throws Throwable {
               return isValidForm(name, id, email, phone,office);
           }
       });

       formObservable.subscribe(new DisposableObserver<Boolean>() {
           @Override
           public void onNext(@NonNull Boolean aBoolean) {
               teacherRegisterButton.setEnabled(aBoolean);
           }

           @Override
           public void onError(@NonNull Throwable e) {

           }

           @Override
           public void onComplete() {

           }
       });
    }

    private Boolean isValidForm(String name, String id, String email, String phone,String office) {
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

        return isName && isId && isEmail && isPhone && isOffice;
    }


    private void setSpinners() {

        //designation spinner...
        ArrayAdapter<String> adapter1= new ArrayAdapter<>(this,R.layout.custom_spinner,getResources()
                .getStringArray(R.array.designation));
        adapter1.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        designationSpinner.setAdapter(adapter1);
        designationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value= adapterView.getItemAtPosition(i).toString();
                teacherDesignation.setText(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //department spinner...

        ArrayAdapter<String> adapter= new ArrayAdapter<>(this,R.layout.custom_spinner,getResources()
                .getStringArray(R.array.department));
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        deptSpinner.setAdapter(adapter);
        deptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value= adapterView.getItemAtPosition(i).toString();
                teacherDept.setText(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void findSection() {
        teacherImage= findViewById(R.id.teacherImageId);
        teacherImage.setOnClickListener(this);
        teacherName= findViewById(R.id.teacherNameId);
        teacherId= findViewById(R.id.teacherUniversityId);
        teacherEmail= findViewById(R.id.teacherEmailId);
        teacherMobile= findViewById(R.id.teacherMobileId);
        deptSpinner= findViewById(R.id.deptSpinnerId);
        designationSpinner= findViewById(R.id.DesignationSpinnerId);
        teacherDept= findViewById(R.id.teacherDeptId);
        teacherDesignation= findViewById(R.id.teacherDesignationId);
        teacherOffice= findViewById(R.id.teacherOfficeId);
        teacherRegisterButton= findViewById(R.id.teacherRegisterButtonId);
        teacherRegisterButton.setOnClickListener(this);
        teacherProgress= findViewById(R.id.teacherProgressId);
        teacherProgress.setVisibility(View.GONE);


    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.teacherRegisterButtonId){
            if(bitmap != null){
                sentData();
            }
            else {
                showSnackBar("Please upload image");
            }

        }
        else if(view.getId()==R.id.teacherImageId){
            showGallery();
        }
    }

    private void sentData() {
        // get all data...
        String name= teacherName.getText().toString();
        String id= teacherId.getText().toString();
        String email= teacherEmail.getText().toString();
        String mobile= teacherMobile.getText().toString();
        String designation= teacherDesignation.getText().toString();
        String department= teacherDept.getText().toString();
        String office= teacherOffice.getText().toString();
        Teacher teacher= new Teacher(name,id,email,mobile,department,designation,"image",office,"Not upload yet");
        teacherRegisterViewModel.setTeacherData(teacher,bitmap);

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
                insertImageUri= result.getUri();
                if(insertImageUri!= null){
                    teacherImage.setImageURI(insertImageUri);
                    convertToByte(insertImageUri);
                }

            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
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


    private void setToolbar() {
        Toolbar toolbar= findViewById(R.id.teacherRegisterToolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Teacher Register");
    }

    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }
}