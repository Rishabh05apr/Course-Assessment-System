package forms;

import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentScreen extends Role {
    private JButton logoutButton;
    private JButton viewEditProfileButton;
    private JButton viewCoursesButton;
    private JLabel studenthomeLabel;
    private JPanel studentPanel;
    private User user;
   // private String loggedInAs;

    public StudentScreen(final User user) {

        //this.loggedInAs=loggedInAs;
        this.user = user;
        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(400, 450);
        FirstScreen.firstFrame.setContentPane(studentPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);


        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginScreen ls=new LoginScreen();
            }
        });
        viewEditProfileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewProfileScreen vps=new ViewProfileScreen(user);
            }
        });
        viewCoursesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StudentCoursesScreen scs=new StudentCoursesScreen(user);
            }
        });
    }
}
