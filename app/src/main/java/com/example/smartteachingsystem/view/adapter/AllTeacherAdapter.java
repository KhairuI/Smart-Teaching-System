package com.example.smartteachingsystem.view.adapter;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllTeacherAdapter extends RecyclerView.Adapter<AllTeacherAdapter.MyViewHolder> implements Filterable {

    private RequestManager requestManager;
    private List<Teacher_List> lists= new ArrayList<>();
    private List<Teacher_List> listSearch= new ArrayList<>();
    private OnItemClickListener listener;

    public AllTeacherAdapter(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_teacher_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name.setText(lists.get(position).getName());
        holder.initial.setText(lists.get(position).getInitial());
        holder.dept.setText(lists.get(position).getDesignation()+" Dept. of "+lists.get(position).getDepartment());
        requestManager.load(lists.get(position).getImage()).into(holder.image);
        holder.email.setText(lists.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void setList(List<Teacher_List> lists){
        this.lists=lists;
        listSearch=lists;
        notifyDataSetChanged();
    }

    //this is for search data
    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private final Filter userFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Teacher_List> filterStudent= new ArrayList<>();

            if(constraint== null || constraint.length()==0){
                filterStudent.addAll(listSearch);
            }
            else {

                String filterPattern= constraint.toString().toLowerCase().trim();
                for (Teacher_List teacher: listSearch){
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

            lists.clear();
            lists.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, initial, dept,email;
        private CircleImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.singleTeacherNameId);
            initial= itemView.findViewById(R.id.singleTeacherInitialId);
            dept= itemView.findViewById(R.id.singleTeacherDeptId);
            image= itemView.findViewById(R.id.singleTeacherImageId);
            email= itemView.findViewById(R.id.singleTeacherEmailId);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position= getAdapterPosition();
            if(position!= RecyclerView.NO_POSITION && listener != null){
                listener.onItemClick(position);
            }
        }
    }

    public  interface OnItemClickListener{
        void onItemClick(int position);
    }

    public  void setOnItemClickListener(OnItemClickListener listener){

        this.listener= listener;
    }




}
