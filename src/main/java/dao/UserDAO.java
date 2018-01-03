package dao;

import exception.ApplicationException;
import model.User;
import util.CommonUtil;
import util.JdbcDataUtil;
import util.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pinak on 10/17/2017.
 */
public class UserDAO {

    private static Logger loggers = Logger.getLogger(UserDAO.class);
    public Map<String, String> authenticateUser(String userName, String password) throws ApplicationException{
        if(userName == null || userName.trim().equals("") || password == null || password.trim().equals(""))
            return null;

        Map<String, String> userMap = null;
        String query = "SELECT U_USERNAME, U_ROLE FROM USER_DETAILS WHERE U_USERNAME=? AND U_PASSWORD=?";

        String errorMessage = "";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : "+query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1,userName);
            ps.setString(2,password);
            rst = ps.executeQuery();
            if(rst!=null){
                if(rst.next()){
                    userMap = new HashMap<String, String>();
                    userMap.put("userName", rst.getString("U_USERNAME"));
                    userMap.put("role", rst.getString("U_ROLE"));
                }
                else
                    loggers.error("Resultset is empty...");
            }
            else
                loggers.error("Resultset is null...");
        } catch (Exception e) {
            //e.printStackTrace();
            errorMessage = "Error while executing query";
            loggers.error(errorMessage,e);
            throw new ApplicationException(errorMessage,e);
        } finally {
            try {
                if(rst != null){
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
                errorMessage = "Error while executing query";
                loggers.error(errorMessage,e);
                throw new ApplicationException(errorMessage,e);
            }
        }
        return userMap;
    }

    public User getUserDetails(String userName){
        if(userName == null || userName.trim().equals(""))
            return null;

        User user = null;
        String query = "SELECT U_ID, U_FIRST_NAME, U_LAST_NAME, U_EMAIL, U_USERNAME, U_ROLE FROM USER_DETAILS WHERE U_USERNAME=?";

        String errorMessage = "";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : "+query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1,userName);
            rst = ps.executeQuery();
            if(rst!=null){
                if(rst.next()){
                    user = new User();
                    user.setId(rst.getInt("U_ID"));
                    user.setFirstName(rst.getString("U_FIRST_NAME"));
                    user.setLastName(rst.getString("U_LAST_NAME"));
                    user.setEmail(rst.getString("U_EMAIL"));
                    user.setUserName(rst.getString("U_USERNAME"));
                    user.setRole(rst.getString("U_ROLE"));
                }
                else
                    loggers.error("Resultset is empty...");
            }
            else
                loggers.error("Resultset is null...");
        } catch (Exception e) {
            //e.printStackTrace();
            errorMessage = "Error while executing query";
            loggers.error(errorMessage,e);
        } finally {
            try {
                if(rst != null){
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
                errorMessage = "Error while executing query";
                loggers.error(errorMessage,e);
            }
        }
        return user;
    }

    public boolean updateUserDetails(User user){
        if(user == null || user.getUserName() == null)
            return false;

        String query = "UPDATE USER_DETAILS SET U_FIRST_NAME = ?, U_LAST_NAME = ?, U_EMAIL = ? WHERE U_USERNAME=?";

        String errorMessage = "";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("executing query : "+query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1,user.getFirstName());
            ps.setString(2,user.getLastName());
            ps.setString(3,user.getEmail());
            ps.setString(4,user.getUserName());


            int result = ps.executeUpdate();
            if(result==1)
                return true;

        } catch (Exception e) {
            //e.printStackTrace();
            errorMessage = "Error while executing query";
            loggers.error(errorMessage,e);
            return false;
        } finally {
            try {
                if(rst != null){
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
                errorMessage = "Error while executing query";
                loggers.error(errorMessage,e);
                return false;
            }
        }
        return false;
    }

