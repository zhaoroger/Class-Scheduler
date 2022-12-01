package com.example.b07project;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class Course implements Parcelable {
    private String name;
    private String courseCode;
    private boolean fall;
    private boolean winter;
    private boolean summer;
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
    public Course(String name, String courseCode, boolean fall, boolean winter, boolean summer) {
        this.name = name;
        this.courseCode = courseCode;
        this.fall = fall;
        this.winter = winter;
        this.summer = summer;
    }

    // Name, course code and prerequisites constructor
    public Course(String name, String courseCode, List<String> prerequisites) {
        this.name = name;
        this.courseCode = courseCode;
        this.prerequisites = prerequisites;
    }

    // Name, course code, offering sessions, and prerequisites constructor
    public Course(String name, String courseCode, boolean fall, boolean winter, boolean summer,
                  List<String> prerequisites) {
        this.name = name;
        this.courseCode = courseCode;
        this.fall = fall;
        this.winter = winter;
        this.summer = summer;
        this.prerequisites = prerequisites;
    }

    protected Course(Parcel in) {
        name = in.readString();
        courseCode = in.readString();
        fall = in.readByte() != 0;
        winter = in.readByte() != 0;
        summer = in.readByte() != 0;
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

    protected void setName(String name) {
        this.name = name;
    }

    public String getCourseCode() {
        return courseCode;
    }

    protected void setCourseCode(String courseCode) {
        this.courseCode = courseCode.toUpperCase();
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

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    protected void addPrerequisite(String prerequisite) {
        prerequisites.add(prerequisite);
    }

    protected void removePrerequisite(String prerequisite) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(courseCode);
        parcel.writeByte((byte) (fall ? 1 : 0));
        parcel.writeByte((byte) (winter ? 1 : 0));
        parcel.writeByte((byte) (summer ? 1 : 0));
        parcel.writeStringList(prerequisites);
    }
}
