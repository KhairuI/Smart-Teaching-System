package com.example.smartteachingsystem.view.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.StudentApp;
import com.example.smartteachingsystem.view.model.TeacherApp;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherAppAdapter extends RecyclerView.Adapter<TeacherAppAdapter.TeacherViewHolder> {

    private List<StudentApp> list= new ArrayList<>();
    private RequestManager requestManager;
    private OnItemClickListener listener;

    public TeacherAppAdapter(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_teacher_appointment, parent, false);
        return new TeacherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {

        holder.name.setText(list.get(position).getName());
        holder.id.setText(list.get(position).getId());
        holder.dept.setText(list.get(position).getDept());
        requestManager.load(list.get(position).getImage()).into(holder.image);
        if(list.get(position).getStatus().equals("Request") || list.get(position).getStatus().equals("Decline")){
            holder.status.setText(list.get(position).getStatus());
            holder.status.setTextColor(Color.RED);
        }
        else {
            holder.status.setText(list.get(position).getStatus());
            holder.status.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<StudentApp> list){
        this.list=list;
        notifyDataSetChanged();
    }

    public class TeacherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView name;
        private final TextView id;
        private final TextView dept;
        private final TextView status;
        private final CircleImageView image;

        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.singleTeacherAppointmentNameId);
            id= itemView.findViewById(R.id.singleTeacherAppointmentUserId);
            dept= itemView.findViewById(R.id.singleTeacherAppointmentDeptId);
            image= itemView.findViewById(R.id.singleTeacherAppointmentImageId);
            status= itemView.findViewById(R.id.singleTeacherAppointmentStatusId);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position= getAdapterPosition();
            if(position!= RecyclerView.NO_POSITION && listener != null){
                listener.onItemClick(position);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int position= getAdapterPosition();
            if(position!= RecyclerView.NO_POSITION && listener != null){
                listener.onLongItemClick(position);
            }
            return false;
        }
    }

    public  interface OnItemClickListener{

        void onItemClick(int position);
        void onLongItemClick(int position);
    }

    public  void setOnItemClickListener(OnItemClickListener listener){

        this.listener= listener;
    }
}
