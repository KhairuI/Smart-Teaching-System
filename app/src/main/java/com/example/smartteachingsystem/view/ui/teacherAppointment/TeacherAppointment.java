package com.example.smartteachingsystem.view.ui.teacherAppointment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.Response;
import com.example.smartteachingsystem.view.model.StudentApp;
import com.example.smartteachingsystem.view.model.Token;
import com.example.smartteachingsystem.view.notification.APIService;
import com.example.smartteachingsystem.view.notification.Data;
import com.example.smartteachingsystem.view.notification.MyResponse;
import com.example.smartteachingsystem.view.notification.NotificationSender;
import com.example.smartteachingsystem.view.ui.chatting.TeacherChattingActivity;
import com.example.smartteachingsystem.view.utils.CheckInternet;
import com.example.smartteachingsystem.view.utils.NoInternetDialogue;
import com.example.smartteachingsystem.view.utils.StateResource;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class TeacherAppointment extends DaggerAppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView name,id,email,dept,phone,counseling,status,reason;
    private ProgressBar progressBar;
    private TextInputEditText editText;
    private Button approve, decline;
    private CircleImageView imageView;
    private ImageView sentMessage;
    private StudentApp studentApp;
    private TeacherAppointmentViewModel viewModel;
    String result="";

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
        setContentView(R.layout.activity_teacher_appointment);
        findSection();

        studentApp= (StudentApp) getIntent().getSerializableExtra("studentAppointment");
        setInfo();
        viewModel= new ViewModelProvider(getViewModelStore(),providerFactory).get(TeacherAppointmentViewModel.class);
        observeResponse();

    }

    private void observeToken() {
        viewModel.observeTeacherToken().observe(this, new Observer<Token>() {
            @Override
            public void onChanged(Token token) {
                sendNotification(token.getToken(),token.getName());
            }
        });
    }

    private boolean Check(){

        return CheckInternet.connect(this);
    }

    private void sendNotification(String token, String name) {
        Data data= new Data(name+" "+result+" your appointment",editText.getText().toString());
        NotificationSender sender= new NotificationSender(data,token);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
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

    private void observeResponse() {
        viewModel.observeTeacherResponse().observe(this, new Observer<StateResource>() {
            @Override
            public void onChanged(StateResource stateResource) {
                if(stateResource!= null){
                    switch (stateResource.status){
                        case LOADING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case SUCCESS:
                            progressBar.setVisibility(View.INVISIBLE);
                            showSnackBar("Response Successfully");
                            viewModel.getToken(studentApp.getuId());
                            observeToken();
                            finish();
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
        if(studentApp.getStatus().equals("Request")){

            status.setText(studentApp.getStatus());
            status.setTextColor(Color.RED);
        }
        else if(studentApp.getStatus().equals("Approve")){
            reason.setText("Your response");
            status.setText("Already "+studentApp.getStatus());
            status.setTextColor(Color.GREEN);
            approve.setEnabled(false);
            decline.setEnabled(false);
        }
        else if(studentApp.getStatus().equals("Decline")){
            reason.setText("Your response");
            status.setText("Already "+studentApp.getStatus());
            status.setTextColor(Color.RED);
            approve.setEnabled(false);
            decline.setEnabled(false);
        }
        requestManager.load(studentApp.getImage()).into(imageView);
        name.setText(studentApp.getName());
        id.setText(studentApp.getId());
        email.setText(studentApp.getEmail());
        dept.setText(studentApp.getDept()+" | Section: "+studentApp.getSection());
        phone.setText(studentApp.getPhone());
        counseling.setText(studentApp.getMessage());
    }

    private void findSection() {
        // set toolbar
        toolbar= findViewById(R.id.teacherAppointmentToolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name= findViewById(R.id.teacherAppointmentNameId);
        status= findViewById(R.id.teacherAppointmentStatusId);
        id= findViewById(R.id.teacherAppointmentUniversityId);
        imageView= findViewById(R.id.teacherAppointmentImageId);
        email= findViewById(R.id.teacherAppointmentEmailId);
        dept= findViewById(R.id.teacherAppointmentDeptId);
        reason= findViewById(R.id.reasonTitleId);
        progressBar= findViewById(R.id.teacherAppointmentProgressId);
        progressBar.setVisibility(View.GONE);
        phone= findViewById(R.id.teacherAppointmentMobileId);
        counseling= findViewById(R.id.counselingReasonId);
        editText= findViewById(R.id.teacherCommentTextId);
        approve= findViewById(R.id.teacherAppointmentApproveButtonId);
        approve.setOnClickListener(this);
        decline= findViewById(R.id.teacherAppointmentDeclineButtonId);
        decline.setOnClickListener(this);
        sentMessage= findViewById(R.id.iv_sent_message);
        sentMessage.setOnClickListener(this);

        ImageView call= findViewById(R.id.teacherAppCallId);
        call.setOnClickListener(this);
        ImageView sentEmail= findViewById(R.id.teacherAppEmailId);
        sentEmail.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.teacherAppointmentApproveButtonId){
            if(TextUtils.isEmpty(editText.getText().toString())){
                editText.setError("Please enter a comment");
            }
            else {
                boolean isConnect= Check();
                if(isConnect){
                    result="approve";
                    Response response= new Response("Approve",editText.getText().toString(),studentApp.getPushKey(),studentApp.getuId());
                    viewModel.teacherResponse(response);
                }
                else {
                    NoInternetDialogue dialogue= new NoInternetDialogue();
                    dialogue.show(getSupportFragmentManager(),"no_internet");
                }

            }

        }
        else if(view.getId()==R.id.teacherAppointmentDeclineButtonId){

            if(TextUtils.isEmpty(editText.getText().toString())){
                editText.setError("Please enter a comment");
            }
            else {
                boolean isConnect= Check();
                if(isConnect){
                    result="decline";
                    Response response= new Response("Decline",editText.getText().toString(),studentApp.getPushKey(),studentApp.getuId());
                    viewModel.teacherResponse(response);
                }
                else {
                    NoInternetDialogue dialogue= new NoInternetDialogue();
                    dialogue.show(getSupportFragmentManager(),"no_internet");
                }
            }
        }
        else if(view.getId()==R.id.teacherAppCallId){
            String number= "tel:"+phone.getText().toString();
            Intent intent= new Intent(Intent.ACTION_DIAL, Uri.parse(number));
            startActivity(intent);
        }
        else if(view.getId()==R.id.teacherAppEmailId){

            String to= email.getText().toString();
            String[] makeTo= to.split(",");
            Intent intent= new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL,makeTo);
            intent.putExtra(Intent.EXTRA_SUBJECT,"Reply of Appointment");
            intent.putExtra(Intent.EXTRA_TEXT,"You will come in time");
            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent,"Choose an email client"));

        }
        else if(view.getId()==R.id.iv_sent_message){
            Intent intent= new Intent(this, TeacherChattingActivity.class);
            intent.putExtra("student_uid",studentApp.getuId());
            startActivity(intent);
        }
    }

    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }
}