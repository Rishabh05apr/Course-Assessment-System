package forms;

import dao.CourseDAO;
import model.Course;
import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class AddCoursesScreen {
    private JTextField courseidTextfield;
    private JTextField endDateTextfield;
    private JTextField courseNameTextfield;
    private JTextField startDateTextfield;
    private JLabel courseidLabel;
    private JLabel courseNameLabel;
    private JLabel startDateLabel;
    private JLabel endDateLabel;
    private JButton addButton;
    private JButton resetButton;
    private JPanel labelPanel;
    private JPanel textfieldPanel;
    private JPanel buttonPanel;
    private JPanel addCoursesPanel;
    private JButton backButton;
    private User user;
    CourseDAO courseDAO;

    public AddCoursesScreen(final User user) {

        this.user = user;

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(400, 450);
        FirstScreen.firstFrame.setContentPane(addCoursesPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);

        final Course course=new Course();
        courseDAO =new CourseDAO();

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                courseidTextfield.setText("");
                endDateTextfield.setText("");
                courseNameTextfield.setText("");
                startDateTextfield.setText("");
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewAddCoursesScreen vs=new ViewAddCoursesScreen(user);
            }
        });
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                boolean flag =true;
                if (courseidTextfield.getText().equals("") || courseNameTextfield.getText().equals(""))
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Valid Course Id and Course Name");
                else {
                    course.setCourseID(courseidTextfield.getText().toUpperCase() + "");
                    course.setCourseName(courseNameTextfield.getText() + "");
                    try {
                        course.setStartDate(Timestamp.valueOf(startDateTextfield.getText() + " 00:00:00"));
                        course.setEndDate(Timestamp.valueOf(endDateTextfield.getText() + " 00:00:00"));
                    } catch (Exception x) {
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Date in the Required Format");
                        flag=false;
                    }

                    if(flag) {
                        if (courseDAO.saveCourseDetails(course,user)) {
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Course Successfully Added");
                            ViewAddCoursesScreen vcs = new ViewAddCoursesScreen(user);
                        } else
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Error!Please Try Again");
                    }

                }
            }
        });
    }

}
