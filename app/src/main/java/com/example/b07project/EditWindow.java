package com.example.b07project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Course Details");

        setContentView(R.layout.edit_screen);

        course = (Course) getIntent().getParcelableExtra("course");
        courseList = getIntent().getParcelableArrayListExtra("courseList");
        setText(course);
        courseName = findViewById(R.id.cname);
        courseCode = findViewById(R.id.ccode);
        courseCode = findViewById(R.id.ccode);
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
        saveButton = findViewById(R.id.savebutton);
        cancelButton = findViewById(R.id.cancel);
        deleteButton = findViewById(R.id.delete);
        checkFall = findViewById(R.id.fallcheck);
        checkWinter = findViewById(R.id.wintercheck);
        checkSummer = findViewById(R.id.summercheck);
        prereqText = findViewById(R.id.cprereqs);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean createClass = true;
                String cc = courseCode.getText().toString();
                for(int i = 0; i < courseList.size(); i++)
                {
                    if(cc.equalsIgnoreCase(courseList.get(i).getCourseCode()) && !cc.toString().equalsIgnoreCase(course.getCourseCode()))
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
                    String toast = "";
                    courseList.get(index).setName(courseName.getText().toString());
                    courseList.get(index).setCourseCode(cc);
                    courseList.get(index).setFallOffering(checkFall.isChecked());
                    courseList.get(index).setWinterOffering(checkWinter.isChecked());
                    courseList.get(index).setSummerOffering(checkSummer.isChecked());

                    String editTextString = prereqText.getText().toString();
                    System.out.println(editTextString);
                    editTextString = editTextString.replaceAll("\\s+", "");
                    editTextString = editTextString.toUpperCase();
                    System.out.println(editTextString);
                    List<String> prereqs = new LinkedList<>(Arrays.asList(editTextString.split(",", 0)));
                    System.out.println(Arrays.toString(prereqs.toArray()));
                    prereqs.removeAll(Collections.singleton(""));
                    for (int i = 0; i < prereqs.size(); i++) {
                        if (prereqs.get(i).equals(courseList.get(index).getCourseCode())) {
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
                    courseList.get(index).setPrerequisites(prereqs);
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

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseList.remove(course);
                returnToMain(view);
            }
        });

    }


    private void setText(Course course) {
        String offerings = "";
        String prerequisites = "";
        List listOfPrereqs = course.getPrerequisites();

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
            prerequisites += listOfPrereqs.get(i) + ", ";
        }
        if(prerequisites.length() > 0) {prerequisites = prerequisites.substring(0, prerequisites.length()-2);}
        prereqText.setText(prerequisites);

    }

    private void returnToMain(View view) {
        Intent intent = new Intent(view.getContext(),MainActivity.class);
        intent.putParcelableArrayListExtra("newCourseList", courseList);
        System.out.println(courseList);
        startActivity(intent);
        finish();
    }
}
