package com.example.smartteachingsystem.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.Teacher_List;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherAdapter extends FirestoreRecyclerAdapter<Teacher_List,TeacherAdapter.TeacherViewHolder> {

    private OnItemClickListener listener;
    private RequestManager requestManager;

    public TeacherAdapter(@NonNull FirestoreRecyclerOptions<Teacher_List> options,RequestManager requestManager) {
        super(options);
        this.requestManager= requestManager;
    }

    @Override
    protected void onBindViewHolder(@NonNull TeacherViewHolder holder, int position, @NonNull Teacher_List model) {

        holder.name.setText(model.getName());
        holder.initial.setText(model.getInitial());
        holder.dept.setText(model.getDesignation()+" Dept. of "+model.getDepartment());
        requestManager.load(model.getImage()).into(holder.image);
        holder.email.setText(model.getEmail());
    }


    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_teacher_list, parent, false);
        return new TeacherViewHolder(view);
    }


    public class TeacherViewHolder extends RecyclerView.ViewHolder{

        private TextView name, initial, dept,email;
        private CircleImageView image;

        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.singleTeacherNameId);
            initial= itemView.findViewById(R.id.singleTeacherInitialId);
            dept= itemView.findViewById(R.id.singleTeacherDeptId);
            image= itemView.findViewById(R.id.singleTeacherImageId);
            email= itemView.findViewById(R.id.singleTeacherEmailId);

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
