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

public final class EditWindow extends Activity {
    Course course;
    ArrayList<Course> courseList;
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
        courseList = getIntent().getParcelableArrayListExtra("courseList");
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
        courseCode.setFilters(new InputFilter[] { filter });
        saveButton = findViewById(R.id.savebutton);
        cancelButton = findViewById(R.id.cancel);
        deleteButton = findViewById(R.id.delete);
        checkFall = findViewById(R.id.fallcheck);
        checkWinter = findViewById(R.id.wintercheck);
        checkSummer = findViewById(R.id.summercheck);
        prereqText = findViewById(R.id.cprereqs);

        saveButton.setOnClickListener(view -> {
            boolean createClass = true;
            String cc = courseCode.getText().toString();
            for(int i = 0; i < courseList.size(); i++)
            {
                if(cc.equalsIgnoreCase(courseList.get(i).getCourseCode()) && !cc.equalsIgnoreCase(course.getCourseCode()))
                {
                    Toast myToast = Toast.makeText(getApplicationContext(), "Duplicate Class", Toast.LENGTH_SHORT);
                    myToast.show();
                    returnToMain(view);
                    createClass = false;
                }
            }
            if(courseName.getText().toString().replaceAll("\\s+", "").equals("") || cc.equals(""))
            {
                Toast myToast = Toast.makeText(getApplicationContext(), "Empty name or course code field", Toast.LENGTH_SHORT);
                myToast.show();
                returnToMain(view);
                createClass = false;
            }
            if(createClass)
            {
                int index = courseList.indexOf(course);
                StringBuilder toast = new StringBuilder();
                courseList.get(index).setName(courseName.getText().toString());
                courseList.get(index).setCourseCode(cc);
                courseList.get(index).setOfferedInFall(checkFall.isChecked());
                courseList.get(index).setOfferedInWinter(checkWinter.isChecked());
                courseList.get(index).setOfferedInSummer(checkSummer.isChecked());

                String editTextString = prereqText.getText().toString();
                editTextString = editTextString.replaceAll("\\s+", "");
                editTextString = editTextString.toUpperCase();
                List<String> prereqs = new LinkedList<>(Arrays.asList(editTextString.split(",", 0)));
                prereqs.removeAll(Collections.singleton(""));
                for (int i = 0; i < prereqs.size(); i++) {
                    if (prereqs.get(i).equals(courseList.get(index).getCourseCode())) {
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
                courseList.get(index).setPrerequisites(prereqs);

                returnToMain(view);
            }
        });

        cancelButton.setOnClickListener(this::returnToMain);

        deleteButton.setOnClickListener(view -> {
            courseList.remove(course);
            returnToMain(view);
        });

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
        for(int i = 0; i < listOfPrereqs.size(); i++)
        {
            prerequisites.append(listOfPrereqs.get(i)).append(", ");
        }
        if(prerequisites.length() > 0) {
            prerequisites = new StringBuilder(prerequisites.substring(0, prerequisites.length() - 2));}
        prereqText.setText(prerequisites.toString());

    }

    private void returnToMain(View view) {
        Intent intent = new Intent(view.getContext(), AdminMainActivity.class);
        intent.putParcelableArrayListExtra("newCourseList", courseList);
        startActivity(intent);
        finish();
    }
}
