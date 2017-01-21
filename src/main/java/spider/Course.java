package spider;

import java.util.List;

public class Course {
    String name;
    List<CourseClass> courseClassList;
    double score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseClass> getCourseClassList() {
        return courseClassList;
    }

    public void setCourseClassList(List<CourseClass> courseClassList) {
        this.courseClassList = courseClassList;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}