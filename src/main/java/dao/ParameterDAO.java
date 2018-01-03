package dao;

import model.Answer;
import model.Parameter;
import model.ParameterSet;
import util.CommonUtil;
import util.JdbcDataUtil;
import util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ParameterDAO {
    private static Logger loggers = Logger.getLogger(ParameterSet.class);
    public Integer getLatestParamSetId(){
        Integer latestParamSetId = 0;
        String query = "SELECT MAX(p.PS_ID) AS PS_ID FROM PARAMETERS p";
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
                    latestParamSetId = rst.getInt("PS_ID");
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
                    rst.close();
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                loggers.error("Error while executing query", e);
            }
        }
        return latestParamSetId;
    }

    public boolean saveParamters(List<Parameter> parameterList){
        PreparedStatement ps = null;
        Connection con = null;
        String query = "INSERT INTO PARAMETERS (PS_ID, QID, P_ID, P_VALUE) VALUES (?,?,?,?)";
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            for (Parameter parameter : parameterList) {
                ps.setInt(1,parameter.getParamSetId());
                ps.setInt(2,parameter.getQuestionId());
                ps.setInt(3,parameter.getParamId());
                ps.setString(4,parameter.getParamValue());
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
    public boolean saveAnsParamMapping(Integer paramSetId, List<Answer> answerList){
        PreparedStatement ps = null;
        Connection con = null;
        String query = "INSERT INTO ANS_PARAM_MAPPING (PS_ID, QID, ANSID) VALUES (?,?,?)";
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            for (Answer answer : answerList) {
                ps.setInt(1,paramSetId);
                ps.setInt(2,answer.getqId());
                ps.setInt(3,answer.getAnsId());
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
    public List<Parameter> getParametersForQuestion(Integer questionId){
        if(!CommonUtil.isValidInteger(questionId))
            return null;
        List<Parameter> parameterList = null;
        String query = "SELECT p.PS_ID, p.P_ID, p.QID, p.P_VALUE FROM PARAMETERS p where p.QID = ?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,questionId);
            rst = ps.executeQuery();
            if (rst != null) {
                parameterList = new LinkedList<Parameter>();
                while (rst.next()) {
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
    public List<Integer> getParameterSetIdsForQuestion(Integer questionId){
        if(!CommonUtil.isValidInteger(questionId))
            return null;
        List<Integer> parameterSetIdList = null;
        String query = "SELECT DISTINCT (p.PS_ID) FROM PARAMETERS p where p.QID = ?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,questionId);
            rst = ps.executeQuery();
            if (rst != null) {
                parameterSetIdList = new LinkedList<Integer>();
                while (rst.next()) {
                    parameterSetIdList.add(rst.getInt("PS_ID"));
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
        return parameterSetIdList;
    }

    public List<Parameter> getParameterList(Integer parameterSetId){
        if(!CommonUtil.isValidInteger(parameterSetId))
            return null;
        List<Parameter> parameterList = null;

        String query = "SELECT p.PS_ID, p.P_ID, p.QID, p.P_VALUE FROM PARAMETERS p where p.PS_ID = ?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : " + query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,parameterSetId);
            rst = ps.executeQuery();
            if (rst != null) {
                parameterList = new LinkedList<Parameter>();
                while (rst.next()) {
                    Parameter parameter = new Parameter();
                    parameter.setParamSetId(parameterSetId);
                    parameter.setParamId(rst.getInt("P_ID"));
                    parameter.setQuestionId(rst.getInt("QID"));
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
