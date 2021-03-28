package com.example.smartteachingsystem.view.ui.studentRegister;

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
import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.ui.studentHome.StudentHome;
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

public class StudentRegister extends DaggerAppCompatActivity implements View.OnClickListener {

    // Declare all view..
    private Spinner studentSpinner;
    private TextView studentDept;
    private CircleImageView studentImage;
    private TextInputEditText studentName,studentId,studentEmail,studentMobile,studentSection;
    private Button studentRegisterButton;
    private ProgressBar studentProgress;
    private Uri insertImageUri= null;
    private StudentRegisterViewModel studentRegisterViewModel;
    private Bitmap bitmap;

    //Rx variable
    Observable<Boolean> formObservable;

    // Dependency Injection
    @Inject
    ViewModelProviderFactory modelProviderFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);
        setToolbar();
        findSection();
        setSpinners();
        formValidation();
        studentRegisterViewModel= new ViewModelProvider(getViewModelStore(),modelProviderFactory).get(StudentRegisterViewModel.class);

        studentObserve();
    }

    private void studentObserve() {
        studentRegisterViewModel.observeSetStudentData().observe(this, new Observer<StateResource>() {
            @Override
            public void onChanged(StateResource stateResource) {
                if(stateResource!= null){
                    switch (stateResource.status){
                        case LOADING:
                            studentProgress.setVisibility(View.VISIBLE);
                            break;
                        case SUCCESS:
                            studentProgress.setVisibility(View.INVISIBLE);
                            goToStudentProfile();
                            break;
                        case ERROR:
                            studentProgress.setVisibility(View.INVISIBLE);
                            showSnackBar(stateResource.message);
                            break;
                    }
                }
            }
        });
    }

    private void goToStudentProfile() {
        Intent intent = new Intent(StudentRegister.this, StudentHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void setSpinners() {
        //set the spinner
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this,R.layout.custom_spinner,getResources()
                .getStringArray(R.array.department));
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        studentSpinner.setAdapter(adapter);
        studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value= adapterView.getItemAtPosition(i).toString();
                studentDept.setText(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    // form validate.....
    private void formValidation() {
        Observable<String> name_observable = RxBindingHelper.getObservableFrom(studentName);
        Observable<String> id_observable = RxBindingHelper.getObservableFrom(studentId);
        Observable<String> email_observable = RxBindingHelper.getObservableFrom(studentEmail);
        Observable<String> phone_observable = RxBindingHelper.getObservableFrom(studentMobile);
        Observable<String> section_observable = RxBindingHelper.getObservableFrom(studentSection);

        formObservable= Observable.combineLatest(name_observable, id_observable, email_observable, phone_observable, section_observable, new Function5<String, String, String, String, String, Boolean>() {
            @Override
            public Boolean apply(String name, String id, String email, String phone, String section) throws Throwable {
                return isValidForm(name, id, email, phone, section);
            }
        });

        formObservable.subscribe(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                studentRegisterButton.setEnabled(aBoolean);
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
            studentName.setError("Please enter name");
        }
        boolean isId = !id.isEmpty();
        if(!isId){
            studentId.setError("Please enter university Id");
        }

        boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.isEmpty();
        if (!isEmail) {
            studentEmail.setError("Please enter valid email");
        }

        boolean isPhone = phone.length() == 11 && !TextUtils.isEmpty(phone);
        if (!isPhone) {
            studentMobile.setError("Phone number should be 11 digit");
        }

        boolean isSection = !section.isEmpty();
        if(!isSection){
            studentSection.setError("Please enter university Id");
        }

        return isName && isId && isEmail && isPhone && isSection;
    }

    private void findSection() {
        studentSpinner= findViewById(R.id.studentSpinnerId);
        studentDept= findViewById(R.id.studentDeptId);
        studentImage= findViewById(R.id.studentImageId);
        studentImage.setOnClickListener(this);
        studentName= findViewById(R.id.studentNameId);
        studentId= findViewById(R.id.studentUniversityId);
        studentEmail= findViewById(R.id.studentEmailId);
        studentMobile= findViewById(R.id.studentMobileId);
        studentSection= findViewById(R.id.studentSectionId);
        studentRegisterButton= findViewById(R.id.studentRegisterButtonId);
        studentRegisterButton.setOnClickListener(this);
        studentProgress= findViewById(R.id.studentProgressId);
        studentProgress.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.studentRegisterButtonId){
            if(bitmap != null){
                sentData();
            }
            else {
                showSnackBar("Please upload image");
            }

        }
        else if(view.getId()==R.id.studentImageId){

            showGallery();
        }
    }

    private void sentData() {
        // get all data...
        String name= studentName.getText().toString();
        String id= studentId.getText().toString();
        String email= studentEmail.getText().toString();
        String mobile= studentMobile.getText().toString();
        String section= studentSection.getText().toString();
        String image= "image";
        String department= studentDept.getText().toString();
        Student student= new Student(name,id,email,mobile,department,section,image);
        studentRegisterViewModel.setStudentData(student,bitmap);
    }

    // take user permission.....
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

    // pick image......
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
                if(insertImageUri!=null){
                    studentImage.setImageURI(insertImageUri);
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


    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }

    private void setToolbar() {
        Toolbar toolbar= findViewById(R.id.studentRegToolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Student Register");

    }
}