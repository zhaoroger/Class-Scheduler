package com.example.b07project;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RealtimeDatabase {
    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://cscb07-project-851d6-default-rtdb.firebaseio.com/").getReference();

    private RealtimeDatabase() {
    }

//    public static void addStudent(StudentAccount student) {
//        databaseReference.child("students").child(student.getUsername()).setValue(student);
//    }
//
//    public static void addAdmin(AdminAccount admin) {
//        databaseReference.child("admins").child(admin.getUsername()).setValue(admin);
//    }

    public static void addCourse(Course course) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    databaseReference.child("courses").child(course.getCourseCode()).setValue(course);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(null, "Error adding course", databaseError.toException());
            }
        };

        databaseReference.child("courses").child(course.getCourseCode()).addListenerForSingleValueEvent(listener);
    }

    public static void removeCourse(Course course) {
        databaseReference.child("courses").child(course.getCourseCode()).removeValue();
    }

    public static void removeCourse(String courseCode) {
        databaseReference.child("courses").child(courseCode).removeValue();
    }

    public static void editCourse(Course course) {
        // TODO
    }

    public static List<Course> getAllCourses() {
        List<Course> allCourses = new ArrayList<Course>();
        DatabaseReference coursesReference = databaseReference.child("courses");
        ValueEventListener coursesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot courseDataSnapshot : dataSnapshot.getChildren()) {
                    Course course = courseDataSnapshot.getValue(Course.class);
                    allCourses.add(course);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(null, "Error getting all courses", databaseError.toException());
            }
        };

        coursesReference.addValueEventListener(coursesListener);
        return allCourses;
    }
}