package com.example.studentmodule;

import java.util.HashSet;

public final class Course implements Comparable<Course> {
    private String name;
    private String courseCode;
    private boolean fall;
    private boolean winter;
    private boolean summer;
    private HashSet<Course> prerequisites = new HashSet<Course>();

    // Name and course code constructor
    public Course(String name, String courseCode) {
        this.name = name;
        this.courseCode = courseCode;
    }

    // Name, course code, and offering sessions constructor
    public Course(String name, String courseCode, boolean fall, boolean winter, boolean summer) {
        this.name = name;
        this.courseCode = courseCode;
        this.fall = fall;
        this.winter = winter;
        this.summer = summer;
    }

    // Name, course code and prerequisites constructor
    public Course(String name, String courseCode, HashSet<Course> prerequisites) {
        this.name = name;
        this.courseCode = courseCode;
        this.prerequisites = prerequisites;
    }

    // Name, course code, offering sessions, and prerequisites constructor
    public Course(String name, String courseCode, boolean fall, boolean winter, boolean summer,
            HashSet<Course> prerequisites) {
        this.name = name;
        this.courseCode = courseCode;
        this.fall = fall;
        this.winter = winter;
        this.summer = summer;
        this.prerequisites = prerequisites;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getCourseCode() {
        return courseCode;
    }

    protected void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public boolean isOfferedInFall() {
        return fall;
    }

    protected void setFallOffering(boolean fall) {
        this.fall = fall;
    }

    public boolean isOfferedInWinter() {
        return winter;
    }

    protected void setWinterOffering(boolean winter) {
        this.winter = winter;
    }

    public boolean isOfferedInSummer() {
        return summer;
    }

    protected void setSummerOffering(boolean summer) {
        this.summer = summer;
    }

    public HashSet<Course> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(HashSet<Course> prerequisites) {
        this.prerequisites = prerequisites;
    }

    protected void addPrerequisite(Course prerequisite) {
        prerequisites.add(prerequisite);
    }

    protected void removePrerequisite(Course prerequisite) {
        prerequisites.remove(prerequisite);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((courseCode == null) ? 0 : courseCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Course other = (Course) obj;
        if (courseCode == null) {
            if (other.courseCode != null)
                return false;
        } else if (!courseCode.equals(other.courseCode))
            return false;
        return true;
    }

    // Added by Pejvak to allow nicer display by showing a sorted list of courses
    // in the student module


    @Override
    public String toString() {
        return this.courseCode + ": " + this.name;
    }

    @Override
    public int compareTo(Course course) {
        if (this.name.compareTo(course.getName()) == 0) {
            if (this.isOfferedInFall() && !course.isOfferedInFall())
                return 1;
            if (!this.isOfferedInFall() && course.isOfferedInFall())
                return -1;
            if (this.isOfferedInWinter() && !course.isOfferedInWinter())
                return 1;
            if (!this.isOfferedInWinter() && course.isOfferedInWinter())
                return -1;
            if (this.isOfferedInSummer() && !course.isOfferedInSummer())
                return 1;
            if (!this.isOfferedInSummer() && course.isOfferedInSummer())
                return -1;
            return 0;
        }
        return this.name.compareTo(course.getName());
    }
}
