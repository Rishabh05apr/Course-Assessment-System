package dao;

import model.Topic;
import util.JdbcDataUtil;
import util.Logger;
import util.CommonUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class TopicDAO {
    private static Logger loggers = Logger.getLogger(TopicDAO.class);
    public List<Topic> getAllTopicsForCourse(String courseId){

        if(!CommonUtil.isValidString(courseId))
            return null;
        List<Topic> topicList = null;
        String query = "SELECT t.TOPIC_ID, t.TOPIC_NAME, t.C_ID FROM TOPIC t where t.C_ID = ?";
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
                topicList = new LinkedList<Topic>();
                while (rst.next()) {
                    Topic topic = new Topic();
                    topic.setTopicId(rst.getInt("TOPIC_ID"));
                    topic.setTopicName(rst.getString("TOPIC_NAME"));
                    topic.setCourseId(courseId);

                    topicList.add(topic);
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
        return topicList;
    }

    public List<Topic> getAllTopicsForAProf(String userName){


        List<Topic> topicList = null;
        String query = "SELECT t.TOPIC_ID, t.TOPIC_NAME FROM TOPIC t where " +
                "t.c_id in (Select c_id from teaches where u_username = ?)";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;

        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1,userName);
            rst = ps.executeQuery();

            if (rst != null) {
                topicList = new LinkedList<Topic>();
                while (rst.next()) {
                    Topic topic = new Topic();
                    topic.setTopicId(rst.getInt("TOPIC_ID"));
                    topic.setTopicName(rst.getString("TOPIC_NAME"));

                    topicList.add(topic);
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
        return topicList;
    }


    public Integer getLatestTopicId(){
        PreparedStatement ps =null;
        Integer latestTopicId = 0;
        String query = "SELECT MAX(t.TOPIC_ID) AS TOPIC_ID FROM TOPIC t";
        Connection con = null;
        ResultSet rst = null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            rst = ps.executeQuery();
            if (rst != null) {
                if (rst.next()) {
                    latestTopicId = rst.getInt("TOPIC_ID");
                } else
                    loggers.error("Resultset is empty...");
            } else
                loggers.error("Data could not be saved...");

        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (rst != null) {
                    rst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return latestTopicId;
    }

    public boolean saveTopic(Topic topic) {
        PreparedStatement ps = null;
        Connection con = null;
        String query = "INSERT INTO TOPIC (TOPIC_ID, TOPIC_NAME, C_ID) VALUES (?,?,?)";
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            ps.setInt(1, topic.getTopicId());
            ps.setString(2, topic.getTopicName());
            ps.setString(3, topic.getCourseId());

            int result = ps.executeUpdate();
            if (result == 1) {
                con.commit();
                return true;
            } else {
                con.rollback();
                return false;
            }

        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                return false;
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
    }

    public boolean addTopicToCourse(Topic topic){
        if(topic == null)
            return false;
        PreparedStatement ps = null;
        Connection con = null;
        String query = "INSERT INTO TOPIC (TOPIC_ID, TOPIC_NAME, C_ID) VALUES (?,?,?)";
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            ps.setInt(1,topic.getTopicId());
            ps.setString(2,topic.getTopicName());
            ps.setString(3,topic.getCourseId());

            int result = ps.executeUpdate();
            if (result == 1)
                return true;
            else{
                con.rollback();
                return false;
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
    }
}
