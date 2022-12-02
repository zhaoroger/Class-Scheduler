package com.example.b07project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Course> courseList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        Button addButton = findViewById(R.id.addbutton);
        Button logout = findViewById(R.id.save_signout);
        courseList = new ArrayList<>();
        if(getIntent().getParcelableArrayListExtra("newCourseList") == null) {setUserInfo();}
        else {courseList = getIntent().getParcelableArrayListExtra("newCourseList");}

        setAdapter();
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AddWindow.class);
            intent.putParcelableArrayListExtra("courseList", courseList);
            view.getContext().startActivity(intent);
            finish();
        });
        logout.setOnClickListener(view -> {

//                Intent intent = new Intent(view.getContext(), *LOGIN ACTIVITY*);
//                *SAVE courseLIST TO FIREBASE*
//                view.getContext().startActivity(intent);
//                finish();
            Toast myToast = Toast.makeText(getApplicationContext(), "Saved Course List", Toast.LENGTH_SHORT);
            myToast.show();
        });
    }

    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(courseList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setUserInfo() {
        List<String> prereqs = Arrays.asList("CSCA01", "CSCA02");
        List<String> prereqs2 = Arrays.asList("CSCA23", "CSCA07");
        List<String> prereqs3 = Arrays.asList("uh", "oh", "smelly");
        courseList.add(new Course("Intro to Computer Science", "csca08", true, true, true, prereqs));
        courseList.add(new Course("Advanced Computer Science", "CSCB07", true, true, true, prereqs2));
        courseList.add(new Course("idk", "stinky", true, true, true, prereqs3));
    }
}