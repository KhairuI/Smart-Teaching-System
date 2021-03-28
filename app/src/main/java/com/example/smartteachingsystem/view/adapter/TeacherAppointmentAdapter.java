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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherAppointmentAdapter extends FirestoreRecyclerAdapter<StudentApp, TeacherAppointmentAdapter.TeacherAppointmentViewHolder> {

    private OnItemClickListener listener;
    private RequestManager requestManager;

    public TeacherAppointmentAdapter(@NonNull FirestoreRecyclerOptions<StudentApp> options,RequestManager requestManager) {
        super(options);
        this.requestManager= requestManager;
    }

    @Override
    protected void onBindViewHolder(@NonNull TeacherAppointmentViewHolder holder, int position, @NonNull StudentApp model) {

        holder.name.setText(model.getName());
        holder.id.setText(model.getId());
        holder.dept.setText(model.getDept());
        requestManager.load(model.getImage()).into(holder.image);
        if(model.getStatus().equals("Request") || model.getStatus().equals("Decline")){
            holder.status.setText(model.getStatus());
            holder.status.setTextColor(Color.RED);
        }
        else {
            holder.status.setText(model.getStatus());
            holder.status.setTextColor(Color.GREEN);
        }
    }

    @NonNull
    @Override
    public TeacherAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_teacher_appointment, parent, false);
        return new TeacherAppointmentViewHolder(view);
    }

    public class TeacherAppointmentViewHolder extends RecyclerView.ViewHolder{

        private TextView name, dept, id,status;
        private CircleImageView image;

        public TeacherAppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.singleTeacherAppointmentNameId);
            dept= itemView.findViewById(R.id.singleTeacherAppointmentDeptId);
            id= itemView.findViewById(R.id.singleTeacherAppointmentUserId);
            image= itemView.findViewById(R.id.singleTeacherAppointmentImageId);
            status= itemView.findViewById(R.id.singleTeacherAppointmentStatusId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position= getAdapterPosition();
                    if(position!= RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }

                }
            });
        }
    }

    public  interface OnItemClickListener{

        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public  void setOnItemClickListener(OnItemClickListener listener){

        this.listener= listener;
    }

}
