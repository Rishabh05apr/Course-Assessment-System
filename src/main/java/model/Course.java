package model;

import java.sql.Timestamp;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.sql.Date;

/**
 * Created by pinak on 10/27/2017.
 */
public class Course {
    private String courseID;
    private String courseName;
    private Timestamp startDate;
    private Timestamp endDate;

    public Course() {
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
