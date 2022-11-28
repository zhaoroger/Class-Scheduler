package com.example.studentmodule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

public final class StudentModuleCommunicator {
    private static StudentModuleCommunicator communicator;
    private static StudentAccount studentAccount;
    private static ArrayList<Course> sortedAllCoursesArray;
    private static ArrayList<Course> sortedStudentCoursesArray;

    private StudentModuleCommunicator() {
        StudentModuleCommunicator.sortedStudentCoursesArray = new ArrayList<Course>();
        StudentModuleCommunicator.sortedAllCoursesArray =
                new ArrayList<Course>(AllCourses.getInstance().getAllCourses());
    }

    public static StudentModuleCommunicator getInstance() {
        if (communicator == null) {
            communicator = new StudentModuleCommunicator();
        }
        return communicator;
    }

    public void setStudentAccount(StudentAccount newStudentAccount) {
        StudentModuleCommunicator.studentAccount = newStudentAccount;
        StudentModuleCommunicator.sortedStudentCoursesArray =
                new ArrayList<Course>(StudentModuleCommunicator.studentAccount.getCourses());
        Collections.sort(StudentModuleCommunicator.sortedStudentCoursesArray);
    }

    public StudentAccount getStudentAccount() {
        if (studentAccount == null)
            throw new RuntimeException("The student account was not passed on to the module communicator.");
        return studentAccount;
    }

    public ArrayList<Course> getSortedStudentCoursesArray() {
        Collections.sort(StudentModuleCommunicator.sortedStudentCoursesArray);
        Collections.reverse(StudentModuleCommunicator.sortedStudentCoursesArray);
        return StudentModuleCommunicator.sortedStudentCoursesArray;
    }

    public void addCourseToSortedStudentCourses(Course course) {
        StudentModuleCommunicator.sortedStudentCoursesArray.add(course);
        Collections.sort(StudentModuleCommunicator.sortedStudentCoursesArray);
        Collections.reverse(StudentModuleCommunicator.sortedStudentCoursesArray);
    }

    public void removeCourseFromSortedStudentCourses(Course course) {
        StudentModuleCommunicator.sortedStudentCoursesArray.remove(course);
    }


    public ArrayList<Course> getSortedAllCoursesArray() {
        Collections.sort(StudentModuleCommunicator.sortedAllCoursesArray);
        return StudentModuleCommunicator.sortedAllCoursesArray;
    }

    public void updateDataBase() {
        LinkedHashSet<Course> studentCourses = new LinkedHashSet<Course>();

        for (Course course:StudentModuleCommunicator.sortedStudentCoursesArray) {
            studentCourses.add(course);
        }

        StudentModuleCommunicator.studentAccount.setCourses(studentCourses);
        //code to push the account to Firebase
    }

}
