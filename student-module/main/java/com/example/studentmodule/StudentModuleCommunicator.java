package com.example.studentmodule;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Stack;

public final class StudentModuleCommunicator {
    private static StudentModuleCommunicator communicator;
    private static StudentAccount studentAccount;
    private static ArrayList<Course> sortedAllCoursesArray;
    private static ArrayList<Course> sortedStudentCoursesArray;
    private static ArrayList<Course> futureCoursesArray;
    private static ArrayList<String> timelineCoursesStringArray;
    private static ArrayList<Course> possibleFutureCoursesArray;

    private StudentModuleCommunicator() {
        StudentModuleCommunicator.sortedStudentCoursesArray = new ArrayList<Course>();
        StudentModuleCommunicator.futureCoursesArray = new ArrayList<Course>();
        StudentModuleCommunicator.possibleFutureCoursesArray = new ArrayList<Course>();
        StudentModuleCommunicator.timelineCoursesStringArray = new ArrayList<String>();
        StudentModuleCommunicator.sortedAllCoursesArray =
                new ArrayList<Course>(AllCourses.getInstance().getAllCourses());
        modularSync();
    }

    public static StudentModuleCommunicator getInstance() {
        if (communicator == null) {
            communicator = new StudentModuleCommunicator();
            communicator.modularSync();
        }
        return communicator;
    }

    public void setStudentAccount(StudentAccount newStudentAccount) {
        StudentModuleCommunicator.studentAccount = newStudentAccount;
        StudentModuleCommunicator.sortedStudentCoursesArray =
                new ArrayList<Course>(StudentModuleCommunicator.studentAccount.getCourses());
        Collections.sort(StudentModuleCommunicator.sortedStudentCoursesArray);
        modularSync();
    }

    public StudentAccount getStudentAccount() {
        if (studentAccount == null)
            throw new RuntimeException("The student account was not passed on to the module communicator.");
        return studentAccount;
    }

    public ArrayList<Course> getSortedStudentCoursesArray() {
        Collections.sort(StudentModuleCommunicator.sortedStudentCoursesArray);
        Collections.reverse(StudentModuleCommunicator.sortedStudentCoursesArray);
        modularSync();
        return StudentModuleCommunicator.sortedStudentCoursesArray;
    }

    public void addCourseToSortedStudentCourses(Course course) {
        StudentModuleCommunicator.sortedStudentCoursesArray.add(course);
        Collections.sort(StudentModuleCommunicator.sortedStudentCoursesArray);
        Collections.reverse(StudentModuleCommunicator.sortedStudentCoursesArray);
        modularSync();
    }

    public void removeCourseFromSortedStudentCourses(Course course) {
        StudentModuleCommunicator.sortedStudentCoursesArray.remove(course);
        modularSync();
    }

    public String generateSemesterLabel(int yearRightNow, int monthRightNow, int semester) {
        int currentSemester;

        if (monthRightNow > 8)
            currentSemester = 0;
        else if (monthRightNow < 5)
            currentSemester = 1;
        else
            currentSemester = 2;

        for (int i=0; i<semester; i++) {
            currentSemester += 1;
            if (currentSemester%3 == 1)
                yearRightNow += 1;
        }

        String s = "";
        if (currentSemester%3 == 0)
            s = "Fall";
        else if (currentSemester%3 == 1)
            s = "Winter";
        else
            s = "Summer";

        return  "<< "+s + " Semester " + " | " + " Year " + yearRightNow +" >>";

    }


    public ArrayList<Course> getSortedAllCoursesArray() {
        Collections.sort(StudentModuleCommunicator.sortedAllCoursesArray);
        modularSync();
        return StudentModuleCommunicator.sortedAllCoursesArray;
    }

    public void updateDataBase() {
        LinkedHashSet<Course> studentCourses = new LinkedHashSet<Course>();

        studentCourses.addAll(StudentModuleCommunicator.sortedStudentCoursesArray);

        StudentModuleCommunicator.studentAccount.setCourses(studentCourses);
        //code to push the account to Firebase
    }

    /*
    If course exists in futureCoursesArray, it will be removed. If not, it will be added.
     */
    public void alterCourseStateInFutureCourses(Course course){
        if (StudentModuleCommunicator.futureCoursesArray.contains(course))
            StudentModuleCommunicator.futureCoursesArray.remove(course);
        else
            StudentModuleCommunicator.futureCoursesArray.add(course);
        modularSync();
    }

    public ArrayList<Course> getPossibleFutureCoursesArray() {
        modularSync();
        return StudentModuleCommunicator.possibleFutureCoursesArray;
    }

    public ArrayList<Course> getFutureCoursesArray() {
        modularSync();
        //return (ArrayList<Course>) StudentModuleCommunicator.futureCoursesArray.clone();
        return StudentModuleCommunicator.futureCoursesArray;
    }

    public ArrayList<String> getTimelineCoursesStringArray() {
        modularSync();
        return timelineCoursesStringArray;
    }

