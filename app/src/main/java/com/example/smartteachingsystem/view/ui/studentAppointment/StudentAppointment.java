package com.example.smartteachingsystem.view.ui.studentAppointment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.TeacherApp;
import com.example.smartteachingsystem.view.model.Teacher_List;
import com.example.smartteachingsystem.view.model.Token;
import com.example.smartteachingsystem.view.notification.APIService;
import com.example.smartteachingsystem.view.notification.Data;
import com.example.smartteachingsystem.view.notification.MyResponse;
import com.example.smartteachingsystem.view.notification.NotificationSender;
import com.example.smartteachingsystem.view.ui.studentRegister.StudentRegisterViewModel;
import com.example.smartteachingsystem.view.utils.RxBindingHelper;
import com.example.smartteachingsystem.view.utils.StateResource;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.BiFunction;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentAppointment extends DaggerAppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private CircleImageView image;
    private TextView name,id,initial,email,department, phone,office,counseling;
    private TextInputEditText editText;
    private ProgressBar progressBar;
    private Button button;
    private Teacher_List list;
    private StudentAppointmentViewModel appointmentViewModel;


    // Dependency Injection
    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;

    @Inject
    APIService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_appointment);
        findSection();
        list= (Teacher_List) getIntent().getSerializableExtra("key");
        setInfo();

        appointmentViewModel= new ViewModelProvider(getViewModelStore(),providerFactory).get(StudentAppointmentViewModel.class);
        appointmentObserve();


    }

    private void tokenObserver() {
        appointmentViewModel.observeStudentToken().observe(this, new Observer<Token>() {
            @Override
            public void onChanged(Token token) {
                sendNotification(token.getToken(),token.getName());
            }
        });
    }

    private void sendNotification(String token, String name) {
        Data data= new Data(name+" request for appointment",editText.getText().toString());
        NotificationSender sender= new NotificationSender(data,token);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if(response.code()==200){
                    if(response.body().success != 1){
                        //showSnackBar("failed");
                    }
                    else {
                     //   showSnackBar("Success");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                showSnackBar("Request Fail");
            }
        });
    }


    private void appointmentObserve() {
        appointmentViewModel.observeStudentAppointment().observe(this, new Observer<StateResource>() {
            @Override
            public void onChanged(StateResource stateResource) {
                if(stateResource!= null){
                    switch (stateResource.status){
                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case SUCCESS:
                            progressBar.setVisibility(View.INVISIBLE);
                            showSnackBar("Submit Successfully");
                            appointmentViewModel.getToken(list.getuId());
                            tokenObserver();
                            finish();
                            //goToStudentProfile();
                            break;
                        case ERROR:
                            progressBar.setVisibility(View.INVISIBLE);
                            showSnackBar(stateResource.message);
                            break;
                    }
                }
            }
        });

    }

    private void setInfo() {
        requestManager.load(list.getImage()).into(image);
        name.setText(list.getName());
        id.setText(list.getId());
        initial.setText("Teacher initial: "+list.getInitial());
        email.setText(list.getEmail());
        department.setText(list.getDesignation()+" Dept. of "+list.getDepartment());
        phone.setText(list.getPhone());
        office.setText(list.getOffice());
        counseling.setText(list.getCounseling());

    }

    private void findSection() {
        toolbar= findViewById(R.id.studentAppointmentToolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image= findViewById(R.id.studentAppointmentImageId);
        name= findViewById(R.id.studentAppointmentNameId);
        id= findViewById(R.id.studentAppointmentUniversityId);
        initial= findViewById(R.id.studentAppointmentInitialId);
        email= findViewById(R.id.studentAppointmentEmailId);
        department= findViewById(R.id.studentAppointmentDeptId);
        phone= findViewById(R.id.studentAppointmentMobileId);
        office= findViewById(R.id.studentAppointmentOfficeId);
        counseling= findViewById(R.id.counselingScheduleId);
        progressBar= findViewById(R.id.studentAppointmentProgressId);
        progressBar.setVisibility(View.GONE);
        editText= findViewById(R.id.studentAppointmentTopicsId);
        button= findViewById(R.id.studentAppointmentButtonId);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.studentAppointmentButtonId){
            if (TextUtils.isEmpty(editText.getText().toString())){
                editText.setError("Enter appointment topic");
            }
            else {
                String message= editText.getText().toString();
                String status= "Pending";
                String name= list.getName();
                String id= list.getId();
                String uId= list.getuId();
                String pushKey= randomDigit();
                String email= list.getEmail();
                String dept= list.getDepartment();
                String image= list.getImage();
                String office= list.getOffice();
                String initial= list.getInitial();
                String designation= list.getDesignation();
                String phone= list.getPhone();

                TeacherApp teacher= new TeacherApp(status,message,name,id,uId,pushKey,email,dept,image,office,initial,designation,phone);
                appointmentViewModel.setStudentAppointment(teacher);
            }

        }

    }

    private void makeData(String value) {

    }

    //generate a random digit.........
    private String randomDigit() {

        char[] chars= "1234567890".toCharArray();
        StringBuilder stringBuilder= new StringBuilder();
        Random random= new Random();
        for(int i=0;i<5;i++){
            char c= chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }
}