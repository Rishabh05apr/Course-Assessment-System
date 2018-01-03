package dao;

import model.*;
import util.JdbcDataUtil;
import util.Logger;
import util.CommonUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by pinak on 10/24/2017.
 */
public class CourseDAO {
    private static Logger loggers = Logger.getLogger(CourseDAO.class);

    public Course getCourseDetails(String courseID, String prof) {
        if (courseID == null)
            return null;
        Course courseDeails = null;
        String query = "SELECT c.C_ID, c.C_NAME, c.C_START_DATE, c.C_END_DATE FROM COURSE c where c.C_ID IN (SELECT DISTINCT (C_ID) FROM TEACHES t where t.C_ID = ? AND t.U_USERNAME = ?)";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, courseID);
            ps.setString(2, prof);
            rst = ps.executeQuery();
            if (rst != null) {
                if (rst.next()) {
                    courseDeails = new Course();
                    courseDeails.setCourseID(rst.getString("C_ID"));
                    courseDeails.setCourseName(rst.getString("C_NAME"));
                    courseDeails.setStartDate(rst.getTimestamp("C_START_DATE"));
                    courseDeails.setEndDate(rst.getTimestamp("C_END_DATE"));
                } else
                    loggers.error("Resultset is empty...");
            } else
                loggers.error("Resultset is null...");
        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return courseDeails;
    }

    public Course getTACourseDetails(String courseID, String ta) {
        if (courseID == null)
            return null;
        Course courseDeails = null;
        String query = "SELECT c.C_ID, c.C_NAME, c.C_START_DATE, c.C_END_DATE FROM COURSE c where c.C_ID IN (SELECT C_ID FROM TA_FOR where C_ID = ? AND STUD_ID = ?)";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, courseID);
            ps.setString(2, ta);
            rst = ps.executeQuery();
            if (rst != null) {
                if (rst.next()) {
                    courseDeails = new Course();
                    courseDeails.setCourseID(rst.getString("C_ID"));
                    courseDeails.setCourseName(rst.getString("C_NAME"));
                    courseDeails.setStartDate(rst.getTimestamp("C_START_DATE"));
                    courseDeails.setEndDate(rst.getTimestamp("C_END_DATE"));
                } else
                    loggers.error("Resultset is empty...");
            } else
                loggers.error("Resultset is null...");
        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return courseDeails;
    }
    public String getProfForACourse(String courseID) {
        if (courseID == null)
            return null;
        String query = "SELECT U_USERNAME FROM TEACHES WHERE C_ID = ?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        String username=null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, courseID);

            rst = ps.executeQuery();
            if (rst != null) {
                if (rst.next()) {
                    username=rst.getString("u_username");
                } else
                    loggers.error("Resultset is empty...");
            } else
                loggers.error("Resultset is null...");
        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return username;
    }

    public List<Course> getCourseList(String profId) {
        if (profId == null || profId.trim().equals(""))
            return null;
        List<Course> courseList = null;
        String query = "SELECT c.C_ID, c.C_NAME, c.C_START_DATE, c.C_END_DATE FROM COURSE c where c.C_ID IN (SELECT DISTINCT (C_ID) FROM TEACHES t where t.U_USERNAME = ?)";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, profId);
            rst = ps.executeQuery();
            if (rst != null) {
                courseList = new LinkedList<Course>();
                while (rst.next()) {
                    Course courseDetails = new Course();
                    courseDetails.setCourseID(rst.getString("C_ID"));
                    courseDetails.setCourseName(rst.getString("C_NAME"));
                    courseDetails.setStartDate(rst.getTimestamp("C_START_DATE"));
                    courseDetails.setEndDate(rst.getTimestamp("C_END_DATE"));

                    courseList.add(courseDetails);
                }
            } else
                loggers.error("Resultset is null...");
        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return courseList;
    }


    public List<Course> getStudentCourseList(String studentID) {
        if (studentID == null || studentID.trim().equals(""))
            return null;
        List<Course> courseList = null;
        String query = "SELECT c.C_ID, c.C_NAME, c.C_START_DATE, c.C_END_DATE FROM COURSE c where c.C_ID IN (SELECT DISTINCT (C_ID) FROM ENROLLS_IN where U_ID = ?)";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, studentID);
            rst = ps.executeQuery();
            if (rst != null) {
                courseList = new LinkedList<Course>();
                while (rst.next()) {
                    Course courseDetails = new Course();
                    courseDetails.setCourseID(rst.getString("C_ID"));
                    courseDetails.setCourseName(rst.getString("C_NAME"));
                    courseDetails.setStartDate(rst.getTimestamp("C_START_DATE"));
                    courseDetails.setEndDate(rst.getTimestamp("C_END_DATE"));

                    courseList.add(courseDetails);
                }
            } else
                loggers.error("Resultset is null...");
        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return courseList;
    }


    public List<Course> getTACourseList(String studentID) {
        if (studentID == null || studentID.trim().equals(""))
            return null;
        List<Course> courseList = null;
        String query = "SELECT c.C_ID, c.C_NAME, c.C_START_DATE, c.C_END_DATE FROM COURSE c where c.C_ID IN (SELECT C_ID FROM TA_FOR where STUD_ID = ?)";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, studentID);
            rst = ps.executeQuery();
            if (rst != null) {
                courseList = new LinkedList<Course>();
                while (rst.next()) {
                    Course courseDetails = new Course();
                    courseDetails.setCourseID(rst.getString("C_ID"));
                    courseDetails.setCourseName(rst.getString("C_NAME"));
                    courseDetails.setStartDate(rst.getTimestamp("C_START_DATE"));
                    courseDetails.setEndDate(rst.getTimestamp("C_END_DATE"));

                    courseList.add(courseDetails);
                }
            } else
                loggers.error("Resultset is null...");
        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return courseList;
    }

    public boolean saveCourseDetails(Course course, User user) {
        if (course == null)
            return false;

        PreparedStatement ps = null;
        Connection con = null;
        String query = "INSERT INTO COURSE (C_ID, C_NAME, C_START_DATE, C_END_DATE) VALUES (?,?,?,?)";
        String insertQuery = "INSERT INTO TEACHES (U_ID, C_ID, U_USERNAME) VALUES (?,?,?)";
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            ps.setString(1, course.getCourseID());
            ps.setString(2, course.getCourseName());
            ps.setTimestamp(3, course.getStartDate());
            ps.setTimestamp(4, course.getEndDate());

            int result = ps.executeUpdate();
            if (result == 1){
                //UserDAO userDAO = new UserDAO();
                //User user = userDAO.getUserDetails(profID);
                if(user!=null)
                    ps = con.prepareStatement(insertQuery);
                ps.setInt(1,user.getId());
                ps.setString(2,course.getCourseID());
                ps.setString(3,user.getUserName());
                if(ps.executeUpdate()==1){
                    con.commit();
                    return true;
                }

                else{
                    con.rollback();
                    return false;
                }
            }

        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

        }
        return false;
    }

    public boolean isEnrolledToCourse(Integer userId, String courseId){
        if (!CommonUtil.isValidString(courseId) || !CommonUtil.isValidInteger(userId))
            return false;
        String query = "SELECT COUNT(C_ID) AS RESULT FROM ENROLLS_IN WHERE C_ID = ? AND U_ID = ?";
        Connection con = null;
        ResultSet rst = null;
        PreparedStatement ps =null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            ps=con.prepareStatement(query);
            ps.setString(1, courseId);
            ps.setInt(2, userId);
            rst = ps.executeQuery();
            if (rst != null) {
                while (rst.next()) {
                    if(rst.getInt("RESULT") >0){
                        loggers.info(userId+" already enrolled in course "+courseId);
                        return true;
                    }
                }
            } else {
                loggers.error("Resultset is null...");
                return false;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
            try {
                con.rollback();
            } catch (SQLException e1) {
                loggers.error("Error while checking isEnrolled", e1);
                return false;
            }
        } finally {
            try {
                if (rst != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
                return false;
            }
        }
        return false;
    }

    public boolean enrollInCourse(Integer userId, String courseId) {
        if (!CommonUtil.isValidString(courseId) || !CommonUtil.isValidInteger(userId))
            return false;
        String query = "INSERT INTO ENROLLS_IN (C_ID, U_ID) VALUES (?,?)";
        Connection con = null;
        PreparedStatement ps =null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps=con.prepareStatement(query);
            ps.setString(1, courseId);
            ps.setInt(2, userId);
            if (ps.executeUpdate() == 1) {
                con.commit();
                return true;
            } else {
                loggers.error("Data could not be saved...");
                loggers.warn("Rolling back transaction....");
                con.rollback();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return false;
    }
    public List<User> getTAsForCourse(String courseId){
        if(!CommonUtil.isValidString(courseId))
            return null;
        List<User> taList  = null;
        String query = "SELECT u.U_FIRST_NAME, u.U_LAST_NAME, u.U_USERNAME, u.U_ID, u.U_EMAIL, u.U_ROLE FROM USER_DETAILS u where " +
                "u.U_USERNAME IN (SELECT DISTINCT (t.STUD_ID) FROM TA_FOR t where t.C_ID = ?)";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, courseId);
            rst = ps.executeQuery();
            if (rst != null) {
                taList = new LinkedList<User>();
                while (rst.next()) {
                    User ta = new User();
                    ta.setId(rst.getInt("U_ID"));
                    ta.setFirstName(rst.getString("U_FIRST_NAME"));
                    ta.setLastName(rst.getString("U_LAST_NAME"));
                    ta.setUserName(rst.getString("U_USERNAME"));
                    ta.setEmail(rst.getString("U_EMAIL"));
                    ta.setRole(rst.getString("U_ROLE"));
                    taList.add(ta);
                }
            } else
                loggers.error("Resultset is null...");
        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return taList;
    }

    public boolean deleteFromCourse(Integer userId, String courseId) {
        if (!CommonUtil.isValidString(courseId) || !CommonUtil.isValidInteger(userId))
            return false;
        String query = "  DELETE FROM ENROLLS_IN WHERE  C_ID = ? AND U_ID = ?";
        Connection con = null;
        PreparedStatement ps =null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps=con.prepareStatement(query);
            ps.setString(1, courseId);
            ps.setInt(2, userId);
            if (ps.executeUpdate() == 1) {
                con.commit();
                return true;
            } else {
                loggers.error("Data could not be saved...");
                loggers.warn("Rolling back transaction....");
                con.rollback();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return false;
    }

    public List<Topic> getTopicList(String courseID) {
        List<Topic> topics = null;
        String query = "SELECT t.TOPIC_ID,t.TOPIC_NAME,t.C_ID FROM TOPIC t where t.C_ID = ?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, courseID);
            rst = ps.executeQuery();
            while (rst != null) {
                topics = new LinkedList<Topic>();
                while (rst.next()) {
                    Topic topic = new Topic();
                    topic.setTopicId(rst.getInt("TOPIC_ID"));
                    topic.setTopicName(rst.getString("TOPIC_NAME"));
                    topics.add(topic);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
        } finally {
            try {
                if (rst != null) {
                    rst.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return topics;
    }
}
