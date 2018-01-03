package app;

import dao.*;
import model.Course;
import model.Question;
import model.SubmissionDetails;
import model.User;
import util.CommonUtil;
import util.JdbcDataUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by pinak on 10/9/2017.
 */
public class MainApp {
    public static void main(String[] args) {
        System.out.println("Hello World");
        UserDAO userDAO = new UserDAO();
        QuestionDAO qdao = new QuestionDAO();
        Question q = new Question();
        CourseDAO courseDAO = new CourseDAO();
        ExerciseDAO exerciseDAO = new ExerciseDAO();
        String userName = "kogan";
        String password = "kogan";
        try {
            //System.out.println(userDAO.authenticateUser(userName, password));
            //System.out.println(userDAO.getProfileDetails(userName));
            //System.out.println(courseDAO.getCourseList("kogan").toString());
            //System.out.println(courseDAO.getCourseDetails("CSC540","kogan"));

            //testSaveCourse(courseDAO);

            //System.out.println(userDAO.getUserDetails("kogan").toString());

            //System.out.println(testUpdateUser(userDAO));

            //System.out.println(userDAO.isTA("aneela"));
            //System.out.println(userDAO.addTA("CSC570","mfisher"));

            //System.out.println(CommonUtil.formatOracleErrorMessage("ORA-20000: TA should be a GRAD student."));
           /* q.setqDifficulty(1);
            q.setqHint("hsjdk");
            q.setqId(4);
            q.setText("How?");
            q.setType("STAND");
            q.setTopicId("DATABASE");
            System.out.println(qdao.saveQuestion(q)); */
            //System.out.println(qdao.getQuestionBankForTopic("DATABASE"));
            //System.out.println(qdao.getQuestionDetails(1));
            //System.out.println(qdao.getQuestionDetails(1).toString());
            //System.out.println(testInsertSubmissionDetails(new SubmissionDAO()));

            System.out.println(exerciseDAO.getCurrentExerciseList("CSC540"));

            /*for (int i=0;i<10;i++) {
                System.out.println(CommonUtil.getRandomNumberInRange(0,30));
            }*/
        } catch (Exception e) {
            System.out.println("EXception Message: "+e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean testInsertSubmissionDetails(SubmissionDAO submissionDAO){
        SubmissionDetails submissionDetails = new SubmissionDetails();
        submissionDetails.setSubmissionId(2);
        submissionDetails.setQuestionId(2);
        submissionDetails.setAnsId(49);
        Integer exerciseId = 9;
        return  submissionDAO.saveStandardExamAttempt(submissionDetails,exerciseId,123);
    }
    private static boolean testUpdateUser(UserDAO userDAO){
        User user = userDAO.getUserDetails("ABC");
        user.setEmail("psabhyan@ncsu.edu");

        return  userDAO.updateUserDetails(user);
    }
    private static void testSaveCourse(CourseDAO courseDAO) {
        Course course = new Course();
        course.setCourseID("CSC580");
        course.setCourseName("Advanced Computer Networks");
        course.setStartDate(new Timestamp(new java.util.Date().getTime()));
        course.setEndDate(new Timestamp(new java.util.Date().getTime()));
        //courseDAO.saveCourseDetails(course,"");
    }

    private static boolean testInsert(String name){
        Connection con = JdbcDataUtil.getConnection();
        String query = "INSERT INTO EMPLOYEE (NAME) VALUES(?)";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,name);
            int result = ps.executeUpdate();
            if(result == 0)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
