import java.util.LinkedHashSet;

public interface Student {
    public LinkedHashSet<Course> getCourses();

    public void setCourses(LinkedHashSet<Course> courses);

    public void addCourse(Course course);

    public void removeCourse(Course course);

    public void generateCourseTimeline(LinkedHashSet<Course> courses);
}
