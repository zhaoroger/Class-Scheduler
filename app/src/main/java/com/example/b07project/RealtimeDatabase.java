package com.example.b07project;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RealtimeDatabase {
    private final static DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://cscb07-project-851d6-default-rtdb.firebaseio.com/").getReference();
    private static final String ADMINS = "admins";
    private static final String COURSES = "courses";
    private static final String STUDENTS = "students";
    private static final String PASSWORD = "password";

    private RealtimeDatabase() {
    }

    public static void addStudent(StudentAccount student) {
        databaseReference.child(STUDENTS).child(student.getUsername()).setValue(student);
    }

    public static void addAdmin(AdminAccount admin) {
        databaseReference.child(ADMINS).child(admin.getUsername()).setValue(admin);
    }

    public static void loginStudent(String username, LoginCallback loginCallback) {
        databaseReference.child(STUDENTS).child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(null, "Error getting student data", task.getException());
                } else {
                    loginCallback.onCallback(task.getResult().exists());
                }
            }
        });
    }

    public static void loginAdmin(String username, LoginCallback loginCallback) {
        databaseReference.child(ADMINS).child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(null, "Error getting admin data", task.getException());
                } else {
                    loginCallback.onCallback(task.getResult().exists());
                }
            }
        });
    }

    public static void getStudentAccount(String username, GetStudentAccountCallback getStudentAccountCallback) {
        databaseReference.child(STUDENTS).child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(null, "Error getting StudentAccount from Firebase", task.getException());
                } else {
                    getStudentAccountCallback.onCallback(task.getResult().getValue(StudentAccount.class));
                }
            }
        });
    }

    public static void syncStudentCourseList(StudentCourseCallback studentCourseCallback, StudentAccount studentAccount) {
        databaseReference.child(STUDENTS).child(studentAccount.getUsername()).child(COURSES).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(null, "Error syncing student course list with realtime database", task.getException());
                } else {
                    for (DataSnapshot studentCourseDataSnapshot : task.getResult().getChildren()) {
                        String studentCourseCode = studentCourseDataSnapshot.getValue(String.class);

                        getCourse(studentCourseCode, new GetCourseCallback() {
                            @Override
                            public void onCallback(Course course) {
                                studentCourseCallback.onCallback(course);
                            }
                        });
                    }
                }
            }
        });
    }

    public static void updateStudentCourseList(StudentAccount student, ArrayList<String> studentCourseList) {
        databaseReference.child(STUDENTS).child(student.getUsername()).child(COURSES).setValue(studentCourseList);
    }

    public static void addCourse(Course course) {
        databaseReference.child(COURSES).child(course.getCourseCode()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(null, "Error adding course to realtime database", task.getException());
                } else {
                    if (!task.getResult().exists()) {
                        databaseReference.child(COURSES).child(course.getCourseCode()).setValue(course);
                    }
                }
            }
        });
    }

    public static void removeCourse(Course course) {
        databaseReference.child(COURSES).child(course.getCourseCode()).removeValue();
    }

    public static void removeCourse(String courseCode) {
        databaseReference.child(COURSES).child(courseCode).removeValue();
    }

    public static void checkExists(String courseCode, CheckExistsCallback checkExistsCallback) {
        databaseReference.child(COURSES).child(courseCode).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(null, "Error checking if course exists", task.getException());
                } else {
                    checkExistsCallback.onCallback(task.getResult().exists());
                }
            }
        });
    }

    public static void getCourse(String courseCode, GetCourseCallback getCourseCallback) {
        databaseReference.child(COURSES).child(courseCode).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(null, "Error getting course data from realtime database", task.getException());
                } else {
                    if (task.getResult().exists()) {
                        getCourseCallback.onCallback(task.getResult().getValue(Course.class));
                    }
                }
            }
        });
    }

    public static void getAllCourses(AllCoursesCallback allCoursesCallback) {
        databaseReference.child(COURSES).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(null, "Error getting course list from realtime database", task.getException());
                } else {
                    ArrayList<Course> courseList = new ArrayList<Course>();

                    for (DataSnapshot courseDataSnapshot : task.getResult().getChildren()) {
                        Course course = courseDataSnapshot.getValue(Course.class);
                        courseList.add(course);
                    }

                    allCoursesCallback.onCallback(courseList);
                }
            }
        });
    }

    public static ValueEventListener syncCourseList(CourseListCallback courseListCallback, Activity activity) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Course> courseList = new ArrayList<Course>();
                
                for (DataSnapshot courseDataSnapshot : dataSnapshot.getChildren()) {
                    Course course = courseDataSnapshot.getValue(Course.class);
                    courseList.add(course);
                }

                courseListCallback.onCallback(courseList, activity);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(null, "Error syncing course list with realtime database", databaseError.toException());
            }
        };
        databaseReference.child(COURSES).addValueEventListener(listener);
        return listener;
    }

    public static void unsyncCourseList(ValueEventListener courseListListener) {
        if (courseListListener != null) {
            databaseReference.child(COURSES).removeEventListener(courseListListener);
        }
    }
}
