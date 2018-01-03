package forms;

import dao.CourseDAO;
import model.User;
import util.CommonUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnrollDropStudentScreen {
    private JTextField studentIDTextfield;
    private JRadioButton enrollRadioButton;
    private JButton enrollButton;
    private JButton dropButton;
    private JButton backButton;
    private JLabel studentIDLabel;
    private JPanel labelPanel;
    private JPanel textfieldPanel;
    private JPanel buttonPanel;
    private JPanel radioPanel;
    private JRadioButton dropRadioButton;
    private JPanel enrollDropPanel;
    private User user;
    private String courseID;
    private CourseDAO courseDAO;

    public EnrollDropStudentScreen(final User user, final String courseID) {

        this.user = user;
        this.courseID = courseID;

        courseDAO=new CourseDAO();
        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(400, 450);
        FirstScreen.firstFrame.setContentPane(enrollDropPanel);
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
                ViewCoursesScreen vcs=new ViewCoursesScreen(user,courseID);
            }
        });
        enrollButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!CommonUtil.isValidString(studentIDTextfield.getText())){
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Valid Student ID");
                }
                else{
                    try {
                                if(courseDAO.enrollInCourse(Integer.parseInt(studentIDTextfield.getText()),courseID)){
                                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Successfully enrolled");
                                }
                                else
                                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Could not enroll the student");

                    }  catch (Exception e1) {
                       // e1.printStackTrace();
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Could not enroll the student");

                    }
                }
            }
        });
        dropButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!CommonUtil.isValidString(studentIDTextfield.getText())){
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please enter valid Student ID");
                }
                else{
                    try {
                        if(courseDAO.deleteFromCourse(Integer.parseInt(studentIDTextfield.getText()),courseID)){
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Successfully dropped");
                        }
                        else
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Could not drop the student");

                    }  catch (Exception e1) {
                        // e1.printStackTrace();
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Could not drop the student");

                    }
                }

            }
        });
    }
}
