package com.example.b07project;

import java.util.HashSet;

final class AllCourses {
    private static AllCourses instance;
    private static HashSet<Course> allCourses = new HashSet<Course>();

    private AllCourses() {
    }

    static AllCourses getInstance() {
        if (instance == null) {
            instance = new AllCourses();
        }

        return instance;
    }

    public HashSet<Course> getAllCourses() {
        return allCourses;
    }

    public void addCourse(Course course) {
        allCourses.add(course);
    }

    public void removeCourse(Course course) {
        allCourses.remove(course);
    }
}