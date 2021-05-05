package com.example.smartteachingsystem.view.ui.studentHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.adapter.StudentAppAdapter;
import com.example.smartteachingsystem.view.adapter.StudentAppointmentAdapter;
import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.model.TeacherApp;
import com.example.smartteachingsystem.view.ui.login.LoginActivity;
import com.example.smartteachingsystem.view.ui.profileStudent.ProfileStudent;
import com.example.smartteachingsystem.view.ui.teacherList.TeacherList;
import com.example.smartteachingsystem.view.utils.CheckInternet;
import com.example.smartteachingsystem.view.utils.DetailsDialogue;
import com.example.smartteachingsystem.view.utils.NoInternetDialogue;
import com.example.smartteachingsystem.view.utils.Resource;
import com.example.smartteachingsystem.view.utils.StateResource;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class StudentHome extends DaggerAppCompatActivity implements View.OnClickListener,StudentAppAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {
    // declare all views...
    private CircleImageView profileImage;
    private TextView profileName, studentId;
    private FloatingActionButton button;
    private ProgressBar progressBar;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private List<TeacherApp> newList= new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;

    private Student newStudent;
    private StudentHomeViewModel studentHomeViewModel;


    // Dependency Injection

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    StudentAppAdapter adapter;

    @Inject
    RequestManager requestManager;

   /* @Inject
    StudentAppointmentAdapter adapter;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        findSection();
        studentHomeViewModel = new ViewModelProvider(getViewModelStore(),providerFactory).get(StudentHomeViewModel.class);
        studentHomeViewModel.getStudentInfo();
        studentHomeViewModel.getStudent();
        observeStudentInfo();
        observeStudent();
        setRecycleView();
        updateToken();

    }

    private void updateToken() {

        String value= FirebaseInstanceId.getInstance().getToken();
        studentHomeViewModel.setToken(value);
    }

    private void observeStudent() {

        studentHomeViewModel.observeStudent().observe(this, new Observer<List<TeacherApp>>() {
            @Override
            public void onChanged(List<TeacherApp> list) {
                newList= list;
                progressBar.setVisibility(View.GONE);
                adapter.setList(newList);
            }
        });
    }

    private void setRecycleView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        /*adapter.setOnItemClickListener(this);
        adapter.startListening();*/

    }

    private void observeStudentInfo() {
        studentHomeViewModel.observeStudentInfo().observe(this, new Observer<Student>() {
            @Override
            public void onChanged(Student student) {
                newStudent= student;
                requestManager.load(student.getImage()).into(profileImage);
                profileName.setText(student.getName());
                studentId.setText(student.getId());
            }
        });
    }

    private void findSection() {
        toolbar= findViewById(R.id.studentProfileToolbarId);
        setSupportActionBar(toolbar);
        profileImage= findViewById(R.id.studentProfileImageId);
        profileName= findViewById(R.id.studentProfileNameId);
        progressBar= findViewById(R.id.studentHomeProgressId);
        searchView= findViewById(R.id.studentHomeSearchId);
        searchView.setOnQueryTextListener(this);
        studentId= findViewById(R.id.studentProfileUniversityId);
        refreshLayout= findViewById(R.id.studentHomeRefreshId);
        refreshLayout.setOnRefreshListener(this);
        recyclerView= findViewById(R.id.studentRecycleViewId);
        button= findViewById(R.id.studentInsertId);
        button.setOnClickListener(this);

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
            boolean isConnect= Check();
            if(isConnect){
                Intent intent= new Intent(StudentHome.this, ProfileStudent.class);
                startActivity(intent);
            }
            else {
                NoInternetDialogue dialogue= new NoInternetDialogue();
                dialogue.show(getSupportFragmentManager(),"no_internet");
            }


        }
        else if(item.getItemId()==R.id.logoutId){
            boolean isConnect= Check();
            if(isConnect){
                studentHomeViewModel.logout();
                goToLoginActivity();
            }
            else {
                NoInternetDialogue dialogue= new NoInternetDialogue();
                dialogue.show(getSupportFragmentManager(),"no_internet");
            }


        }
        else if(item.getItemId()==R.id.aboutId){
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            LayoutInflater inflater= getLayoutInflater();
            final View view= inflater.inflate(R.layout.about_dialogue,null);
            builder.setView(view).setTitle("About").setCancelable(true).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).create().show();

        }
        return super.onOptionsItemSelected(item);
    }
    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.studentInsertId){
            boolean isConnect= Check();
            if(isConnect){
                Intent intent= new Intent(StudentHome.this, TeacherList.class);
                startActivity(intent);
            }
            else {
                NoInternetDialogue dialogue= new NoInternetDialogue();
                dialogue.show(getSupportFragmentManager(),"no_internet");
            }
        }
    }
    private void goToLoginActivity() {
        Intent intent = new Intent(StudentHome.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(int position) {

        openDialogue(newList.get(position));
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
                boolean isConnect= Check();
                if(isConnect){
                    String value= newList.get(position).getStatus();
                    if(value.equals("Pending")){
                        showSnackBar("Pending appointment can not deleted");
                    }
                    else {
                        studentHomeViewModel.studentDelete(key);
                        observeDelete(position);
                    }
                }
                else{
                    NoInternetDialogue dialogue= new NoInternetDialogue();
                    dialogue.show(getSupportFragmentManager(),"no_internet");
                }



            }
        }).create().show();

    }

    private void observeDelete(int position) {
        studentHomeViewModel.observeStudentDelete().observe(this, new Observer<StateResource>() {
            @Override
            public void onChanged(StateResource stateResource) {
                switch (stateResource.status){
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        showSnackBar(stateResource.message);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        newList.remove(position);
                        adapter.notifyItemRemoved(position);
                        showSnackBar("Delete successfully");
                        break;
                }
            }
        });
    }

    private void openDialogue(TeacherApp teacherApp) {

        DetailsDialogue detailsDialogue= new DetailsDialogue(teacherApp);
        detailsDialogue.show(getSupportFragmentManager(),"Details Dialogue");
    }


    @Override
    public void onRefresh() {
        newList.clear();
        studentHomeViewModel.getStudent();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }


    private boolean Check(){

        return CheckInternet.connect(this);
    }

}