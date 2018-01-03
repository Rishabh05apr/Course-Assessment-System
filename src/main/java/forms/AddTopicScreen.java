package forms;

import dao.CourseDAO;
import dao.QuestionDAO;
import dao.TopicDAO;
import model.Course;
import model.Question;
import model.Topic;
import util.CommonUtil;
import model.User;
import model.Topic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

public class AddTopicScreen /*implements ActionListener*/ {
    private JPanel addTopicScreenPanel;
    private JTextField topicIdTextField;
    private JTextField topicNameTextField;
    private JButton addButton;
    private JButton resetButton;
    private JButton backButton;
    private JPanel buttonPanel;
    private JPanel textFieldPanel;
    private JLabel topicNameLabel;
    private JLabel topicIdLabel;
    private JPanel labelPanel;
    private JPanel topicDetailsPanel;

    private User user;
    private CourseDAO courseDAO;
    private TopicDAO topicDAO;
    private QuestionDAO questionDAO;
    private String courseId;

    public AddTopicScreen(final User user, final String courseId) {

        this.user = user;
        this.courseId = courseId;

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(550, 450);
        FirstScreen.firstFrame.setContentPane(addTopicScreenPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);
        ButtonGroup bg = new ButtonGroup();

        courseDAO = new CourseDAO();
        topicDAO = new TopicDAO();
        questionDAO = new QuestionDAO();

        topicIdTextField.setEditable(false);
        topicIdTextField.setText((topicDAO.getLatestTopicId() + 1) + "");


        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topicNameTextField.setText("");
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Topic topic = new Topic();
                boolean flag = true;
                if (topicNameTextField.getText().equals("") || topicNameTextField.getText().equals(""))
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Valid Topic Name");
                else {
                    try {

                        topic.setTopicId(Integer.parseInt(topicIdTextField.getText()));
                        topic.setTopicName(topicNameTextField.getText());
                        topic.setCourseId(courseId);
                    } catch (Exception e1) {
                        flag = false;
                    }
                    if (flag) {
                        if (topicDAO.saveTopic(topic)) {
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Topic Successfully Added");
                            AddTopicScreen addTopicScreen = new AddTopicScreen(user, courseId);
                        } else
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Error!Please Try Again");
                    }

                }
            }


        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewCoursesScreen viewCoursesScreen = new ViewCoursesScreen(user,courseId);
            }
        });
    }
}
