package forms;

import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TAScreen extends Role {
    private JButton logoutButton;
    private JButton viewProfileButton;
    private JButton enrollDropAButton;
    private JButton viewCoursesButton;
    private JLabel tahomeLabel;
    private JPanel taPanel;


    public TAScreen(final User user) {


        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(400, 450);
        FirstScreen.firstFrame.setContentPane(taPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginScreen ls=new LoginScreen();
            }
        });

        viewProfileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewProfileScreen vps=new ViewProfileScreen(user);
            }
        });

        viewCoursesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewAddCoursesScreen vac=new ViewAddCoursesScreen(user);
            }
        });
        enrollDropAButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HomeEnrollDropStudentScreen h=new HomeEnrollDropStudentScreen(user);
            }
        });
    }
}
