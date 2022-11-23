import java.util.HashSet;

final class AdminAccount extends Account implements Admin {

    private AllCourses allCourses = AllCourses.getInstance();

    public AdminAccount(String username, String password, String name) {
        super(username, password, name);
    }

    @Override
    public HashSet<Course> viewCourses() {
        return allCourses.getAllCourses();
    }

    @Override
    public void addCourse(Course course) {
        allCourses.addCourse(course);

    }

    @Override
    public void removeCourse(Course course) {
        allCourses.removeCourse(course);
    }

    @Override
    public void editCourse(Course course) {
        // TODO
    }
}
