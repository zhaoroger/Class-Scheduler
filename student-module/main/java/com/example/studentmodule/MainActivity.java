package com.example.studentmodule;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.studentmodule.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.LinkedHashSet;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private void switchToStudentActivity() {
        Intent switchActivityIntent = new Intent(this, StudentActivity.class);
        switchActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(switchActivityIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Log.i("Main Activity", "Switching to Student Activity.");


                //basic initialization
                AllCourses allCourses = AllCourses.getInstance();
                allCourses.addCourse(
                        new Course(
                                "Introduction to Computer Science I",
                                "CSCA08",
                                true,
                                false,
                                false
                        )
                );
                allCourses.addCourse(
                        new Course(
                                "Introduction to Computer Science II with a name that is really long!",
                                "CSCA48",
                                false,
                                true,
                                false
                        )
                );
                allCourses.addCourse(
                        new Course(
                                "Introduction to Programming",
                                "CSCA10",
                                true,
                                false,
                                false
                        )
                );
                allCourses.addCourse(
                        new Course(
                                "Introduction to Physics I",
                                "PHYA10",
                                true,
                                false,
                                false
                        )
                );
                allCourses.addCourse(
                        new Course(
                                "Introduction to Physics II",
                                "PHYA21",
                                true,
                                false,
                                false
                        )
                );
                allCourses.addCourse(
                        new Course(
                                "Calculus I",
                                "MATA30",
                                true,
                                false,
                                false
                        )
                );
                allCourses.addCourse(
                        new Course(
                                "Calculus II",
                                "MATA36",
                                true,
                                false,
                                false
                        )
                );
                allCourses.addCourse(
                        new Course(
                                "Project course",
                                "PHYD01",
                                true,
                                false,
                                false
                        )
                );
                allCourses.addCourse(
                        new Course(
                                "Reading course",
                                "PHD72",
                                true,  //to be corrected when a course is offered in two sessions
                                false,
                                true
                        )
                );

                //sample logged in account
                StudentAccount studentAccount = new StudentAccount(
                        "lkelvin",
                        "Password",
                        "Lord Kelvin"
                );
                LinkedHashSet<Course> studentCourses = new LinkedHashSet<Course>();
                studentCourses.add(new Course("Introduction to Programming", "CSCA08"));
                studentAccount.setCourses(studentCourses);

                //fetching student account to the student activity page
                StudentModuleCommunicator.getInstance().setStudentAccount(studentAccount);
                switchToStudentActivity();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}