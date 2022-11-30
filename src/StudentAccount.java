import java.util.ArrayList;
import java.util.List;

final class StudentAccount extends Account implements Student {
    List<String> courses = new ArrayList<String>();

    public StudentAccount() {
    }

    public StudentAccount(String username, String password, String name) {
        super(username, password, name);
    }

    public StudentAccount(String username, String password, String name, List<String> courses) {
        super(username, password, name);
        this.courses = courses;
    }

    @Override
    public List<String> getCourses() {
        return courses;
    }

    @Override
    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    @Override
    public void addCourse(String course) {
        if (!courses.contains(course)) {
            courses.add(course);
        }
    }

    @Override
    public void removeCourse(String course) {
        courses.remove(course);
    }

    @Override
    public void generateCourseTimeline(List<String> courses) {
        // TODO
    }
}
