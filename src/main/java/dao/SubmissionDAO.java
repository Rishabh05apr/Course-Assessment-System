package dao;

import model.Submission;
import model.SubmissionDetails;
import model.User;
import util.CommonUtil;
import util.JdbcDataUtil;
import util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubmissionDAO {
    private static Logger loggers = Logger.getLogger(SubmissionDAO.class);
    public boolean saveStandardExamAttempt(SubmissionDetails submissionDetails, Integer exerciseId, Integer userId){
        if(submissionDetails==null)
            return false;
        String insSubmissionDetailsProc = "{call INS_SUBMISSION_DETAILS_PROC(?,?,?,?,?,?,?)}";
        CallableStatement callableStatement = null;
        Connection con = null;

        try {
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            callableStatement = con.prepareCall(insSubmissionDetailsProc);
            callableStatement.setInt(1,submissionDetails.getSubmissionId());
            callableStatement.setInt(2,submissionDetails.getQuestionId());
            callableStatement.registerOutParameter(3, Types.INTEGER);
            //callableStatement.setInt(3,submissionDetails.getScore());
            callableStatement.setInt(4,submissionDetails.getAnsId());
            callableStatement.setInt(5,exerciseId);
            callableStatement.setInt(6,userId);
            callableStatement.registerOutParameter(7, Types.VARCHAR);

            int result = callableStatement.executeUpdate();
            if (result == 1) {
                con.commit();
                return true;
            } else {
                con.rollback();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (callableStatement != null) {

                    callableStatement.close();
                }

                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public Integer getPointScoredForAQuestion(int subid,int qid){
        Integer score=null;
        String query = "SELECT * FROM SUBMISSION_DETAILS WHERE SUB_ID = ? and QID=?";
        Connection con = null;
        ResultSet rst = null;
        PreparedStatement ps =null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,subid);
            ps.setInt(2,qid);
            rst = ps.executeQuery();
            if (rst != null) {
                if (rst.next()) {
                    score = rst.getInt("score");
                } else
                    loggers.error("Resultset is empty...");
            } else
                loggers.error("No score is submission table");

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
        return score;
    }

    public List<SubmissionDetails> getAllSubmissionDetailsForAnAttempt(int subid){
        List<SubmissionDetails> submissionList=new ArrayList<SubmissionDetails>();
        String query = "SELECT * FROM SUBMISSION_details WHERE sub_id=?";
        Connection con = null;
        ResultSet rst = null;
        PreparedStatement ps =null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,subid);

            rst = ps.executeQuery();
            if (rst != null) {
                while (rst.next()) {
                    SubmissionDetails sub=new SubmissionDetails();
                    sub.setSubmissionId(subid);
                    sub.setAnsId(rst.getInt("ans_id"));
                    sub.setScore(rst.getInt("score"));
                    sub.setQuestionId(rst.getInt("qid"));
                    submissionList.add(sub);
                }
            } else
                loggers.error("Error");

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
        return submissionList;
    }

    public List<Submission> getAllSubmissionsForAStudent(String courseID, User user){
        List<Submission> submissionList=new ArrayList<Submission>();
        String query = "SELECT * FROM SUBMISSIONS WHERE u_id=? and ex_id in (select DISTINCT (ex_id) from exercise where c_id =?) order by ex_id,attempt_no";
        Connection con = null;
        ResultSet rst = null;
        PreparedStatement ps =null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,user.getId());
            ps.setString(2,courseID);
            rst = ps.executeQuery();
            if (rst != null) {
                while (rst.next()) {
                    Submission sub=new Submission();
                    sub.setAttemptedAt(rst.getTimestamp("attempted_at"));
                    sub.setAttemptNo(rst.getInt("attempt_no"));
                    sub.setExerciseId(rst.getInt("ex_id"));
                    sub.setSubmissionId(rst.getInt("sub_id"));
                    sub.setTotalScore(rst.getInt("score"));
                    submissionList.add(sub);
                }
            } else
                loggers.error("Error");

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
        return submissionList;
    }
    public Integer getTotalScore(int subid){
        Integer tscore=null;
        String query = "SELECT SCORE FROM SUBMISSIONS WHERE SUB_ID = ?";
        Connection con = null;
        ResultSet rst = null;
        PreparedStatement ps =null;
        try {
            loggers.debug("Executing query : " + query+"sub_id: "+subid);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,subid);

            rst = ps.executeQuery();
            if (rst != null) {
                if (rst.next()) {
                    tscore = rst.getInt("score");
                } else
                    loggers.error("Resultset is empty...");
            } else
                loggers.error("No score is submission table");

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
        return tscore;
    }
    public Integer getLatestSubmissionId(){
        Integer latestSubmissionId = 0;
        String query = "SELECT MAX(s.SUB_ID) AS SUB_ID FROM SUBMISSIONS s";
        Connection con = null;
        ResultSet rst = null;
        PreparedStatement ps =null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            rst = ps.executeQuery();
            if (rst != null) {
                if (rst.next()) {
                    latestSubmissionId = rst.getInt("SUB_ID");
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
        return latestSubmissionId;
    }

    public boolean saveAttempt(Submission submission){
        if(submission == null)
            return false;

        PreparedStatement ps = null;
        Connection con = null;
        String query = "INSERT INTO SUBMISSIONS (SUB_ID, U_ID, EX_ID, ATTEMPT_NO, ATTEMPTED_AT) VALUES (?,?,?,?,?)";
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            ps.setInt(1, submission.getSubmissionId());
            ps.setInt(2,submission.getUserId());
            ps.setInt(3,submission.getExerciseId());
            ps.setInt(4, submission.getAttemptNo());
            ps.setTimestamp(5,submission.getAttemptedAt());

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

    public Integer getLatestSubmissionAttempt(Integer exerciseId, Integer studentId){
        Integer latestAttemptNo = 0;
        String query = "SELECT MAX(ATTEMPT_NO) AS ATTEMPT_NO FROM SUBMISSIONS s WHERE s.EX_ID = ? AND s.U_ID = ?";
        Connection con = null;
        ResultSet rst = null;
        PreparedStatement ps =null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,exerciseId);
            ps.setInt(2,studentId);
            rst = ps.executeQuery();
            if (rst != null) {
                if (rst.next()) {
                    latestAttemptNo = rst.getInt("ATTEMPT_NO");
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
        return latestAttemptNo;
    }

    public boolean saveFinalScore(Integer uId, Integer exId){
        if(!CommonUtil.isValidInteger(uId) || !CommonUtil.isValidInteger(exId))
            return false;
        String insScoreSummaryProc = "{call INS_SCORE_SUMMARY_PROC(?,?)}";
        CallableStatement callableStatement = null;
        Connection con = null;

        try {
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            callableStatement = con.prepareCall(insScoreSummaryProc);
            callableStatement.setInt(1,uId);
            callableStatement.setInt(2,exId);
            int result = callableStatement.executeUpdate();
            if (result == 1) {
                con.commit();
                return true;
            } else {
                con.rollback();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (callableStatement != null) {

                    callableStatement.close();
                }

                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Integer getAvgFinalScore(Integer uId, Integer exId, String scoringPolicy){
        Integer score=0;
        if(!CommonUtil.isValidString(scoringPolicy))
            return 0;
        String query = "";
        if(scoringPolicy.equalsIgnoreCase("AVERAGE SCORE"))
            query = "SELECT AVG(SCORE) FROM SUBMISSIONS WHERE EX_ID = ? and U_ID=?";
        else if(scoringPolicy.equalsIgnoreCase("MAXIMUM SCORE"))
            query = "SELECT MAX(SCORE) FROM SUBMISSIONS WHERE EX_ID = ? and U_ID=?";
        else if(scoringPolicy.equalsIgnoreCase("LATEST ATTEMPT"))
            query = "SELECT SCORE FROM SUBMISSIONS WHERE EX_ID = ? and U_ID=? AND ROWNUM<=1 ORDER BY ATTEMPTED_AT DESC";
        Connection con = null;
        ResultSet rst = null;
        PreparedStatement ps =null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,exId);
            ps.setInt(2,uId);
            rst = ps.executeQuery();
            if (rst != null) {
                if (rst.next()) {
                    score = rst.getInt(1);
                } else
                    loggers.error("Resultset is empty...");
            } else
                loggers.error("No score is submission table");

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
        return score;
    }

    public boolean exists(Integer uId, Integer exId){
        Integer score=0;
        String query = "SELECT * FROM SCORE_SUMMARY s WHERE s.EX_ID = ? and s.USER_ID=?";
        Connection con = null;
        ResultSet rst = null;
        PreparedStatement ps =null;
        try {
            loggers.debug("Executing query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,exId);
            ps.setInt(2,uId);
            rst = ps.executeQuery();
            if (rst != null) {
                if (rst.next()) {
                    return true;
                } else
                    return false;
            } else
                return false;

        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query", e);
            return false;
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
    }

    public boolean saveScoreSummary(Integer uId, Integer exId, Integer finalScore, String action){

        PreparedStatement ps = null;
        Connection con = null;
        String query = "";
        if(action.equalsIgnoreCase("UPDATE"))
            query = "UPDATE SCORE_SUMMARY SET SCORE = ? WHERE EX_ID = ? and USER_ID=?";
        else
            query = "INSERT INTO SCORE_SUMMARY(USER_ID,EX_ID,SCORE) VALUES (?,?,?)";
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            ps.setInt(1, uId);
            ps.setInt(2,exId);
            ps.setInt(3,finalScore);

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
}
