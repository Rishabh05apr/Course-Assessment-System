package dao;

import model.Answer;
import util.CommonUtil;
import util.JdbcDataUtil;
import util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class AnswerDAO {
    private static Logger loggers = Logger.getLogger(AnswerDAO.class);

    public Integer getLatestAnswerId(){
        Integer latestAnsId = 0;
        String query = "SELECT MAX(a.ANSID) AS ANSID FROM ANSWERS a";
        Connection con = null;
        ResultSet rst = null;
        PreparedStatement ps=null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            rst = ps.executeQuery();
            if (rst != null) {
                if (rst.next()) {
                    latestAnsId = rst.getInt("ANSID");
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
        return latestAnsId;
    }
    public boolean saveAnswer(Answer answer) {
        PreparedStatement ps = null;
        Connection con = null;
        String query = "INSERT INTO ANSWERS (ANSID, ANS_CATEGORY, SHORT_EXPLAINATION, QID, ANS) VALUES (?,?,?,?,?)";
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            ps.setInt(1, answer.getAnsId());
            ps.setString(2, answer.getCategory());
            ps.setString(3, answer.getShortExplaination());
            ps.setInt(4, answer.getqId());
            ps.setString(5, answer.getAns());

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
    public List<Answer> getAnswerListForQuestion(Integer questionId, String ansCategory){
        if(!CommonUtil.isValidInteger(questionId)||!CommonUtil.isValidString(ansCategory))
            return null;
        List<Answer> answerList = null;
        String query = "SELECT * FROM ANSWERS a WHERE a.QID = ? AND a.ANS_CATEGORY = ?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query+" ,QID : "+questionId+" ,ANS_CATEGORY : "+ansCategory);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,questionId);
            ps.setString(2,ansCategory);
            rst = ps.executeQuery();
            if (rst != null) {
                answerList = new LinkedList<Answer>();
                while (rst.next()) {
                    Answer answer = new Answer();
                    answer.setAnsId(rst.getInt("ANSID"));
                    answer.setCategory(rst.getString("ANS_CATEGORY"));
                    answer.setAns(rst.getString("ANS"));
                    answer.setqId(questionId);
                    answer.setShortExplaination(rst.getString("SHORT_EXPLAINATION"));
                    answerList.add(answer);
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
        return answerList;
    }

    public List<Answer> getAnswerListForQuestionWithParameter(Integer questionId, String ansCategory, Integer paramSetId){
        if(!CommonUtil.isValidInteger(questionId)||!CommonUtil.isValidString(ansCategory))
            return null;
        List<Answer> answerList = null;
        String query = "SELECT * FROM ANSWERS a WHERE a.ANS_CATEGORY = ?" +
                " AND a.ANSID IN (SELECT DISTINCT (ANSID) FROM ANS_PARAM_MAPPING apm where apm.QID = ? AND apm.PS_ID = ?)";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query+" ,QID : "+questionId+" ,ANS_CATEGORY : "+ansCategory);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);

            ps.setString(1,ansCategory);
            ps.setInt(2,questionId);
            ps.setInt(3,paramSetId);
            rst = ps.executeQuery();
            if (rst != null) {
                answerList = new LinkedList<Answer>();
                while (rst.next()) {
                    Answer answer = new Answer();
                    answer.setAnsId(rst.getInt("ANSID"));
                    answer.setCategory(rst.getString("ANS_CATEGORY"));
                    answer.setAns(rst.getString("ANS"));
                    answer.setqId(questionId);
                    answer.setShortExplaination(rst.getString("SHORT_EXPLAINATION"));
                    answerList.add(answer);
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
        return answerList;
    }

    public Answer getAnswer(Integer ansId){
        if(!CommonUtil.isValidInteger(ansId))
            return null;
        Answer answer = null;
        String query = "SELECT * FROM ANSWERS a WHERE a.ANSID = ?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,ansId);
            rst = ps.executeQuery();
            if (rst != null) {
                while (rst.next()) {
                    answer = new Answer();
                    answer.setAnsId(rst.getInt("ANSID"));
                    answer.setCategory(rst.getString("ANS_CATEGORY"));
                    answer.setAns(rst.getString("ANS"));
                    answer.setqId(rst.getInt("QID"));
                    answer.setShortExplaination(rst.getString("SHORT_EXPLAINATION"));
                }
            } else
                loggers.error("Resultset is null...");
        } catch (Exception e) {
            e.printStackTrace();
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
                e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return answer;
    }
    public boolean saveAnswer(List<Answer> answerList) {
        PreparedStatement ps = null;
        Connection con = null;
        String query = "INSERT INTO ANSWERS (ANSID, ANS_CATEGORY, SHORT_EXPLAINATION, QID, ANS) VALUES (?,?,?,?,?)";
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            for (Answer answer : answerList) {
                ps.setInt(1, answer.getAnsId());
                ps.setString(2, answer.getCategory());
                ps.setString(3, answer.getShortExplaination());
                ps.setInt(4, answer.getqId());
                ps.setString(5, answer.getAns());
                ps.executeUpdate();
            }
            con.commit();
            return true;
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

}
