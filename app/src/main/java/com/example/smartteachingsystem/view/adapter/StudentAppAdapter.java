package com.example.smartteachingsystem.view.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.TeacherApp;
import com.example.smartteachingsystem.view.model.Teacher_List;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAppAdapter extends RecyclerView.Adapter<StudentAppAdapter.StudentViewHolder> implements Filterable {

    private List<TeacherApp> list= new ArrayList<>();
    private List<TeacherApp> listSearch= new ArrayList<>();
    private final RequestManager requestManager;
    private OnItemClickListener listener;

    public StudentAppAdapter(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_student_appointment, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {

        holder.name.setText(list.get(position).getName());
        holder.initial.setText(list.get(position).getInitial());
        holder.dept.setText(list.get(position).getDesignation()+" Dept. of "+list.get(position).getDept());
        requestManager.load(list.get(position).getImage()).into(holder.image);
        if(list.get(position).getStatus().equals("Pending") || list.get(position).getStatus().equals("Decline")){
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

    public void setList(List<TeacherApp> list){
        this.list=list;
        listSearch= list;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private final Filter userFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TeacherApp> filterStudent= new ArrayList<>();

            if(constraint== null || constraint.length()==0){
                filterStudent.addAll(listSearch);
            }
            else {

                String filterPattern= constraint.toString().toLowerCase().trim();
                for (TeacherApp teacher: listSearch){
                    if(teacher.getName().toLowerCase().contains(filterPattern)||teacher.getInitial().toLowerCase().contains(filterPattern)){
                        filterStudent.add(teacher);
                    }
                }
            }

            FilterResults results= new FilterResults();
            results.values= filterStudent;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            list.clear();
            list.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView name;
        private final TextView initial;
        private final TextView dept;
        private final TextView status;
        private final CircleImageView image;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.singleStudentAppointmentNameId);
            initial= itemView.findViewById(R.id.singleStudentAppointmentInitialId);
            dept= itemView.findViewById(R.id.singleStudentAppointmentDeptId);
            image= itemView.findViewById(R.id.singleStudentAppointmentImageId);
            status= itemView.findViewById(R.id.singleStudentAppointmentStatusId);
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
