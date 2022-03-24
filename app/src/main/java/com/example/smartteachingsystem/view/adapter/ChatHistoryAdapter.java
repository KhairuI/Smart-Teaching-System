package com.example.smartteachingsystem.view.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.ChatUser;
import com.example.smartteachingsystem.view.model.StudentChatUser;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.MyViewHolder> implements Filterable {

    private final RequestManager requestManager;
    private List<StudentChatUser> lists= new ArrayList<>();
    private List<StudentChatUser> listSearch= new ArrayList<>();
    private OnItemClickListener listener;
    private String currentUid;

    public ChatHistoryAdapter(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_chat_history, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        StudentChatUser studentChatUser = lists.get(position);

        Log.d("mymsg", "Chat history Adapter -> sender uId: "+studentChatUser.getReceiverUid());
        // set value
        requestManager.load(studentChatUser.getImage()).into(holder.circleImageView);
        holder.name.setText(studentChatUser.getName());
        if(studentChatUser.getMessageUid().equals(currentUid)){
            holder.lastMessage.setText("You: "+studentChatUser.getLastMessage());
        }
        else {
            holder.lastMessage.setText(studentChatUser.getLastMessage());
        }

        holder.dateTime.setText(studentChatUser.getDateTime());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(studentChatUser.getReceiverUid());
            }
        });
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongItemClick(studentChatUser.getReceiverUid(),position);
                return false;
            }
        });

    }

    public void setUserInfo(String currentUid){
        this.currentUid= currentUid;
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void setList(List<StudentChatUser> lists){
        this.lists=lists;
        listSearch=lists;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private final Filter userFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<StudentChatUser> filterStudent= new ArrayList<>();

            if(constraint== null || constraint.length()==0){
                filterStudent.addAll(listSearch);
                notifyDataSetChanged();
            }
            else {

                String filterPattern= constraint.toString().toLowerCase().trim();
                for (StudentChatUser studentChatUser : listSearch){
                    if(studentChatUser.getName().toLowerCase().contains(filterPattern)|| studentChatUser.getLastMessage().toLowerCase().contains(filterPattern)){
                        filterStudent.add(studentChatUser);
                    }
                }
            }

            FilterResults results= new FilterResults();
            results.values= filterStudent;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            lists.clear();
            lists.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final ConstraintLayout layout;
        private final CircleImageView circleImageView;
        private final TextView name;
        private final TextView lastMessage;
        private final TextView dateTime;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            layout= itemView.findViewById(R.id.historyLayout);
            circleImageView= itemView.findViewById(R.id.img_history);
            name= itemView.findViewById(R.id.txt_history_name);
            lastMessage= itemView.findViewById(R.id.txt_history_lastMessage);
            dateTime= itemView.findViewById(R.id.txt_history_time);
        }
    }

    public  interface OnItemClickListener{
        void onItemClick(String uId);
        void onLongItemClick(String uId,int position);
    }

    public  void setOnItemClickListener(OnItemClickListener listener){
        this.listener= listener;
    }
}