    private void updateTimelineCoursesStringArray() {
        Date date = Calendar.getInstance().getTime();

        SimpleDateFormat yearFormat =
                new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat monthFormat =
                new SimpleDateFormat("MM", Locale.getDefault());
        int year = Integer.parseInt(yearFormat.format(date));
        int month = Integer.parseInt(monthFormat.format(date));

        Log.i("date", ""+year+""+month);
        int semester;
        if (4 < month && month < 9)
            semester = 0;
        else if (month > 8)
            semester = -1;
        else
            semester = -2;

        //to do: update timelineCoursesArray
        StudentModuleCommunicator.timelineCoursesStringArray.clear();
        Log.i("FutureCourseArray", ""+futureCoursesArray.size());
        ArrayList<Course> wholeStack = new ArrayList<Course>(futureCoursesArray);
        ArrayList<Course> fallStack = new ArrayList<Course>();
        ArrayList<Course> winterStack = new ArrayList<Course>();
        ArrayList<Course> summerStack = new ArrayList<Course>();
        String fallLabel = "";
        String winterLabel = "";
        String summerLabel = "";
        timelineCoursesStringArray.clear();
        while (wholeStack.size() > 0) {
            semester += 1;
            if (semester > 0) {
                for (Course course : wholeStack) {
                    if (course.isOfferedInFall() && !fallStack.contains(course)) {
                        fallStack.add(course);
                        fallLabel = generateSemesterLabel(year, month, semester);
                    }
                    if (fallStack.size() == 6)
                        break;
                }
                wholeStack.removeAll(fallStack);
            } else {
                Log.i("asdf", "skipping fall"+semester);
                fallLabel = "";
            }
            semester += 1;

            if (semester > 0) {
                for (Course course : wholeStack) {
                    if (course.isOfferedInWinter() && !winterStack.contains(course)) {
                        winterStack.add(course);
                        winterLabel = generateSemesterLabel(year, month, semester);
                    }
                    if (winterStack.size() == 6)
                        break;
                }
                wholeStack.removeAll(winterStack);
            } else {
                winterLabel = "";
            }

            semester += 1;

            for (Course course:wholeStack) {
                if (course.isOfferedInSummer() && !summerStack.contains(course)) {
                    summerStack.add(course);
                    summerLabel = generateSemesterLabel(year, month, semester);
                }
                if (summerStack.size() == 6)
                    break;
            }
            wholeStack.removeAll(summerStack);


            Log.i("FallStack", ""+fallStack.size() + fallLabel);
            Log.i("WinterStack", ""+winterStack.size() + winterLabel);
            Log.i("SummerStack", ""+summerStack.size() + summerLabel);

            ArrayList<Course> toBeRemoved = new ArrayList<Course>();
            if (fallStack.size() > 0 && fallLabel.length() > 0) {
                timelineCoursesStringArray.add(fallLabel);

                for (int i=0; (i < 6) && (i < fallStack.size()); i++) {
                    timelineCoursesStringArray.add("" + (i+1) + ") " + fallStack.get(i).toString());
                    toBeRemoved.add(fallStack.get(i));
                }
                fallStack.removeAll(toBeRemoved);
                toBeRemoved.clear();

            }

            if (winterStack.size() > 0 && winterLabel.length() > 0) {
                timelineCoursesStringArray.add(winterLabel);
                for (int i = 0; (i < 6) && (i < winterStack.size()); i++) {
                    timelineCoursesStringArray.add("" + (i+1) + ") " + winterStack.get(i).toString());
                    toBeRemoved.add(winterStack.get(i));
                }
                winterStack.removeAll(toBeRemoved);
                toBeRemoved.clear();
            }

            if (summerStack.size() > 0) {
                timelineCoursesStringArray.add(summerLabel);
                for (int i = 0; (i < 6) && (i < summerStack.size()); i++) {
                    timelineCoursesStringArray.add("" + (i+1) + ") " + summerStack.get(i).toString());
                    toBeRemoved.add(summerStack.get(i));
                }
                summerStack.removeAll(toBeRemoved);
                toBeRemoved.clear();
            }

            fallStack.clear();
            winterStack.clear();
            summerStack.clear();
            fallLabel = "";
            winterLabel = "";
            summerLabel = "";
        }
        Log.i("timeline length", ""+timelineCoursesStringArray.size());
    }

    private void updatePossibleFutureCoursesArray() {
        ArrayList<Course> toBeRemoved = new ArrayList<Course>();
        ArrayList<Course> toBeAdded = new ArrayList<Course>();

        for (Course course:StudentModuleCommunicator.possibleFutureCoursesArray) {
            for (Course c:course.getPrerequisites()) {
                if (!StudentModuleCommunicator.sortedStudentCoursesArray.contains(c))
                    if (!toBeRemoved.contains(c))
                        toBeRemoved.add(c);
            }
        }

        StudentModuleCommunicator.possibleFutureCoursesArray.removeAll(toBeRemoved);

        for (Course course:StudentModuleCommunicator.sortedAllCoursesArray) {
            boolean canTake = true;
            for (Course c:course.getPrerequisites())
                if (!StudentModuleCommunicator.sortedStudentCoursesArray.contains(c)) {
                    canTake = false;
                    break;
                }
            if (canTake)
                toBeAdded.add(course);
        }

        for (Course course:toBeAdded)
            if (!StudentModuleCommunicator.possibleFutureCoursesArray.contains(course)
            && !StudentModuleCommunicator.sortedStudentCoursesArray.contains(course))
                StudentModuleCommunicator.possibleFutureCoursesArray.add(course);

        StudentModuleCommunicator.possibleFutureCoursesArray
                .removeAll(StudentModuleCommunicator.sortedStudentCoursesArray);
    }

    private void updateFutureCoursesArray() {
        ArrayList<Course> toBeRemoved = new ArrayList<Course>();
        for (Course course:StudentModuleCommunicator.futureCoursesArray)
            if (StudentModuleCommunicator.sortedStudentCoursesArray.contains(course))
                toBeRemoved.add(course);

        StudentModuleCommunicator.futureCoursesArray.removeAll(toBeRemoved);
    }

    private void modularSync() {
        updatePossibleFutureCoursesArray();
        updateFutureCoursesArray();
        updateTimelineCoursesStringArray();
    }

}
