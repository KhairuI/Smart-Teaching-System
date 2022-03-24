package com.example.smartteachingsystem.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.Message;
import com.example.smartteachingsystem.view.model.StudentMessage;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.ChatViewHolder> {

    private final int SENDER_TEXT=0;
    private final int SENDER_IMAGE=1;
    private final int RECEIVER_TEXT=2;
    private final int RECEIVER_IMAGE=3;
    private String currentUid;
    private RequestManager requestManager;
    private List<Message> studentMessageList = new ArrayList<>();

    public ChattingAdapter() {
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if(viewType==SENDER_TEXT){
            view= inflater.inflate(R.layout.chat_view_1,parent,false);
            return new ChatViewHolder(view, SENDER_TEXT);
        }
        else if(viewType==SENDER_IMAGE){
            view= inflater.inflate(R.layout.chat_view_2,parent,false);
            return new ChatViewHolder(view, SENDER_IMAGE);
        }
        else if(viewType==RECEIVER_TEXT){
            view= inflater.inflate(R.layout.chat_view_3,parent,false);
            return new ChatViewHolder(view, RECEIVER_TEXT);
        }
        else {
            view= inflater.inflate(R.layout.chat_view_4,parent,false);
            return new ChatViewHolder(view, RECEIVER_IMAGE);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        Message message = studentMessageList.get(position);
        Log.d("mymsg", "Chatting Adapter -> sender UId: "+message.getSenderId());
        if(message.getSenderId().equals(currentUid)){
            // from sender site
            Log.d("mymsg", "Chatting Adapter -> my message: ");
            if(message.getMessageType().equals("text")){
                holder.senderMessageText.setText(message.getMessage());
                holder.senderMessageDate.setText(getCurrentTime(message.getDateTime()));
                holder.constrain1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(holder.senderMessageDate.getVisibility()== View.VISIBLE){
                            holder.senderMessageDate.setVisibility(View.GONE);
                        }
                        else {
                            holder.senderMessageDate.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
            else {
                requestManager.load(message.getImage()).into(holder.senderImage);
                holder.senderImageDate.setText(getCurrentTime(message.getDateTime()));
            }
        }
        else {
            // from receiver site
            Log.d("mymsg", "Chatting Adapter -> partner message: ");
            if(message.getMessageType().equals("text")){
                holder.receiverMessageText.setText(message.getMessage());
                holder.receiverMessageDate.setText(getCurrentTime(message.getDateTime()));
                holder.constrain2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(holder.receiverMessageDate.getVisibility()== View.VISIBLE){
                            holder.receiverMessageDate.setVisibility(View.GONE);
                        }
                        else {
                            holder.receiverMessageDate.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
            else {
                requestManager.load(message.getImage()).into(holder.receiverImage);
                holder.receiverImageDate.setText(getCurrentTime(message.getDateTime()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return studentMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = studentMessageList.get(position);
        if(message.getSenderId().equals(currentUid)){
            // from sender site
            if(message.getMessageType().equals("text")){
                return SENDER_TEXT;
            }
            else {
                return SENDER_IMAGE;
            }
        }
        else {
            // from receiver site
            if(message.getMessageType().equals("text")){
                return RECEIVER_TEXT;
            }
            else {
                return RECEIVER_IMAGE;
            }
        }
    }

    public void setUserInfo(String currentUid, RequestManager requestManager){
        this.currentUid= currentUid;
        this.requestManager= requestManager;
    }

    public void setMessageList(List<Message> studentMessageList){
        this.studentMessageList = studentMessageList;
    }
    public void setNewMessage(Message studentMessage){
        studentMessageList.add(studentMessage);
        notifyItemInserted(studentMessageList.size()-1);

    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder{

        TextView senderMessageText,senderMessageDate,senderImageDate,receiverMessageText,receiverMessageDate,receiverImageDate;
        ImageView senderImage,receiverImage;
        ConstraintLayout constrain1, constrain2;

        public ChatViewHolder(View itemView, int viewType) {
            super(itemView);
            if(viewType==0){
                senderMessageText= itemView.findViewById(R.id.tv_sender_message);
                senderMessageDate= itemView.findViewById(R.id.tv_sender_message_date);
                constrain1= itemView.findViewById(R.id.constrain1);
            }
            else if(viewType==1){
                senderImage= itemView.findViewById(R.id.iv_sender_image);
                senderImageDate= itemView.findViewById(R.id.tv_sender_image_date);
            }
            else if(viewType==2){
                receiverMessageText= itemView.findViewById(R.id.tv_receiver_message);
                receiverMessageDate= itemView.findViewById(R.id.tv_receiver_message_date);
                constrain2= itemView.findViewById(R.id.constrain2);
            }
            else if(viewType==3){
                receiverImage= itemView.findViewById(R.id.iv_receiver_image);
                receiverImageDate= itemView.findViewById(R.id.tv_receiver_image_date);
            }
        }
    }

    // time convert
    public static String getCurrentTime(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy hh:mm a", Locale.US);
        String cTime = "";
        try {
            Date parse = df.parse(date);
            cTime = sdf.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cTime;
    }
}
