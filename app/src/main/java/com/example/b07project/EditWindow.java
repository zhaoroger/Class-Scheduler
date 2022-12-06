package com.example.b07project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class EditWindow extends Activity {
    Course course;
    EditText courseName;
    EditText courseCode;
    CheckBox checkFall;
    CheckBox checkWinter;
    CheckBox checkSummer;
    EditText prereqText;
    Button saveButton;
    Button cancelButton;
    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Course Details");

        setContentView(R.layout.edit_screen);

        course = getIntent().getParcelableExtra("course");
        setText(course);
        courseName = findViewById(R.id.cname);
        courseCode = findViewById(R.id.ccode);
        courseCode = findViewById(R.id.ccode);
        //prevents inputting space
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };
        courseCode.setFilters(new InputFilter[]{filter});
        saveButton = findViewById(R.id.savebutton);
        cancelButton = findViewById(R.id.cancel);
        deleteButton = findViewById(R.id.delete);
        checkFall = findViewById(R.id.fallcheck);
        checkWinter = findViewById(R.id.wintercheck);
        checkSummer = findViewById(R.id.summercheck);
        prereqText = findViewById(R.id.cprereqs);

        saveButton.setOnClickListener(view -> {
            String cc = courseCode.getText().toString();

            if (courseName.getText().toString().replaceAll("\\s+", "").equals("") || cc.equals("")) {
                Toast myToast = Toast.makeText(getApplicationContext(), "Empty name or course code field", Toast.LENGTH_SHORT);
                myToast.show();
                returnToMain(view);
            }

            if (cc.equals(course.getCourseCode())) {
                editCourse();
                returnToMain(view);
            } else {
                RealtimeDatabase.checkExists(cc, new CheckExistsCallback() {
                    @Override
                    public void onCallback(boolean exists) {
                        if (!exists) {
                            editCourse();
                            returnToMain(view);
                        } else {
                            Toast myToast = Toast.makeText(getApplicationContext(), cc + " already exists", Toast.LENGTH_SHORT);
                            myToast.show();
                            returnToMain(view);
                        }
                    }
                });
            }
        });

        cancelButton.setOnClickListener(this::returnToMain);

        deleteButton.setOnClickListener(view -> {
            RealtimeDatabase.removeCourse(course);
            returnToMain(view);
        });
    }

    private void editCourse() {
        RealtimeDatabase.removeCourse(course);
        Course course = new Course();
        course.setName(courseName.getText().toString());
        course.setCourseCode(courseCode.getText().toString());
        course.setOfferedInFall(checkFall.isChecked());
        course.setOfferedInWinter(checkWinter.isChecked());
        course.setOfferedInSummer(checkSummer.isChecked());
        String editTextString = prereqText.getText().toString();
        editTextString = editTextString.replaceAll("\\s+", "");
        editTextString = editTextString.toUpperCase();
        List<String> prereqs = new LinkedList<>(Arrays.asList(editTextString.split(",", 0)));
        prereqs.removeAll(Collections.singleton(""));
        course.setPrerequisites(prereqs);
        RealtimeDatabase.addCourse(course);
        for (String prereq : prereqs) {
            RealtimeDatabase.addCourse(new Course(prereq));
        }
    }

    private void setText(Course course) {
        StringBuilder prerequisites = new StringBuilder();
        List<String> listOfPrereqs = course.getPrerequisites();

        courseName = findViewById(R.id.cname);
        courseName.setText(course.getName());

        courseCode = findViewById(R.id.ccode);
        courseCode.setText(course.getCourseCode());

        checkFall = findViewById(R.id.fallcheck);
        checkFall.setChecked(course.isOfferedInFall());
        checkWinter = findViewById(R.id.wintercheck);
        checkWinter.setChecked(course.isOfferedInWinter());
        checkSummer = findViewById(R.id.summercheck);
        checkSummer.setChecked(course.isOfferedInSummer());

        prereqText = findViewById(R.id.cprereqs);
        for (int i = 0; i < listOfPrereqs.size(); i++) {
            prerequisites.append(listOfPrereqs.get(i)).append(", ");
        }
        if (prerequisites.length() > 0) {
            prerequisites = new StringBuilder(prerequisites.substring(0, prerequisites.length() - 2));
        }
        prereqText.setText(prerequisites.toString());

    }

    private void returnToMain(View view) {
        Intent intent = new Intent(view.getContext(), AdminMainActivity.class);
        startActivity(intent);
        finish();
    }
}