    public Map<String, String> getProfileDetails(String userName)throws ApplicationException{
        if(userName == null || userName.trim().equals(""))
            return null;

        String errorMessage = "";
        Map<String,String> userProfileMap = null;
        String query = "SELECT u.U_FIRST_NAME, u.U_LAST_NAME, u.U_USERNAME, u.U_EMAIL FROM USER_DETAILS u where u.U_USERNAME = ?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : "+query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1,userName);
            rst = ps.executeQuery();
            if(rst!=null){
                if(rst.next()){
                    userProfileMap = new HashMap<String, String>();
                    userProfileMap.put("firstName", rst.getString("U_FIRST_NAME"));
                    userProfileMap.put("lastName", rst.getString("U_LAST_NAME"));
                    userProfileMap.put("userName", rst.getString("U_USERNAME"));
                    userProfileMap.put("email", rst.getString("U_EMAIL"));
                }
                else{
                    errorMessage = "Resultset is empty...";
                    loggers.error(errorMessage);
                    return null;
                }
            }
            else
                loggers.error("Resultset is null...");
        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query",e);
        } finally {
            try {
                if(rst != null){
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
                loggers.error("Error while executing query",e);
            }
        }
        return userProfileMap;
    }



    public void sampleDBMethod(){
        String query = "SELECT U_USERNAME, U_ROLE AS USER_COUNT FROM USER_DETAILS WHERE U_USERNAME=? AND U_PASSWORD=?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : "+query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            rst = ps.executeQuery();
            if(rst!=null){
                if(rst.next()){

                }
                else
                    loggers.error("Resultset is empty...");
            }
            else
                loggers.error("Resultset is null...");
        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query",e);
        } finally {
            try {
                if(rst != null){
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
                loggers.error("Error while executing query",e);
            }
        }
    }

    public static boolean isTA(String userName){
        String query = "SELECT COUNT(STUD_ID) AS TA_COUNT FROM TA_FOR WHERE STUD_ID=?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : "+query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1,userName);
            rst = ps.executeQuery();
            if(rst!=null){
                if(rst.next()){
                    if(rst.getInt("TA_COUNT")>0)
                        return true;
                }
                else {
                    loggers.error("Resultset is empty...");
                    return false;
                }
            }
            else {
                loggers.error("Resultset is null...");
                return false;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query",e);
        } finally {
            try {
                if(rst != null){
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
                loggers.error("Error while executing query",e);
            }
        }
        return false;
    }

    public boolean isTAForCourse(String userName, String courseID){
        if(!CommonUtil.isValidString(userName) || !CommonUtil.isValidString(courseID))
            return false;
        String query = "SELECT COUNT(STUD_ID) AS TA_COUNT FROM TA_FOR WHERE STUD_ID=? AND C_ID = ?";
        ResultSet rst = null;
        PreparedStatement ps =null;
        Connection con =null;
        try {
            loggers.debug("Getting result for the query : "+query);
            con = JdbcDataUtil.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1,userName);
            ps.setString(2,courseID);
            rst = ps.executeQuery();
            if(rst!=null){
                if(rst.next()){
                    if(rst.getInt("TA_COUNT")>0)
                        return true;
                }
                else {
                    loggers.error("Resultset is empty...");
                    return false;
                }
            }
            else {
                loggers.error("Resultset is null...");
                return false;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            loggers.error("Error while executing query",e);
        } finally {
            try {
                if(rst != null){
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
                loggers.error("Error while executing query",e);
            }
        }
        return false;
    }

    public boolean addTA(String courseId, String userName) throws SQLException{
        if(!CommonUtil.isValidString(userName) || !CommonUtil.isValidString(courseId))
            return false;
        String query = "INSERT INTO TA_FOR (STUD_ID, C_ID) VALUES (?,?)";
        ResultSet rst = null;
        Connection con = null;
        PreparedStatement ps =null;
        loggers.debug("Executing query : "+query);
        try {
            con = JdbcDataUtil.getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            ps.setString(2, courseId);
            ps.setString(1, userName);
            if (ps.executeUpdate() == 1) {
                con.commit();
                return true;
            } else {
                loggers.error("Data could not be saved...");
                loggers.warn("Rolling back transaction....");
                con.rollback();
            }
        }
        catch(Exception e){
            loggers.error("Error while executing query",e);
        }finally {
            try {
                if(rst != null){
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
                loggers.error("Error while executing query",e);
            }
        }
        return false;
    }
}
