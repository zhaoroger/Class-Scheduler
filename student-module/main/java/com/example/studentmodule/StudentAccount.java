package com.example.studentmodule;

import java.util.LinkedHashSet;

final class StudentAccount extends Account implements Student {
    LinkedHashSet<Course> courses = new LinkedHashSet<Course>();

    public StudentAccount(String username, String password, String name) {
        super(username, password, name);
    }

    public StudentAccount(String username, String password, String name, LinkedHashSet<Course> courses) {
        super(username, password, name);
        this.courses = courses;
    }

    @Override
    public LinkedHashSet<Course> getCourses() {
        return courses;
    }

    @Override
    public void setCourses(LinkedHashSet<Course> courses) {
        this.courses = courses;
    }

    @Override
    public void addCourse(Course course) {
        courses.add(course);
    }

    @Override
    public void removeCourse(Course course) {
        courses.remove(course);
    }

    @Override
    public void generateCourseTimeline(LinkedHashSet<Course> courses) {
        // TODO
    }
}
