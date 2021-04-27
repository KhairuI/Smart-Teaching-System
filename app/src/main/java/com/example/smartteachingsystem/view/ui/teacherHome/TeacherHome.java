package com.example.smartteachingsystem.view.ui.teacherHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.adapter.TeacherAppAdapter;
import com.example.smartteachingsystem.view.adapter.TeacherAppointmentAdapter;
import com.example.smartteachingsystem.view.model.StudentApp;
import com.example.smartteachingsystem.view.model.Teacher;
import com.example.smartteachingsystem.view.model.TeacherApp;
import com.example.smartteachingsystem.view.ui.login.LoginActivity;
import com.example.smartteachingsystem.view.ui.profileTeacher.ProfileTeacher;
import com.example.smartteachingsystem.view.ui.teacherAppointment.TeacherAppointment;
import com.example.smartteachingsystem.view.utils.StateResource;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherHome extends DaggerAppCompatActivity implements TeacherAppAdapter.OnItemClickListener {
    // declare all views...
    private CircleImageView teacherProfileImage;
    private TextView teacherName, teacherId;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TeacherHomeViewModel teacherHomeViewModel;
    private Teacher newTeacher;
    private List<StudentApp> newList= new ArrayList<>();


    // Dependency Injection

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;

    @Inject
    TeacherAppAdapter adapter;

  /*  @Inject
    TeacherAppointmentAdapter adapter;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        setToolbar();
        findSection();
        teacherHomeViewModel = new ViewModelProvider(getViewModelStore(),providerFactory).get(TeacherHomeViewModel.class);
        teacherHomeViewModel.getTeacherInfo();
        observeTeacherInfo();
        teacherHomeViewModel.getStudentAppointment();
        observeStudentAppointment();
        setRecycleView();
        updateToken();
    }

    private void observeStudentAppointment() {
        teacherHomeViewModel.observeStudentAppointment().observe(this, new Observer<List<StudentApp>>() {
            @Override
            public void onChanged(List<StudentApp> studentApps) {
                newList= studentApps;
                progressBar.setVisibility(View.GONE);
                adapter.setList(newList);
            }
        });

    }

    private void updateToken() {
        String value= FirebaseInstanceId.getInstance().getToken();
        teacherHomeViewModel.setToken(value);
    }

    private void setRecycleView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
      /*  adapter.setOnItemClickListener(this);
        adapter.startListening();*/
    }

    private void observeTeacherInfo() {
        teacherHomeViewModel.observeTeacherInfo().observe(this, new Observer<Teacher>() {
            @Override
            public void onChanged(Teacher teacher) {
                newTeacher= teacher;
                requestManager.load(teacher.getImage()).into(teacherProfileImage);
                teacherName.setText(teacher.getName());
                teacherId.setText(teacher.getId());
            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar= findViewById(R.id.teacherProfileToolbarId);
        setSupportActionBar(toolbar);
    }

    private void findSection() {

        teacherProfileImage= findViewById(R.id.teacherProfileImageId);
        teacherName= findViewById(R.id.teacherProfileNameId);
        teacherId= findViewById(R.id.teacherProfileUniversityId);
        recyclerView= findViewById(R.id.teacherRecycleViewId);
        progressBar= findViewById(R.id.teacherHomeProgressId);
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

            Intent intent= new Intent(TeacherHome.this, ProfileTeacher.class);
            startActivity(intent);

        }
        else if(item.getItemId()==R.id.logoutId){
            teacherHomeViewModel.logout();
            goToLoginActivity();
        }
        else if(item.getItemId()==R.id.aboutId){
            showSnackBar("About");

        }
        return super.onOptionsItemSelected(item);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(TeacherHome.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

   /* @Override
    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
        StudentApp studentApp= documentSnapshot.toObject(StudentApp.class);
        Intent intent= new Intent(TeacherHome.this,TeacherAppointment.class);
        intent.putExtra("studentAppointment",studentApp);
        startActivity(intent);
        //String name= documentSnapshot.getString("name");
        //showSnackBar(name);
    }*/


    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(int position) {
        StudentApp studentApp= newList.get(position);
        Intent intent= new Intent(TeacherHome.this,TeacherAppointment.class);
        intent.putExtra("studentAppointment",studentApp);
        startActivity(intent);
    }

    @Override
    public void onLongItemClick(int position) {

        String key= newList.get(position).getPushKey();
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Delete").setIcon(R.drawable.ic_delete).setMessage("Do you want to delete ?")
                .setCancelable(true).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value= newList.get(position).getStatus();
                if(value.equals("Request")){
                    showSnackBar("Request appointment can not deleted");
                }
                else {
                    teacherHomeViewModel.teacherDelete(key);
                    observeDelete(position);
                }

            }
        }).create().show();

    }

    private void observeDelete(int position) {
        teacherHomeViewModel.observeTeacherDelete().observe(this, new Observer<StateResource>() {
            @Override
            public void onChanged(StateResource stateResource) {
                switch (stateResource.status){
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        newList.remove(position);
                        adapter.notifyItemRemoved(position);
                        showSnackBar("Delete successfully");
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        showSnackBar(stateResource.message);
                        break;
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }
}