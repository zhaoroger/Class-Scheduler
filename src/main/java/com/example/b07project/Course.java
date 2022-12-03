package com.example.b07project;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public final class Course implements Comparable<Course>, Parcelable {
    private String name;
    private String courseCode;
    private boolean offeredInFall;
    private boolean offeredInWinter;
    private boolean offeredInSummer;
    private List<String> prerequisites = new ArrayList<String>();

    // No argument constructor
    public Course() {
    }

    // Name and course code constructor
    public Course(String name, String courseCode) {
        this.name = name;
        this.courseCode = courseCode;
    }

    // Name, course code, and offering sessions constructor
    public Course(String name, String courseCode, boolean offeredInFall, boolean winter, boolean offeredInSummer) {
        this.name = name;
        this.courseCode = courseCode;
        this.offeredInFall = offeredInFall;
        this.offeredInWinter = winter;
        this.offeredInSummer = offeredInSummer;
    }

    // Name, course code and prerequisites constructor
    public Course(String name, String courseCode, List<String> prerequisites) {
        this.name = name;
        this.courseCode = courseCode;
        this.prerequisites = prerequisites;
    }

    // Name, course code, offering sessions, and prerequisites constructor
    public Course(String name, String courseCode, boolean offeredInFall, boolean winter, boolean offeredInSummer, List<String> prerequisites) {
        this.name = name;
        this.courseCode = courseCode;
        this.offeredInFall = offeredInFall;
        this.offeredInWinter = winter;
        this.offeredInSummer = offeredInSummer;
        this.prerequisites = prerequisites;
    }

    protected Course(Parcel in) {
        name = in.readString();
        courseCode = in.readString();
        offeredInFall = in.readByte() != 0;
        offeredInWinter = in.readByte() != 0;
        offeredInSummer = in.readByte() != 0;
        prerequisites = in.createStringArrayList();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode.toUpperCase();
    }

    public boolean isOfferedInFall() {
        return offeredInFall;
    }

    public void setOfferedInFall(boolean offeredInFall) {
        this.offeredInFall = offeredInFall;
    }

    public boolean isOfferedInWinter() {
        return offeredInWinter;
    }

    public void setOfferedInWinter(boolean offeredInWinter) {
        this.offeredInWinter = offeredInWinter;
    }

    public boolean isOfferedInSummer() {
        return offeredInSummer;
    }

    public void setOfferedInSummer(boolean offeredInSummer) {
        this.offeredInSummer = offeredInSummer;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public void addPrerequisite(String prerequisite) {
        prerequisites.add(prerequisite);
    }

    public void removePrerequisite(String prerequisite) {
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
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Course other = (Course) obj;
        if (courseCode == null) {
            if (other.courseCode != null) return false;
        } else if (!courseCode.equals(other.courseCode)) return false;
        return true;
    }

    @Override
    public String toString() {
        return courseCode + ": " + name;
    }

    @Override
    public int compareTo(Course otherCourse) {
        return courseCode.compareToIgnoreCase(otherCourse.getCourseCode());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(courseCode);
        parcel.writeByte((byte) (offeredInFall ? 1 : 0));
        parcel.writeByte((byte) (offeredInWinter ? 1 : 0));
        parcel.writeByte((byte) (offeredInSummer ? 1 : 0));
        parcel.writeStringList(prerequisites);
    }
}
