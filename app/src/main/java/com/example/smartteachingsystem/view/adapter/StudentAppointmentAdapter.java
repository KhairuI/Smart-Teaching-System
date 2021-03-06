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
import com.example.smartteachingsystem.view.model.TeacherApp;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAppointmentAdapter extends FirestoreRecyclerAdapter<TeacherApp, StudentAppointmentAdapter.StudentAppointmentViewHolder> {

    private OnItemClickListener listener;
    private RequestManager requestManager;


    public StudentAppointmentAdapter(@NonNull FirestoreRecyclerOptions<TeacherApp> options,RequestManager requestManager) {
        super(options);
        this.requestManager= requestManager;
    }

    @Override
    protected void onBindViewHolder(@NonNull StudentAppointmentViewHolder holder, int position, @NonNull TeacherApp model) {

        holder.name.setText(model.getName());
        holder.initial.setText(model.getInitial());
        holder.dept.setText(model.getDesignation()+" Dept. of "+model.getDept());
        requestManager.load(model.getImage()).into(holder.image);
        if(model.getStatus().equals("Pending") || model.getStatus().equals("Decline")){
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
    public StudentAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_student_appointment, parent, false);
        return new StudentAppointmentViewHolder(view);
    }


    public class StudentAppointmentViewHolder extends RecyclerView.ViewHolder{

        private TextView name, initial, dept,status;
        private CircleImageView image;

        public StudentAppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.singleStudentAppointmentNameId);
            initial= itemView.findViewById(R.id.singleStudentAppointmentInitialId);
            dept= itemView.findViewById(R.id.singleStudentAppointmentDeptId);
            image= itemView.findViewById(R.id.singleStudentAppointmentImageId);
            status= itemView.findViewById(R.id.singleStudentAppointmentStatusId);

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
