package com.example.studentmodule;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    /*
    Switches from the current activity to the student activity
     */
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


        // ~~~~~~~~~~~~~~~~~  Initializing StudentModuleCommunicator ~~~~~~~~~~~~~~~~~~~
        // ~~~~~~~~~~~~~~~~~  and when ready, switching to StudentActivity ~~~~~~~~~~~~~
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Begin ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                RealtimeDatabase.syncCourseList(new CourseListCallback() {
                    @Override
                    public void onCallback(List<Course> courseList, Activity activity) {
                        if (!StudentActivity.isInitiated) {
                            StudentModuleCommunicator.getInstance().setSortedAllCoursesArray(new ArrayList<>(courseList));
                            if (StudentModuleCommunicator.isStudentModuleCommunicatorReady) {
                                switchToStudentActivity();
                            }
                        }
                    }
                }, MainActivity.this);
                */

                RealtimeDatabase.getAllCourses(new AllCoursesCallback() {
                    @Override
                    public void onCallback(List<Course> courseList) {
                        if (!StudentActivity.isInitiated) {
                            StudentModuleCommunicator.getInstance().setSortedAllCoursesArray(new ArrayList<>(courseList));
                            if (StudentModuleCommunicator.isStudentModuleCommunicatorReady) {
                                switchToStudentActivity();
                            }
                        }
                    }
                });

                RealtimeDatabase.getStudentAccount("studentusername1", new GetStudentAccountCallback() {
                    @Override
                    public void onCallback(StudentAccount studentAccount) {
                        if (!StudentActivity.isInitiated) {
                            StudentModuleCommunicator.getInstance().setStudentAccount(studentAccount);
                            if (StudentModuleCommunicator.isStudentModuleCommunicatorReady) {
                                switchToStudentActivity();
                            }
                        }
                    }
                });
            }
        });
        // ~~~~~~~~~~~~~~~~~  Initializing StudentModuleCommunicator ~~~~~~~~~~~~~~~~~~~
        // ~~~~~~~~~~~~~~~~~  and when ready, switching to StudentActivity ~~~~~~~~~~~~~
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ End ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

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
