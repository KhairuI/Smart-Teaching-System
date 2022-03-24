package com.example.smartteachingsystem.view.ui.chatting;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.adapter.ChattingAdapter;
import com.example.smartteachingsystem.view.model.ChatUser;
import com.example.smartteachingsystem.view.model.Message;
import com.example.smartteachingsystem.view.model.StudentChatUser;
import com.example.smartteachingsystem.view.model.StudentMessage;
import com.example.smartteachingsystem.view.model.TeacherChatUser;
import com.example.smartteachingsystem.view.model.TeacherMessage;
import com.example.smartteachingsystem.view.model.Teacher_List;
import com.example.smartteachingsystem.view.ui.chatHistory.StudentChatHistoryActivity;
import com.example.smartteachingsystem.view.ui.studentAppointment.StudentAppointment;
import com.example.smartteachingsystem.view.utils.SharedPrefUtils;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class StudentChattingActivity extends DaggerAppCompatActivity implements View.OnClickListener {

    // declare all views...
    private ImageView selectImage;
    private ImageView voiceImage;
    private ImageView photoImage;
    private EditText messageEditText;
    private RecyclerView chattingRecycle;
    private TextView chattingName,chattingInfo,noConversation;
    private ProgressBar progressBar;
    private CircleImageView chattingImage;
    private int isVisible=0;
    private String msg;
    private Animation openAnim,closeAnim;
    private String teacherUid;
    private ChatViewModel chatViewModel;
    private Teacher_List teacher;
    private SharedPrefUtils prefUtils;
    private final ChattingAdapter adapter= new ChattingAdapter();

    // Dependency Injection
    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        prefUtils= new SharedPrefUtils(this);
        findSection();
        teacherUid= getIntent().getStringExtra("teacher_uid");
        chatViewModel = new ViewModelProvider(getViewModelStore(),providerFactory).get(ChatViewModel.class);
        chatViewModel.getTeacherInfoInChatting(teacherUid);
        chatViewModel.getMessageList(prefUtils.getCurrentUId()+"_"+teacherUid);
        observeTeacherInfo();
        observeMessageList();
        observeNewMessage();
        // get teacher info


    }

    private void observeMessageList() {
        chatViewModel.observeAllMessage().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                progressBar.setVisibility(View.GONE);
                    adapter.setMessageList(messages);
                    chattingRecycle.setAdapter(adapter);
                    if (adapter.getItemCount()>1){
                        chattingRecycle.smoothScrollToPosition(adapter.getItemCount()-1);
                    }
            }
        });

    }

    private void observeNewMessage() {

        chatViewModel.observeNewMessage().observe(this, new Observer<Message>() {
            @Override
            public void onChanged(Message message) {
                adapter.setNewMessage(message);
                chattingRecycle.smoothScrollToPosition(adapter.getItemCount()-1);
            }
        });
    }

    private void observeTeacherInfo() {
        chatViewModel.observeTeacherInfo().observe(this, new Observer<Teacher_List>() {
            @Override
            public void onChanged(Teacher_List mTeacher) {
                teacher= mTeacher;
                requestManager.load(teacher.getImage()).into(chattingImage);
                chattingName.setText(teacher.getName());
                chattingName.setSelected(true);
                chattingInfo.setText(teacher.getDesignation()+" Dept. of "+teacher.getDepartment());
                adapter.setUserInfo(prefUtils.getCurrentUId(),requestManager);

            }
        });
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.select_image_icon){
            if(isVisible==0){
                selectImage.setRotation(45F);
                voiceImage.startAnimation(openAnim);
                photoImage.startAnimation(openAnim);
                isVisible=1;
            }
            else {

                selectImage.setRotation(0F);
                voiceImage.startAnimation(closeAnim);
                photoImage.startAnimation(closeAnim);
                isVisible=0;
            }

        }
        else if(view.getId()==R.id.voice_image){

        }
        else if(view.getId()==R.id.photo_image){

            Log.d("mymsg", "onClick: "+demoMethod());
        }
        else if(view.getId()==R.id.message_sent_btn){

            sendMessage();
        }
        else if(view.getId()==R.id.chat_profile){

            Intent intent= new Intent(this, StudentAppointment.class);
            intent.putExtra("key",teacher);
            startActivity(intent);
        }

    }

    private void sendMessage() {
        msg= messageEditText.getText().toString();
        if(!TextUtils.isEmpty(msg)){
            Message message= new Message(getCurrentDateAnsTime(),"text",msg,"image",prefUtils.getCurrentUId());
            ChatUser chatUser= new ChatUser(getCurrentDateAnsTime(),msg,"text",prefUtils.getImage(),teacherUid,prefUtils.getCurrentUId(),prefUtils.getName());
            Log.d("mymsg", "Student Chatting Activity -> current UId: "+prefUtils.getCurrentUId());
            Log.d("mymsg", "Student Chatting Activity -> teacher UId: "+teacherUid);

            StudentChatUser studentChatUser= new StudentChatUser(getCurrentDateAnsTime(),msg,"text",teacher.getImage(),
                    prefUtils.getCurrentUId(),teacherUid,teacher.getName());

            TeacherChatUser teacherChatUser= new TeacherChatUser(getCurrentDateAnsTime(),msg,"text",prefUtils.getImage(),
                    prefUtils.getCurrentUId(),prefUtils.getCurrentUId(),prefUtils.getName());

            chatViewModel.sendStudentMessage(message,studentChatUser, teacherChatUser);
            messageEditText.setText("");
            observeMessageList();
           // chatViewModel.getMessageList(prefUtils.getCurrentUId()+"_"+teacherUid);
        }
    }

    private String getCurrentDateAnsTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return df.format(Calendar.getInstance().getTime());
     /*   DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.US);
        return df.format(Calendar.getInstance().getTime());*/
    }

    private String demoMethod(){
       /* Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Log.d("mymsg", "timestamp: "+timestamp.toString());*/
             DateFormat df = new SimpleDateFormat("dd-MMM-yy hh:mm a", Locale.US);
        return df.format(Calendar.getInstance().getTime());
    }

    private void findSection() {
        Toolbar toolbar = findViewById(R.id.studentChattingToolbarId);
        setSupportActionBar(toolbar);

        selectImage= findViewById(R.id.select_image_icon);
        selectImage.setOnClickListener(this);

        voiceImage= findViewById(R.id.voice_image);
        voiceImage.setOnClickListener(this);

        photoImage= findViewById(R.id.photo_image);
        photoImage.setOnClickListener(this);

        ImageView messageSendBtn = findViewById(R.id.message_sent_btn);
        messageSendBtn.setOnClickListener(this);

        ImageView chatProfile = findViewById(R.id.chat_profile);
        chatProfile.setOnClickListener(this);

        progressBar= findViewById(R.id.chatting_progress);

        messageEditText= findViewById(R.id.edt_msg_chatting);
        chattingImage= findViewById(R.id.chatting_icon_image);
        chattingName= findViewById(R.id.tv_chatting_name);
        chattingInfo= findViewById(R.id.tv_chatting_info);
        noConversation= findViewById(R.id.tv_no_conversation);

        chattingRecycle= findViewById(R.id.chattingRecycleView);
        chattingRecycle.setHasFixedSize(true);
        chattingRecycle.setItemAnimator(new DefaultItemAnimator());

        ImageView back= findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentChattingActivity.this, StudentChatHistoryActivity.class));
            }
        });

        // animation
        openAnim= AnimationUtils.loadAnimation(this,R.anim.fab_open);
        closeAnim= AnimationUtils.loadAnimation(this,R.anim.fab_close);
    }

    private void showSnackBar(String message) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG).show();
    }
}