package com.example.b07project;

import java.util.List;

public interface Admin {

    public List<Course> getAllCourses();

    public void addCourse(Course course);

    public void removeCourse(Course course);

    public void editCourse(Course course);
}
