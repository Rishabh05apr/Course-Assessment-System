package forms;

import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentHomeworkScreen {
    private JButton currentHomeworkButton;
    private JButton pastHomeworkButton;
    private JPanel studentHomeworkPanel;
    private JButton backButton;
    private User user;

    public StudentHomeworkScreen(final User user, final String courseID) {
        this.user=user;

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(400, 450);
        FirstScreen.firstFrame.setContentPane(studentHomeworkPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);

        currentHomeworkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CurrentHomeworkScreen ch=new CurrentHomeworkScreen(user,courseID);
            }
        });
        pastHomeworkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PastHomeworkScreen ph=new PastHomeworkScreen(user,courseID);
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StudentCoursesScreen sc=new StudentCoursesScreen(user);
            }
        });
    }
}
