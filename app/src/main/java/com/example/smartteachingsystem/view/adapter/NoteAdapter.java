package com.example.smartteachingsystem.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartteachingsystem.R;
import com.example.smartteachingsystem.view.model.Note;
import com.example.smartteachingsystem.view.model.TeacherApp;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> implements Filterable {

    private List<Note> noteList= new ArrayList<>();
    private List<Note> searchList= new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.note_single_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        holder.name.setText(noteList.get(position).getName());
        holder.date.setText(noteList.get(position).getDate());
        holder.note.setText(noteList.get(position).getNoteText());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void setList(List<Note> noteList){
       this.noteList= noteList;
        searchList= noteList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private final Filter userFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Note> filterStudent= new ArrayList<>();

            if(constraint== null || constraint.length()==0){
                filterStudent.addAll(searchList);
            }
            else {

                String filterPattern= constraint.toString().toLowerCase().trim();
                for (Note note: searchList){
                    if(note.getName().toLowerCase().contains(filterPattern)){
                        filterStudent.add(note);
                    }
                }
            }

            FilterResults results= new FilterResults();
            results.values= filterStudent;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            noteList.clear();
            noteList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private final TextView name;
        private final TextView note;
        private final TextView date;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.singleNoteNameId);
            note= itemView.findViewById(R.id.singleNoteId);
            date= itemView.findViewById(R.id.singleNoteDateId);
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
