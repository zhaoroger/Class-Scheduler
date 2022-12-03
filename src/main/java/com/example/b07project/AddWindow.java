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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class AddWindow extends Activity {
    ArrayList<Course> courseList;
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
        courseList = getIntent().getParcelableArrayListExtra("courseList");

        saveButton.setOnClickListener(view -> {
            boolean createClass = true;
            String cc = courseCode.getText().toString();
            for (int i = 0; i < courseList.size(); i++) {
                if (cc.equalsIgnoreCase(courseList.get(i).getCourseCode())) {
                    Toast myToast = Toast.makeText(getApplicationContext(), "Duplicate Class", Toast.LENGTH_SHORT);
                    myToast.show();
                    returnToMain(view);
                    createClass = false;
                }
            }
            if (courseName.getText().toString().replaceAll("\\s+", "").equals("") || cc.equals("")) {
                Toast myToast = Toast.makeText(getApplicationContext(), "Empty name or course code field", Toast.LENGTH_SHORT);
                myToast.show();
                returnToMain(view);
                createClass = false;
            }
            if (createClass) {
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
                for (int i = 0; i < prereqs.size(); i++) {
                    if (prereqs.get(i).equals(course.getCourseCode())) {
                        toast.append(prereqs.get(i));
                        toast.append(", ");
                        prereqs.remove(i);
                        break;
                    }
                    int j;
                    for (j = 0; j < courseList.size(); j++) {
                        if (prereqs.get(i).equals(courseList.get(j).getCourseCode())) {
                            break;
                        }
                    }
                    if (j == courseList.size()) {
                        toast.append(prereqs.get(i));
                        toast.append(", ");
                        prereqs.remove(i);
                        i--;
                    }
                }
                if (!toast.toString().equals("")) {
                    toast = new StringBuilder(toast.substring(0, toast.length() - 2) + " not available prerequisite");
                    Toast myToast = Toast.makeText(getApplicationContext(), toast.toString(), Toast.LENGTH_SHORT);
                    myToast.show();
                }
                course.setPrerequisites(prereqs);
                RealtimeDatabase.addCourse(course);
                returnToMain(view);
            }
        });

        cancelButton.setOnClickListener(this::returnToMain);
    }

    private void returnToMain(View view) {
        Intent intent = new Intent(view.getContext(), AdminMainActivity.class);
        intent.putParcelableArrayListExtra("newCourseList", courseList);
        startActivity(intent);
        finish();
    }
}

