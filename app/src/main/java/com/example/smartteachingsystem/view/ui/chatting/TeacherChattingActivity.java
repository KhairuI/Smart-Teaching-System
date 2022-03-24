package com.example.smartteachingsystem.view.ui.chatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.adapter.ChattingAdapter;
import com.example.smartteachingsystem.view.model.ChatUser;
import com.example.smartteachingsystem.view.model.Message;
import com.example.smartteachingsystem.view.model.Student;
import com.example.smartteachingsystem.view.model.StudentChatUser;
import com.example.smartteachingsystem.view.model.StudentMessage;
import com.example.smartteachingsystem.view.model.TeacherChatUser;
import com.example.smartteachingsystem.view.model.TeacherMessage;
import com.example.smartteachingsystem.view.utils.SharedPrefUtils;
import com.example.smartteachingsystem.view.viewModel.ViewModelProviderFactory;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherChattingActivity extends DaggerAppCompatActivity implements View.OnClickListener {
    private String studentUid;
    private SharedPrefUtils prefUtils;

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
    private Student student;
    private Animation openAnim,closeAnim;
    private ChatViewModel chatViewModel;
    private final ChattingAdapter adapter= new ChattingAdapter();

    // Dependency Injection
    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestManager requestManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_chatting);
        prefUtils= new SharedPrefUtils(this);
        findSection();
        studentUid= getIntent().getStringExtra("student_uid");
        Log.d("mymsg", "Teacher chatting: "+studentUid);
        chatViewModel = new ViewModelProvider(getViewModelStore(),providerFactory).get(ChatViewModel.class);
        chatViewModel.getStudentInfoInChatting(studentUid);
        chatViewModel.getMessageList(prefUtils.getCurrentUId()+"_"+studentUid);
        observeStudentInfo();
        observeMessageList();
        observeNewMessage();

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



    private void observeStudentInfo() {
        chatViewModel.observeStudentInfo().observe(this, new Observer<Student>() {
            @Override
            public void onChanged(Student newStudent) {
                if(newStudent != null){
                    student= newStudent;
                    requestManager.load(student.getImage()).into(chattingImage);
                    chattingName.setText(student.getName());
                    chattingName.setSelected(true);
                    chattingInfo.setText("Dept. of "+student.getDepartment());
                    adapter.setUserInfo(prefUtils.getCurrentUId(),requestManager);
                }
                else {
                    Log.d("mymsg", "onChanged: Info null");
                }

            }
        });
    }

    private void findSection() {
        Toolbar toolbar = findViewById(R.id.teacherChattingToolbarId);
        setSupportActionBar(toolbar);

        selectImage= findViewById(R.id.select_image_icon);
        selectImage.setOnClickListener(this);

        voiceImage= findViewById(R.id.voice_image);
        voiceImage.setOnClickListener(this);

        photoImage= findViewById(R.id.photo_image);
        photoImage.setOnClickListener(this);
        chattingImage= findViewById(R.id.chatting_icon_image);
        chattingName= findViewById(R.id.tv_chatting_name);
        chattingInfo= findViewById(R.id.tv_chatting_info);
        noConversation= findViewById(R.id.tv_no_conversation);
        messageEditText= findViewById(R.id.edt_msg_chatting);
        progressBar= findViewById(R.id.chatting_progress);

        ImageView messageSendBtn = findViewById(R.id.message_sent_btn);
        messageSendBtn.setOnClickListener(this);

        ImageView chatProfile = findViewById(R.id.chat_profile);
        chatProfile.setOnClickListener(this);

        chattingRecycle= findViewById(R.id.chattingRecycleView);
        chattingRecycle.setHasFixedSize(true);
        chattingRecycle.setItemAnimator(new DefaultItemAnimator());

        // animation
        openAnim= AnimationUtils.loadAnimation(this,R.anim.fab_open);
        closeAnim= AnimationUtils.loadAnimation(this,R.anim.fab_close);
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

            //Log.d("mymsg", "onClick: "+demoMethod());
        }
        else if(view.getId()==R.id.message_sent_btn){

            sendMessage();
        }
        else if(view.getId()==R.id.chat_profile){
            openDialogue();
        }
    }

    private void openDialogue() {
        MaterialAlertDialogBuilder builder= new MaterialAlertDialogBuilder(this);
        LayoutInflater inflater= getLayoutInflater();
        final View view= inflater.inflate(R.layout.chat_student_details,null);
        builder.setView(view);
        builder.setTitle("Profile").setIcon(R.drawable.ic_account).setBackground(AppCompatResources.getDrawable(this,R.drawable.dialogue_bg)).setCancelable(true);
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
         TextView name= view.findViewById(R.id.txt_name);
         name.setText(student.getName());
         TextView id= view.findViewById(R.id.txt_id);
         id.setText(student.getId());
         CircleImageView image= view.findViewById(R.id.iv_image);
        requestManager.load(student.getImage()).into(image);
        TextView email= view.findViewById(R.id.txt_email);
        email.setText(student.getEmail());
        TextView dept= view.findViewById(R.id.txt_dept);
        dept.setText(student.getDepartment()+" | "+student.getSection());
        TextView phone= view.findViewById(R.id.txt_phone);
        phone.setText(student.getPhone());

    }

    private void sendMessage() {
        msg= messageEditText.getText().toString();
        if(!TextUtils.isEmpty(msg)){

            Message message= new Message(getCurrentDateAnsTime(),"text",msg,"image",prefUtils.getCurrentUId());
            ChatUser chatUser= new ChatUser(getCurrentDateAnsTime(),msg,"text",prefUtils.getImage(),studentUid,prefUtils.getCurrentUId(),prefUtils.getName());

            Log.d("mymsg", "Teacher Chatting Activity -> current UId: "+prefUtils.getCurrentUId());
            Log.d("mymsg", "Teacher Chatting Activity -> student UId: "+studentUid);


            TeacherChatUser teacherChatUser= new TeacherChatUser(getCurrentDateAnsTime(),msg,"text",student.getImage(),
                    prefUtils.getCurrentUId(),studentUid,student.getName());

            StudentChatUser studentChatUser= new StudentChatUser(getCurrentDateAnsTime(),msg,"text",prefUtils.getImage(),
                    prefUtils.getCurrentUId(),prefUtils.getCurrentUId(),prefUtils.getName());

            chatViewModel.sendTeacherMessage(message,teacherChatUser, studentChatUser);
            messageEditText.setText("");
            observeMessageList();
           // chatViewModel.getMessageList(prefUtils.getCurrentUId()+"_"+studentUid);
        }
    }

    private String getCurrentDateAnsTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return df.format(Calendar.getInstance().getTime());
     /*   DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.US);
        return df.format(Calendar.getInstance().getTime());*/
    }
}