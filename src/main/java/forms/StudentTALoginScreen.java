package forms;

import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentTALoginScreen extends Role {
    private JButton loginAsStudentButton;
    private JButton loginAsTAButton;
    private JButton logoutButton;
    private JPanel studentTALoginScreenPanel;
    private User user;


    public StudentTALoginScreen(final User user) {
        this.user=user;

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(400, 450);
        FirstScreen.firstFrame.setContentPane(studentTALoginScreenPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);

        loginAsStudentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                StudentScreen studentScreen=new StudentScreen(user);

            }
        });
        loginAsTAButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                user.setRole("TA");
                TAScreen taScreen=new TAScreen(user);

            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginScreen loginScreen=new LoginScreen();
            }
        });
    }
}
