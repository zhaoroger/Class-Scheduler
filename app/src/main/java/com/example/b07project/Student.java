package com.example.b07project;

import java.util.List;

public interface Student {
    public List<String> getCourses();

    public void setCourses(List<String> courses);

    public void addCourse(String course);

    public void removeCourse(String course);

    public void generateCourseTimeline(List<String> courses);
}
