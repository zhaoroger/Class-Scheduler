package com.example.b07project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {
    private ArrayList<Course> courseList;
    private RecyclerView recyclerView;
    private ValueEventListener activeListener;

    public ValueEventListener getActiveListener() {
        return activeListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        Button addButton = findViewById(R.id.addbutton);
        Button logout = findViewById(R.id.save_signout);
        courseList = new ArrayList<>();

        setAdapter();
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AddWindow.class);
            intent.putParcelableArrayListExtra("courseList", courseList);
            view.getContext().startActivity(intent);
            RealtimeDatabase.unsyncCourseList(activeListener);
            finish();
        });
        logout.setOnClickListener(view -> {

//                Intent intent = new Intent(view.getContext(), *LOGIN ACTIVITY*);
//                *SAVE courseLIST TO FIREBASE*
//                view.getContext().startActivity(intent);
//                RealtimeDatabase.unsyncCourseList(activeListener);
//                finish();
            Toast myToast = Toast.makeText(getApplicationContext(), "Saved Course List", Toast.LENGTH_SHORT);
            myToast.show();
        });
    }

    private void setAdapter() {
        activeListener = RealtimeDatabase.syncCourseList(new CourseListCallback() {
            @Override
            public void onCallback(List<Course> courseList, Activity activity) {
                recyclerAdapter adapter = new recyclerAdapter((ArrayList<Course>) courseList, (AdminMainActivity) activity);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }
        }, this);
    }
}