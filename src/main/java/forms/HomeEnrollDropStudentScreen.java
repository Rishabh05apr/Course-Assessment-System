package forms;

import dao.CourseDAO;
import dao.UserDAO;
import model.User;
import util.CommonUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeEnrollDropStudentScreen {
    private JRadioButton enrollRadioButton;
    private JRadioButton dropRadioButton;
    private JTextField studentIdTextField;
    private JButton enrollButton;
    private JButton dropButton;
    private JButton backButton;
    private JTextField courseIdTextField;
    private JLabel studentIdLabel;
    private JLabel courseIdLabel;
    private JPanel buttonPanel;
    private JPanel labelTextFieldPanel;
    private JPanel enrollDropStudentPanel;
    private CourseDAO courseDAO;
    private User user;
    private UserDAO userDAO;

    public HomeEnrollDropStudentScreen(final User user) {

        this.user = user;
        courseDAO=new CourseDAO();
        userDAO=new UserDAO();

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(400, 450);
        FirstScreen.firstFrame.setContentPane(enrollDropStudentPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);

        ButtonGroup bg=new ButtonGroup();
        bg.add(dropRadioButton);
        bg.add(enrollRadioButton);

        enrollRadioButton.setSelected(true);
        dropButton.setEnabled(false);


        enrollRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dropButton.setEnabled(false);
                enrollButton.setEnabled(true);
            }
        });

        dropRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enrollButton.setEnabled(false);
                dropButton.setEnabled(true);
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(user.getRole().equalsIgnoreCase("TA")) {
                TAScreen ta=new TAScreen(user);
                }
                else if(user.getRole().equalsIgnoreCase("PROF")) {
                InstructorScreen is=new InstructorScreen(user);
                }

            }
        });
        enrollButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String studentid=studentIdTextField.getText();
                String courseidtoenroll=courseIdTextField.getText().toUpperCase();
                if(!CommonUtil.isValidString(studentid) || !CommonUtil.isValidString(courseidtoenroll)){
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please enter valid Student ID and CourseID");
                }
                else{
                    try {
                            if(userDAO.isTAForCourse(user.getUserName(),courseidtoenroll) || isprof(user.getUserName(),courseidtoenroll)) {
                                if (courseDAO.enrollInCourse(Integer.parseInt(studentid), courseIdTextField.getText())) {
                                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Successfully enrolled");
                                } else
                                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Could not enroll the student");
                            }
                            else
                                JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Not a prof or TA for the course");
                    }  catch (Exception e1) {

                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Could not enroll the student");

                    }
                }
            }
        });
        dropButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String studentid=studentIdTextField.getText();
                String courseidtodrop=courseIdTextField.getText().toUpperCase();
                if(!CommonUtil.isValidString(studentid) || !CommonUtil.isValidString(courseidtodrop)){
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please enter valid Student ID and CourseID");
                }
                else{
                    try {
                        if(userDAO.isTAForCourse(user.getUserName(),courseidtodrop) || isprof(user.getUserName(),courseidtodrop)) {

                            if (courseDAO.deleteFromCourse(Integer.parseInt(studentid), courseidtodrop)) {
                                JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Successfully dropped");
                            } else
                                JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Could not drop the student");
                        }
                        else
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Not a prof or TA for the course");
                    }  catch (Exception e1) {

                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Could not drop the student");

                    }
                }

            }
        });
    }

    private boolean isprof(String userName, String courseid) {
        String prof=courseDAO.getProfForACourse(courseid);
        if(prof.equalsIgnoreCase(userName))
            return true;
        else
            return false;
    }
}

