package dao;

import model.DetailedReport;
import model.SummarizedReport;
import util.JdbcDataUtil;
import util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ReportDAO {

    private static Logger loggers = Logger.getLogger(CourseDAO.class);

    public List<SummarizedReport> generateReport(String CourseId){
        List<SummarizedReport> summarizedReports = null;
        String query = "SELECT u.U_ID,u.U_FIRST_NAME,u.U_LAST_NAME,ss.EX_ID,ss.SCORE,ex.SCORE_POLICY"+
            " FROM USER_DETAILS u, SCORE_SUMMARY ss,EXERCISE ex"+
            " WHERE u.U_ID=ss.USER_ID AND ss.EX_ID=ex.EX_ID AND ex.C_ID=?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, CourseId);
            rst = ps.executeQuery();

            summarizedReports = new LinkedList<SummarizedReport>();
            while(rst.next()) {
                SummarizedReport summarizedReport =  new SummarizedReport();
                //summarizedReport.setAttemptNo(rst.getInt("ATTEMPT_NO"));
                //summarizedReport.setAttemptedAt(rst.getTimestamp("ATTEMPTED_AT"));
                summarizedReport.setExerciseId(rst.getInt("EX_ID"));
                summarizedReport.setFirstName(rst.getString("U_FIRST_NAME"));
                summarizedReport.setLastName(rst.getString("U_LAST_NAME"));
                summarizedReport.setFinalScore(rst.getInt("SCORE"));
                summarizedReport.setScorePolicy(rst.getString("SCORE_POLICY"));
                summarizedReport.setUserId(rst.getInt("U_ID"));
                summarizedReports.add(summarizedReport);
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
        return summarizedReports;
    }

    public List<DetailedReport> generateReportforStudent(String CourseId, Integer uid){
        List<DetailedReport> detailedReports = null;
        String query = "SELECT u.U_ID,u.U_FIRST_NAME,u.U_LAST_NAME,s.EX_ID,s.ATTEMPT_NO,s.SCORE," +
                "s.ATTEMPTED_AT FROM USER_DETAILS u INNER JOIN SUBMISSIONS s ON s.U_ID = u.U_ID AND" +
                " s.U_ID IN (SELECT e.U_ID FROM ENROLLS_IN e where e.C_ID = ? AND  e.U_ID = ?) ORDER BY s.U_ID,s.EX_ID";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, CourseId);
            ps.setInt(2,uid);
            rst = ps.executeQuery();
            detailedReports = new LinkedList<DetailedReport>();
            while(rst.next()) {
                DetailedReport detailedReport =  new DetailedReport();
                detailedReport.setAttemptNo(rst.getInt("ATTEMPT_NO"));
                detailedReport.setAttemptedAt(rst.getTimestamp("ATTEMPTED_AT"));
                detailedReport.setExerciseId(rst.getInt("EX_ID"));
                detailedReport.setFirstName(rst.getString("U_FIRST_NAME"));
                detailedReport.setLastName(rst.getString("U_LAST_NAME"));
                detailedReport.setScore(rst.getInt("SCORE"));
                detailedReport.setUserId(rst.getInt("U_ID"));
                detailedReports.add(detailedReport);
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
        return detailedReports;
    }
}
