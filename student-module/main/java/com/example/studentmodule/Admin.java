package com.example.studentmodule;

import java.util.HashSet;

public interface Admin {

    public HashSet<Course> viewCourses();

    public void addCourse(Course course);

    public void removeCourse(Course course);

    public void editCourse(Course course);
}
