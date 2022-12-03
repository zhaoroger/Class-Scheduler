package com.example.b07project;

import android.app.Activity;

import java.util.List;

public interface CourseListCallback {
    void onCallback(List<Course> courseList, Activity activity);
}
