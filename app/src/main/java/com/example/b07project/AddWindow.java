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

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class AddWindow extends Activity {
    EditText courseName;
    EditText courseCode;
    CheckBox checkFall;
    CheckBox checkWinter;
    CheckBox checkSummer;
    EditText prereqText;
    Button saveButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Course");

        setContentView(R.layout.add_screen);

        courseName = findViewById(R.id.cname_add);
        courseCode = findViewById(R.id.ccode_add);
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
        saveButton = findViewById(R.id.addcoursebutton);
        cancelButton = findViewById(R.id.cancel_add);
        checkFall = findViewById(R.id.fallcheck_add);
        checkWinter = findViewById(R.id.wintercheck_add);
        checkSummer = findViewById(R.id.summercheck_add);
        prereqText = findViewById(R.id.cprereqs_add);

        saveButton.setOnClickListener(view -> {
            String cc = courseCode.getText().toString();

            if (courseName.getText().toString().replaceAll("\\s+", "").equals("") || cc.equals("")) {
                Toast myToast = Toast.makeText(getApplicationContext(), "Empty name or course code field", Toast.LENGTH_SHORT);
                myToast.show();
                returnToMain(view);
            }

            RealtimeDatabase.checkExists(cc, new CheckExistsCallback() {
                @Override
                public void onCallback(boolean exists) {
                    if (!exists) {
                        Course course = new Course();
                        StringBuilder toast = new StringBuilder();
                        course.setName(courseName.getText().toString());
                        course.setCourseCode(cc);
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
                        returnToMain(view);
                    } else {
                        Toast myToast = Toast.makeText(getApplicationContext(), cc + " already exists", Toast.LENGTH_SHORT);
                        myToast.show();
                        returnToMain(view);
                    }
                }
            });
        });

        cancelButton.setOnClickListener(this::returnToMain);
    }

    private void returnToMain(View view) {
        Intent intent = new Intent(view.getContext(), AdminMainActivity.class);
        startActivity(intent);
        finish();
    }
}

