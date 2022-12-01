package com.example.b07project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
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

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Course");

        setContentView(R.layout.add_screen);

        courseName = findViewById(R.id.cname_add);
        courseCode = findViewById(R.id.ccode_add);
        InputFilter filter = new InputFilter() { //prevents inputting space
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }

        };
        courseCode.setFilters(new InputFilter[] { filter });
        saveButton = findViewById(R.id.addcoursebutton);
        cancelButton = findViewById(R.id.cancel_add);
        checkFall = findViewById(R.id.fallcheck_add);
        checkWinter = findViewById(R.id.wintercheck_add);
        checkSummer = findViewById(R.id.summercheck_add);
        prereqText = findViewById(R.id.cprereqs_add);
        courseList = getIntent().getParcelableArrayListExtra("courseList");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean createClass = true;
                String cc = courseCode.getText().toString();
                for(int i = 0; i < courseList.size(); i++)
                {
                    if(cc.equalsIgnoreCase(courseList.get(i).getCourseCode()))
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
                if(createClass) {
                    Course course = new Course();
                    String toast = "";
                    course.setName(courseName.getText().toString());
                    course.setCourseCode(cc);
                    course.setFallOffering(checkFall.isChecked());
                    course.setWinterOffering(checkWinter.isChecked());
                    course.setSummerOffering(checkSummer.isChecked());

                    String editTextString = prereqText.getText().toString();
                    System.out.println(editTextString);
                    editTextString = editTextString.replaceAll("\\s+", "");
                    editTextString = editTextString.toUpperCase();
                    System.out.println(editTextString);
                    List<String> prereqs = new LinkedList<>(Arrays.asList(editTextString.split(",", 0)));
                    System.out.println(Arrays.toString(prereqs.toArray()));
                    prereqs.removeAll(Collections.singleton(""));
                    for (int i = 0; i < prereqs.size(); i++) {
                        if (prereqs.get(i).equals(course.getCourseCode())) {
                            toast += prereqs.get(i);
                            toast += ", ";
                            prereqs.remove(i);
                            i--;
                            break;
                        }
                        int j;
                        for (j = 0; j < courseList.size(); j++) {
                            if (prereqs.get(i).equals(courseList.get(j).getCourseCode())) {
                                break;
                            }
                        }
                        System.out.println(j == courseList.size());
                        if (j == courseList.size()) {
                            toast += prereqs.get(i);
                            toast += ", ";
                            prereqs.remove(i);
                            i--;
                        }
                    }
                    if (!toast.equals("")) {
                        toast = toast.substring(0, toast.length() - 2) + " not available prerequisite";
                        Toast myToast = Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT);
                        myToast.show();
                    }
                    course.setPrerequisites(prereqs);
                    courseList.add(course);
                    System.out.println(Arrays.toString(prereqs.toArray()));

                    returnToMain(view);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToMain(view);
            }
        });


    }

    private void returnToMain(View view) {
        Intent intent = new Intent(view.getContext(),MainActivity.class);
        intent.putParcelableArrayListExtra("newCourseList", courseList);
        System.out.println(courseList);
        startActivity(intent);
        finish();
    }
}

