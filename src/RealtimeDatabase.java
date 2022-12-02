import android.util.Log;

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

    private RealtimeDatabase() {
    }

    public static void addStudent(StudentAccount student) {
        databaseReference.child(STUDENTS).child(student.getUsername()).setValue(student);
    }

    public static void addAdmin(AdminAccount admin) {
        databaseReference.child(ADMINS).child(admin.getUsername()).setValue(admin);
    }

    public static void addCourse(Course course) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    databaseReference.child(COURSES).child(course.getCourseCode()).setValue(course);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(null, "Error adding course to realtime database", databaseError.toException());
            }
        };

        databaseReference.child(COURSES).child(course.getCourseCode()).addListenerForSingleValueEvent(listener);
    }

    public static void removeCourse(Course course) {
        databaseReference.child(COURSES).child(course.getCourseCode()).removeValue();
    }

    public static void removeCourse(String courseCode) {
        databaseReference.child(COURSES).child(courseCode).removeValue();
    }

    public static void editCourse(Course course) {
        // TODO
    }

    public static void syncCourseList(ArrayList<Course> courseList) {
        databaseReference.child(COURSES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                courseList.clear();

                for (DataSnapshot courseDataSnapshot : dataSnapshot.getChildren()) {
                    Course course = courseDataSnapshot.getValue(Course.class);
                    courseList.add(course);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(null, "Error syncing course list with realtime database", databaseError.toException());
            }
        });
    }
}