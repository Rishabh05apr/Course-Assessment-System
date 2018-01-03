package forms;

import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InstructorScreen extends Role {
    private JButton viewProfileButton;
    private JButton viewAddCoursesButton;
    private JButton enrollDropAButton;
    private JButton searchAddQuestionsToButton;
    private JButton logoutButton;
    private JLabel instructorhomeLabel;
    private JPanel instructorPanel;
    private JPanel instructorhomePanel;
    private User user;

    public InstructorScreen(final User user) {

        this.user =user;

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(400, 450);
        FirstScreen.firstFrame.setContentPane(instructorPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginScreen ls=new LoginScreen();
            }
        });
        viewAddCoursesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewAddCoursesScreen vs=new ViewAddCoursesScreen(user);
            }
        });

        viewProfileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewProfileScreen vps=new ViewProfileScreen(user);
            }
        });
        searchAddQuestionsToButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewAddQuestionBankScreen va=new ViewAddQuestionBankScreen(user);
            }
        });
        enrollDropAButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HomeEnrollDropStudentScreen h=new HomeEnrollDropStudentScreen(user);
            }
        });
    }
}
