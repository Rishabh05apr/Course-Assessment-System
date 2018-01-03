package dao;

import model.Parameter;
import model.ParameterSet;
import model.Question;
import model.Topic;
import util.CommonUtil;
import util.JdbcDataUtil;
import util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QuestionDAO {
    private static Logger loggers = Logger.getLogger(QuestionDAO.class);

    public boolean saveQuestion(Question question){
        PreparedStatement ps = null;
        Connection con = null;
        String query = "INSERT INTO QUESTIONS (QID, TOPIC_ID, TYPE, QTEXT,DIFFICULTY, HINT, DETAILED_SOLUTION) VALUES (?,?,?,?,?,?,?)";
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            ps.setInt(1, question.getqId());
            ps.setInt(2,question.getTopic().getTopicId());
            ps.setString(3,question.getType() );
            ps.setString(4, question.getText());
            ps.setInt(5,question.getDifficulty());
            ps.setString(6,question.getHint());
            ps.setString(7,question.getDetailedSolution());

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
    public List<Question> getQuestionBankForTopic(String topicName, String courseId){
        List<Question> questionList = null;
        PreparedStatement ps =null;
        Connection con = null;

        String query = "SELECT q.QID,q.TOPIC_ID,q.TYPE,q.QTEXT,q.DIFFICULTY,q.HINT,q.DETAILED_SOLUTION FROM QUESTIONS q " +
                "where q.TOPIC_ID IN (SELECT DISTINCT (TOPIC_ID) FROM TOPIC t WHERE t.C_ID = ?";
        if(!topicName.equalsIgnoreCase("ALL"))
            query = query+" AND t.TOPIC_NAME = ?";
        query = query+")";
        ResultSet rst = null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, courseId);
            if(!topicName.equalsIgnoreCase("ALL"))
                ps.setString(2,topicName);
            rst = ps.executeQuery();
            if (rst != null) {
                questionList = new LinkedList<Question>();
                while(rst.next()) {
                    Question question = new Question();
                    Topic topic = new Topic();
                    topic.setTopicName(topicName);
                    topic.setCourseId(courseId);
                    topic.setTopicId(rst.getInt("TOPIC_ID"));
                    question.setTopic(topic);
                    question.setqId(rst.getInt("QID"));
                    question.setType(rst.getString("TYPE"));
                    question.setText(rst.getString("QTEXT"));
                    question.setqDifficulty(rst.getInt("DIFFICULTY"));
                    question.setqHint(rst.getString("HINT"));
                    question.setDetailedSolution(rst.getString("DETAILED_SOLUTION"));

                    questionList.add(question);
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
        return questionList;
    }

    public List<Question> getAllQuestionsForATopic(String topicName,String userName){

        List<Question> questionList = null;
        PreparedStatement ps =null;
        Connection con =null;
        String query="";
        if(topicName.equalsIgnoreCase("ALL")) {
            query = "SELECT q.QID,q.TOPIC_ID,q.TYPE,q.QTEXT,q.DIFFICULTY,q.HINT,q.DETAILED_SOLUTION FROM QUESTIONS q " +
                    "where q.TOPIC_ID IN (SELECT TOPIC_ID FROM TOPIC t WHERE c_id in (select c_id from teaches where u_username=?))";
        }else {
            query = "SELECT q.QID,q.TOPIC_ID,q.TYPE,q.QTEXT,q.DIFFICULTY,q.HINT,q.DETAILED_SOLUTION FROM QUESTIONS q " +
                    "where q.TOPIC_ID IN (SELECT DISTINCT (TOPIC_ID) FROM TOPIC t WHERE t.TOPIC_NAME=?)";
        }
        ResultSet rst = null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            if(!topicName.equalsIgnoreCase("ALL"))
                ps.setString(1,topicName);
            else
                ps.setString(1,userName);
            rst = ps.executeQuery();
            if (rst != null) {
                questionList = new LinkedList<Question>();
                while(rst.next()) {
                    Question question = new Question();
                    Topic topic = new Topic();
                    topic.setTopicName(topicName);
                    topic.setTopicId(rst.getInt("TOPIC_ID"));
                    question.setTopic(topic);
                    question.setqId(rst.getInt("QID"));
                    question.setType(rst.getString("TYPE"));
                    question.setText(rst.getString("QTEXT"));
                    question.setqDifficulty(rst.getInt("DIFFICULTY"));
                    question.setqHint(rst.getString("HINT"));
                    question.setDetailedSolution(rst.getString("DETAILED_SOLUTION"));

                    questionList.add(question);
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
        return questionList;
    }

    public List<Question> getAllQuestions(String userName){
        List<Question> questionList = null;
        PreparedStatement ps =null;
        Connection con =null;
        String topicName="ALL";
        String query = "SELECT q.QID,q.TOPIC_ID,q.TYPE,q.QTEXT,q.DIFFICULTY,q.HINT,q.DETAILED_SOLUTION FROM QUESTIONS q ";
        ResultSet rst = null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            rst = ps.executeQuery();
            if (rst != null) {
                questionList = new LinkedList<Question>();
                while(rst.next()) {
                    Question question = new Question();
                    Topic topic = new Topic();
                    topic.setTopicName(topicName);
                    topic.setCourseId("");
                    topic.setTopicId(rst.getInt("TOPIC_ID"));
                    question.setTopic(topic);
                    question.setqId(rst.getInt("QID"));
                    question.setType(rst.getString("TYPE"));
                    question.setText(rst.getString("QTEXT"));
                    question.setqDifficulty(rst.getInt("DIFFICULTY"));
                    question.setqHint(rst.getString("HINT"));
                    question.setDetailedSolution(rst.getString("DETAILED_SOLUTION"));

                    questionList.add(question);
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
        return questionList;
    }

    public Question getQuestionDetails(Integer qId){

        String query = " SELECT * FROM QUESTIONS q , TOPIC t " +
                "WHERE (q.TOPIC_ID = t.TOPIC_ID) AND q.QID = ?  ";
        Connection con = null;
        PreparedStatement ps =null;
        Question question = null;
        ResultSet rst = null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            ps.setInt(1,  qId);
            rst = ps.executeQuery();
            if (rst != null) {
                if (rst.next()) {
                    question = new Question();
                    Topic topic = new Topic();
                    topic.setTopicId(rst.getInt("TOPIC_ID"));
                    topic.setTopicName(rst.getString("TOPIC_NAME"));
                    topic.setCourseId(rst.getString("C_ID"));
                    question.setTopic(topic);

                    question.setqId(rst.getInt("QID"));
                    question.setType(rst.getString("TYPE"));
                    question.setText(rst.getString("QTEXT"));
                    question.setqDifficulty(rst.getInt("DIFFICULTY"));
                    question.setqHint(rst.getString("HINT"));
                    question.setDetailedSolution(rst.getString("DETAILED_SOLUTION"));

                } else
                    loggers.error("Resultset is empty...");
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
                if (rst != null) {
                    ps.close();
                }


            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return question;
    }

    public Integer getLatestQuestionId(){
        Integer latestQuestionId = 0;
        String query = "SELECT MAX(q.QID) AS Q_ID FROM QUESTIONS q";
        Connection con = null;
        PreparedStatement ps=null;
        ResultSet rst = null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            rst = ps.executeQuery();
            if (rst != null) {
                if (rst.next()) {
                    latestQuestionId = rst.getInt("Q_ID");
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
                if (ps != null) {
                    ps.close();
                }
                if (rst != null) {
                    con.close();
                }

            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return latestQuestionId;
    }

    public List<Integer> getAvailableQuestionLevels(){
        List<Integer> difficultylevels=new ArrayList<Integer>();

        String query = "SELECT DISTINCT (q.DIFFICULTY) FROM QUESTIONS q";
        Connection con = null;
        ResultSet rst = null;
        Statement s=null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            s = con.createStatement();
            rst = s.executeQuery(query);
            int i=0;
            if (rst != null) {
                while (rst.next()) {
                    difficultylevels.add(rst.getInt("DIFFICULTY"));
                }
            } else
                loggers.error("No questions Available in DB");

        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (s != null) {
                    s.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return difficultylevels;
    }

    public List<Parameter> getParametersForQuestionFromParameterSet(int submissionId){
        if(!CommonUtil.isValidInteger(submissionId))
            return null;
        List<Parameter> parameterList = null;
        String query = "SELECT * FROM PARAMETERS p WHERE PS_ID = ?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,submissionId);
            rst = ps.executeQuery();
            if (rst != null) {
                parameterList = new LinkedList<Parameter>();
                while(rst.next()) {
                    Parameter parameter = new Parameter();
                    parameter.setParamSetId(rst.getInt("PS_ID"));
                    parameter.setParamId(rst.getInt("P_ID"));
                    parameter.setParamValue(rst.getString("P_VALUE"));

                    parameterList.add(parameter);
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
        return parameterList;

    }

    public List<Question> getQuestionForDifficultyLevel(Integer difficultyLevel,List<Integer> qIdList){
        List<Question> questionList = null;
        String query;
        if(qIdList.size()==0) {
            query = "SELECT * FROM QUESTIONS q WHERE q.DIFFICULTY = ?";
        }
        else {
            query = "SELECT * FROM QUESTIONS q WHERE q.DIFFICULTY = ? and qid not in(";
            for(Integer x:qIdList){
                query=query+x+",";
            }
            query=query.substring(0,query.length()-1);
            query=query+")";
        }
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query+" ,difficultylevel : "+difficultyLevel);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, difficultyLevel);

            rst = ps.executeQuery();
            if (rst != null) {
                questionList = new LinkedList<Question>();
                while(rst.next()) {
                    Question question = new Question();

                    question.setqId(rst.getInt("QID"));
                    question.setType(rst.getString("TYPE"));
                    question.setText(rst.getString("QTEXT"));
                    question.setqDifficulty(rst.getInt("DIFFICULTY"));
                    question.setqHint(rst.getString("HINT"));
                    question.setDetailedSolution(rst.getString("DETAILED_SOLUTION"));

                    questionList.add(question);
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
        return questionList;
    }

}
