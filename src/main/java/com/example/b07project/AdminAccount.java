package com.example.b07project;

import java.util.List;

final class AdminAccount extends Account implements Admin {

    public AdminAccount(String username, String password, String name) {
        super(username, password, name);
    }

    @Override
    public List<Course> getAllCourses() {
        // TODO
        return null;
    }

    @Override
    public void addCourse(Course course) {
        RealtimeDatabase.addCourse(course);
    }

    @Override
    public void removeCourse(Course course) {
        RealtimeDatabase.addCourse(course);
    }

    @Override
    public void editCourse(Course course) {
        // TODO
        RealtimeDatabase.editCourse(course);
    }
}
