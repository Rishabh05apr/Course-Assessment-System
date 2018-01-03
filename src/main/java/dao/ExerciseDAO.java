package dao;

import model.Exercise;
import model.Parameter;
import model.Question;
import util.CommonUtil;
import util.JdbcDataUtil;
import util.Logger;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pinak on 10/28/2017.
 */
public class ExerciseDAO {
    private static Logger loggers = Logger.getLogger(CourseDAO.class);
    AnswerDAO answerDAO = new AnswerDAO();

    public Integer getLatestExerciseId(){
        PreparedStatement ps =null;
        Integer latestExerciseId = 0;
        String query = "SELECT MAX(e.EX_ID) AS EX_ID FROM EXERCISE e";
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
                    latestExerciseId = rst.getInt("EX_ID");
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
        return latestExerciseId;
    }
    public boolean addExercise(Exercise ex){

        if(ex == null)
            return false;
        PreparedStatement ps = null;
        Connection con = null;
        String query = "INSERT INTO EXERCISE (EX_ID, EX_START_DATE, EX_END_DATE,EX_NAME,EX_ATTEMPTS,EX_NUM_Q,INCORR_PT,CORR_PT,SCORE_POLICY,EX_MODE,C_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            ps.setInt(1, ex.getExId());
            ps.setTimestamp(2, ex.getStartDate());
            ps.setTimestamp(3, ex.getEndDate());
            ps.setString(4, ex.getExName());
            ps.setInt(5,ex.getExAttempts());
            ps.setInt(6,ex.getExNumOfQuestions());
            ps.setInt(7,ex.getPointsForIncorrectAns());
            ps.setInt(8,ex.getPointsForCorrectAns());
            ps.setString(9,ex.getScoringPolicy());
            ps.setString(10,ex.getMode());
            ps.setString(11,ex.getCourse());

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
    public List<Exercise> getExerciseList(String CourseId) {
        if (CourseId == null || CourseId.trim().equals(""))
            return null;
        List<Exercise> exercises = null;
        String query = "SELECT e.EX_ID, e.EX_START_DATE, e.EX_END_DATE, e.EX_NAME , e.EX_ATTEMPTS , e.EX_NUM_Q, e.INCORR_PT, e.CORR_PT, e.SCORE_POLICY, e.EX_MODE  FROM EXERCISE e where e.C_ID = ?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, CourseId);
            rst = ps.executeQuery();
            if (rst != null) {
                exercises = new LinkedList<Exercise>();
                while(rst.next()) {
                    Exercise exercise = new Exercise();
                    exercise.setExId(rst.getInt("EX_ID"));
                    exercise.setStartDate(rst.getTimestamp("EX_START_DATE"));
                    exercise.setEndDate(rst.getTimestamp("EX_END_DATE"));
                    exercise.setExAttempts(rst.getInt("EX_ATTEMPTS"));
                    exercise.setExNumOfQuestions(rst.getInt("EX_NUM_Q"));
                    exercise.setPointsForIncorrectAns(rst.getInt("INCORR_PT"));
                    exercise.setPointsForCorrectAns(rst.getInt("CORR_PT"));
                    exercise.setScoringPolicy(rst.getString("SCORE_POLICY"));
                    exercise.setMode(rst.getString("EX_MODE"));
                    exercise.setExName(rst.getString("EX_NAME"));
                    exercises.add(exercise);
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
        return exercises;
    }
    public List<Exercise> getPastExerciseList(String CourseId) {
        if (CourseId == null || CourseId.trim().equals(""))
            return null;
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        List<Exercise> exercises = null;
        String query = "SELECT * FROM EXERCISE e where e.C_ID = ? AND e.EX_START_DATE < ? AND e.EX_END_DATE <?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, CourseId);
            ps.setTimestamp(2,currentTime);
            ps.setTimestamp(3,currentTime);
            rst = ps.executeQuery();
            if (rst != null) {
                exercises = new LinkedList<Exercise>();
                while(rst.next()) {
                    Exercise exercise = new Exercise();
                    exercise.setExId(rst.getInt("EX_ID"));
                    exercise.setStartDate(rst.getTimestamp("EX_START_DATE"));
                    exercise.setEndDate(rst.getTimestamp("EX_END_DATE"));
                    exercise.setExAttempts(rst.getInt("EX_ATTEMPTS"));
                    exercise.setExNumOfQuestions(rst.getInt("EX_NUM_Q"));
                    exercise.setPointsForIncorrectAns(rst.getInt("INCORR_PT"));
                    exercise.setPointsForCorrectAns(rst.getInt("CORR_PT"));
                    exercise.setScoringPolicy(rst.getString("SCORE_POLICY"));
                    exercise.setMode(rst.getString("EX_MODE"));
                    exercise.setExName(rst.getString("EX_NAME"));
                    exercises.add(exercise);
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
        return exercises;
    }
    public List<Exercise> getCurrentExerciseList(String CourseId) {
        if (CourseId == null || CourseId.trim().equals(""))
            return null;
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        List<Exercise> exercises = null;
        String query = "SELECT * FROM EXERCISE e where e.C_ID = ? AND e.EX_START_DATE < ? AND e.EX_END_DATE > ?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, CourseId);
            ps.setTimestamp(2, currentTime);
            ps.setTimestamp(3, currentTime);
            rst = ps.executeQuery();
            if (rst != null) {
                exercises = new LinkedList<Exercise>();
                while(rst.next()) {
                    Exercise exercise = new Exercise();
                    exercise.setExId(rst.getInt("EX_ID"));
                    exercise.setStartDate(rst.getTimestamp("EX_START_DATE"));
                    exercise.setEndDate(rst.getTimestamp("EX_END_DATE"));
                    exercise.setExAttempts(rst.getInt("EX_ATTEMPTS"));
                    exercise.setExNumOfQuestions(rst.getInt("EX_NUM_Q"));
                    exercise.setPointsForIncorrectAns(rst.getInt("INCORR_PT"));
                    exercise.setPointsForCorrectAns(rst.getInt("CORR_PT"));
                    exercise.setScoringPolicy(rst.getString("SCORE_POLICY"));
                    exercise.setMode(rst.getString("EX_MODE"));
                    exercise.setExName(rst.getString("EX_NAME"));
                    exercises.add(exercise);
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
        return exercises;
    }

    public List<Question> getQuestionsForExercise(Integer exerciseId){
        if(!CommonUtil.isValidInteger(exerciseId))
            return null;
        List<Question> questions = null;
        String query = "SELECT q.TOPIC_ID,q.QID,q.TYPE,q.QTEXT,q.DIFFICULTY,q.HINT,q.DETAILED_SOLUTION, eq.PS_ID FROM QUESTIONS q, EXERCISE_QUESTION eq " +
                " where eq.EXID = ? AND q.QID = eq.QID ";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, exerciseId);
            rst = ps.executeQuery();
            if (rst != null) {
                questions = new LinkedList<Question>();
                while(rst.next()) {
                    Question question = new Question();
                    //question.setTopicId(rst.getString("TOPIC"));
                    question.setType(rst.getString("TYPE"));
                    question.setText(rst.getString("QTEXT"));
                    question.setqId(rst.getInt("QID"));
                    question.setqHint(rst.getString("HINT"));
                    question.setqDifficulty(rst.getInt("DIFFICULTY"));
                    question.setParamSetId(rst.getInt("PS_ID"));
                    questions.add(question);
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
                } if (ps != null) {
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
        return questions;
    }
    public boolean deleteQuestionFromExercise(Integer exid,Integer qid){
        if ( !CommonUtil.isValidInteger(exid))
            return false;
        String query = "  DELETE FROM EXERCISE_QUESTION WHERE  EXID = ? AND QID = ?";
        Connection con = null;
        PreparedStatement ps=null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            ps.setInt(1, exid);
            ps.setInt(2, qid);
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

    public boolean addQuestionToExercise(Integer qid,Integer exid, Integer parameterSetId){

        if (!CommonUtil.isValidInteger(qid) || !CommonUtil.isValidInteger(exid))
            return false;
        String query = "";
        if (parameterSetId!=null)
            query = "INSERT INTO EXERCISE_QUESTION (EXID, QID, PS_ID) VALUES (?,?,?)";
        else
            query = "INSERT INTO EXERCISE_QUESTION (EXID, QID) VALUES (?,?)";
        Connection con = null;
        PreparedStatement ps =null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            ps.setInt(1, exid);
            ps.setInt(2, qid);
            if(parameterSetId!=null)
                ps.setInt(3,parameterSetId);
            //ps.setString(3,mode);
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

    public Exercise getExerciseDetails(Integer exid){
        if(!CommonUtil.isValidInteger(exid))
            return null;
        Exercise exercise = new Exercise();
        String query = "SELECT e.EX_ID, e.EX_START_DATE, e.EX_END_DATE, e.EX_NAME , e.EX_ATTEMPTS , e.EX_NUM_Q, e.INCORR_PT, e.CORR_PT, e.SCORE_POLICY, e.EX_MODE ,e.C_ID FROM EXERCISE e where e.EX_ID = ?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, exid);
            rst = ps.executeQuery();
            if (rst != null) {
                if(rst.next()){
                    exercise.setMode(rst.getString("EX_MODE"));
                    exercise.setScoringPolicy(rst.getString("SCORE_POLICY"));
                    exercise.setPointsForCorrectAns(rst.getInt("CORR_PT"));
                    exercise.setPointsForIncorrectAns(rst.getInt("INCORR_PT"));
                    exercise.setExNumOfQuestions(rst.getInt("EX_NUM_Q"));
                    exercise.setExAttempts(rst.getInt("EX_ATTEMPTS"));
                    exercise.setExName(rst.getString("EX_NAME"));
                    exercise.setExId(rst.getInt("EX_ID"));
                    exercise.setCourse(rst.getString("C_ID"));
                    exercise.setStartDate(rst.getTimestamp("EX_START_DATE"));
                    exercise.setEndDate(rst.getTimestamp("EX_END_DATE"));
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
        return exercise;
    }

    public Integer numberOfExercises(String cid){
        Integer count = 0;
        String query = "SELECT COUNT(e.EX_ID) AS EX_ID FROM EXERCISE e where e.C_ID = ?";
        Connection con = null;
        ResultSet rst = null;
        PreparedStatement ps = null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            ps.setString(1, cid);
            rst = ps.executeQuery();
            if (rst != null) {
                if (rst.next()) {
                    count = rst.getInt("EX_ID");
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
            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return count;
    }
    public List<Parameter> getParametersForQuestionInExam(Integer exerciseId, Integer questionId){
        if(!CommonUtil.isValidInteger(exerciseId) || !CommonUtil.isValidInteger(questionId))
            return null;
        List<Parameter> parameterList = null;
        String query = "SELECT * FROM PARAMETERS p " +
                " WHERE p.PS_ID IN ( SELECT PS_ID FROM EXERCISE_QUESTION eq WHERE eq.EXID = ? AND eq.QID = ? )";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,exerciseId);
            ps.setInt(2,questionId);
            rst = ps.executeQuery();
            if (rst != null) {
                parameterList = new LinkedList<Parameter>();
                while(rst.next()) {
                    Parameter parameter = new Parameter();
                    parameter.setParamSetId(rst.getInt("PS_ID"));
                    parameter.setParamId(rst.getInt("P_ID"));
                    parameter.setQuestionId(questionId);
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
}
