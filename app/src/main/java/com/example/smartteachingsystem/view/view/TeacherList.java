package com.example.smartteachingsystem.view.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.example.smartteachingsystem.R;

public class TeacherList extends AppCompatActivity {
    // Declare all views..
    private RecyclerView teacherList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        findSection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.search_menu,menu);


        MenuItem menuItem= menu.findItem(R.id.searchId);
        SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void findSection() {
        teacherList= findViewById(R.id.teacherListRecycleId);
        toolbar= findViewById(R.id.teacherListToolbarId);
        setSupportActionBar(toolbar);
    }
}