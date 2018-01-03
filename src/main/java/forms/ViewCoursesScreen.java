package forms;

import dao.CourseDAO;
import model.Course;
import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewCoursesScreen implements ActionListener {
    private JPanel viewcoursesPanel;
    private JTextField courseidTextfield;
    private JButton goButton;
    private JLabel courseidLabel;
    private JPanel courseidPanel;
    private JTextField courseNameTextfield;
    private JTextField endDateTextfield;
    private JTextField startDateTextfield;
    private JLabel courseNameLabel;
    private JLabel startDateLabel;
    private JLabel endDateLabel;
    private JPanel labelPanel;
    private JPanel textfieldPanel;
    private JPanel buttonPanel;
    private JButton viewAddExerciseButton;
    private JButton viewAddTAButton;
    private JButton enrollDropStudentButton;
    private JButton viewReportButton;
    private JButton backButton;
    private JPanel p1;
    private JPanel p2;
    private JButton addTopicsButton;
    private User user;
    private String courseID;
    CourseDAO cdao;
    int exId;

    public ViewCoursesScreen(final User user) {

        this.user = user;

            FirstScreen.firstFrame.getContentPane().removeAll();
            FirstScreen.firstFrame.setSize(700, 450);
            FirstScreen.firstFrame.setContentPane(viewcoursesPanel);
            FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            FirstScreen.firstFrame.setVisible(true);

            courseNameTextfield.setEditable(false);
            startDateTextfield.setEditable(false);
            endDateTextfield.setEditable(false);

            if(user.getRole().equalsIgnoreCase("TA")){
            viewAddTAButton.setEnabled(false);
            addTopicsButton.setEnabled(false);
        }

            viewAddExerciseButton.setEnabled(false);
            viewAddTAButton.setEnabled(false);
            enrollDropStudentButton.setEnabled(false);
            viewReportButton.setEnabled(false);
            addTopicsButton.setEnabled(false);

            cdao= new CourseDAO();




        goButton.addActionListener(this);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewAddCoursesScreen vcs=new ViewAddCoursesScreen(user);
            }
        });
        enrollDropStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EnrollDropStudentScreen edss=new EnrollDropStudentScreen(user,courseID);
            }
        });
        viewAddTAButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewAddTAScreen vta=new ViewAddTAScreen(user,courseID);
            }
        });
        viewAddExerciseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewAddExerciseScreen vaes= new ViewAddExerciseScreen(courseID, user);
            }
        });
        viewReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewReportScreen viewReportScreen=new ViewReportScreen(user, courseID);
            }
        });
        addTopicsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddTopicScreen addTopicScreen = new AddTopicScreen(user, courseID);
            }
        });
    }

    public ViewCoursesScreen(final User user, final String courseID) {

        this.user = user;
        this.courseID=courseID;

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(700, 450);
        FirstScreen.firstFrame.setContentPane(viewcoursesPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);

        courseNameTextfield.setEditable(false);
        startDateTextfield.setEditable(false);
        endDateTextfield.setEditable(false);

        cdao= new CourseDAO();
        Course course=null;
        if(user.getRole().equalsIgnoreCase("TA")){
            {
                viewAddTAButton.setEnabled(false);
                addTopicsButton.setEnabled(false);
            }
            course = cdao.getTACourseDetails(courseID,user.getUserName());
        }else if(user.getRole().equalsIgnoreCase("PROF")) {
            course = cdao.getCourseDetails(courseID, user.getUserName());
        }


        courseidTextfield.setText(courseID);
        goButton.addActionListener(this);



        courseNameTextfield.setText(course.getCourseName());
        startDateTextfield.setText(course.getStartDate() + "");
        endDateTextfield.setText(course.getEndDate() + "");

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewAddCoursesScreen vcs=new ViewAddCoursesScreen(user);
            }
        });
        enrollDropStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EnrollDropStudentScreen edss=new EnrollDropStudentScreen(user,courseID);
            }
        });
        viewAddExerciseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewAddExerciseScreen vaes= new ViewAddExerciseScreen(courseID, user);
            }
        });
        viewAddTAButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewAddTAScreen vta=new ViewAddTAScreen(user,courseID);
            }
        });
        viewReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewReportScreen viewReportScreen=new ViewReportScreen(user, courseID);
            }
        });
        addTopicsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddTopicScreen addTopicScreen = new AddTopicScreen(user, courseID);
            }
        });
        }

    public void actionPerformed(ActionEvent e) {
        if(courseidTextfield.getText().equals("")) {

            courseNameTextfield.setText("");
            startDateTextfield.setText("");
            endDateTextfield.setText("");
            viewAddExerciseButton.setEnabled(false);
            viewAddTAButton.setEnabled(false);
            enrollDropStudentButton.setEnabled(false);
            viewReportButton.setEnabled(false);
            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please enter valid CourseId");
        }
        else {
            courseID=courseidTextfield.getText().toUpperCase();
            Course course=null;
            if(user.getRole().equalsIgnoreCase("PROF")) {
                course = cdao.getCourseDetails(courseID, user.getUserName());
            }
            else if (user.getRole().equalsIgnoreCase("TA")){
                course= cdao.getTACourseDetails(courseID, user.getUserName());
            }
            if(course==null) {

                courseNameTextfield.setText("");
                startDateTextfield.setText("");
                endDateTextfield.setText("");
                viewAddExerciseButton.setEnabled(false);
                viewAddTAButton.setEnabled(false);
                enrollDropStudentButton.setEnabled(false);
                viewReportButton.setEnabled(false);
                JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please enter valid CourseId");
            }
            else {
                if(user.getRole().equalsIgnoreCase("PROF")) {
                    courseNameTextfield.setText(course.getCourseName());
                    startDateTextfield.setText(course.getStartDate() + "");
                    endDateTextfield.setText(course.getEndDate() + "");

                    viewAddExerciseButton.setEnabled(true);
                    viewAddTAButton.setEnabled(true);
                    enrollDropStudentButton.setEnabled(true);
                    viewReportButton.setEnabled(true);
                    addTopicsButton.setEnabled(true);
                }
                else if (user.getRole().equalsIgnoreCase("TA")){
                    courseNameTextfield.setText(course.getCourseName());
                    startDateTextfield.setText(course.getStartDate() + "");
                    endDateTextfield.setText(course.getEndDate() + "");

                    viewAddExerciseButton.setEnabled(true);
                    viewAddTAButton.setEnabled(false);
                    enrollDropStudentButton.setEnabled(true);
                    viewReportButton.setEnabled(true);
                }

            }
        }
    }
}
