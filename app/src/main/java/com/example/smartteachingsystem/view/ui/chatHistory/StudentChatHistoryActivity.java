package com.example.smartteachingsystem.view.ui.chatHistory;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.adapter.ChatHistoryAdapter;
import com.example.smartteachingsystem.view.model.ChatUser;
import com.example.smartteachingsystem.view.model.StudentChatUser;
import com.example.smartteachingsystem.view.ui.chatting.ChatViewModel;
import com.example.smartteachingsystem.view.ui.chatting.StudentChattingActivity;
import com.example.smartteachingsystem.view.ui.studentHome.StudentHome;
import com.example.smartteachingsystem.view.utils.SharedPrefUtils;
import com.example.smartteachingsystem.view.utils.StateResource;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import dagger.android.support.DaggerAppCompatActivity;

public class StudentChatHistoryActivity extends DaggerAppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,ChatHistoryAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView btnRefresh;
    private TextView noRecord;
    private EditText editText;
    private SwipeRefreshLayout refreshLayout;
    private ChatViewModel chatViewModel;
    private List<StudentChatUser> newList= new ArrayList<>();
    private SharedPrefUtils prefUtils;

    // Dependency Injection
    @Inject
    ViewModelProviderFactory modelProviderFactory;

    @Inject
    ChatHistoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);
        prefUtils= new SharedPrefUtils(this);
        findSection();
        chatViewModel= new ViewModelProvider(getViewModelStore(),modelProviderFactory).get(ChatViewModel.class);
        chatViewModel.getHistoryList();
        observeHistory();
        setRecycleView();

    }

    private void setRecycleView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setUserInfo(prefUtils.getCurrentUId());
        adapter.setOnItemClickListener(this);
    }

    private void observeHistory() {
        chatViewModel.observeChatHistory().observe(this, new Observer<List<StudentChatUser>>() {
            @Override
            public void onChanged(List<StudentChatUser> chatUsers) {
                progressBar.setVisibility(View.GONE);
                if(chatUsers.size()==0){
                    noRecord.setVisibility(View.VISIBLE);
                }
                else {
                    newList= chatUsers;
                    adapter.setList(newList);
                }
            }
        });
    }

    private void findSection() {
        Toolbar toolbar = findViewById(R.id.chatHistoryToolbarId);
        setSupportActionBar(toolbar);

        recyclerView= findViewById(R.id.recyclerViewHistory);
        progressBar= findViewById(R.id.chatHistoryProgressId);
        noRecord= findViewById(R.id.txt_no_record);
        refreshLayout= findViewById(R.id.history_refresh);
        refreshLayout.setOnRefreshListener(this);
        editText= findViewById(R.id.edt_history);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    Toast.makeText(StudentChatHistoryActivity.this,"empty",Toast.LENGTH_SHORT).show();
                    adapter.setList(newList);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
                else {
                    Toast.makeText(StudentChatHistoryActivity.this,charSequence.toString(),Toast.LENGTH_SHORT).show();
                    adapter.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnRefresh= findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newList.clear();
                chatViewModel.getHistoryList();
            }
        });
        ImageView back= findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentChatHistoryActivity.this, StudentHome.class));
            }
        });
    }

    @Override
    public void onRefresh() {
        newList.clear();
        chatViewModel.getHistoryList();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(String uId) {
        Intent intent= new Intent(this, StudentChattingActivity.class);
        intent.putExtra("teacher_uid",uId);
        startActivity(intent);
    }

    @Override
    public void onLongItemClick(String uId,int position) {
        showDialogue(uId,position);
    }

    private void showDialogue(String uId,int position) {
        MaterialAlertDialogBuilder builder= new MaterialAlertDialogBuilder(this);
        builder.setTitle("Delete").setIcon(R.drawable.ic_delete).setMessage("Do you want to delete ?")
                .setBackground(AppCompatResources.getDrawable(this,R.drawable.dialogue_bg)).setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chatViewModel.chatDelete(uId);
                observeDelete(position);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();


    }

    private void observeDelete(int position) {
        chatViewModel.observeChatDelete().observe(this, new Observer<StateResource>() {
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

    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        newList.clear();
        chatViewModel.getHistoryList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        newList.clear();
        chatViewModel.getHistoryList();
    }
}