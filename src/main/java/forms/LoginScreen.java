package forms;

import dao.UserDAO;
import exception.ApplicationException;
import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class LoginScreen extends Role{
    private JButton loginButton;
    private JButton resetButton;
    private JTextField usernameTextfield;
    private JTextField passwordTextfield;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JPanel loginPanel;
    private JPanel labelPanel;
    private JPanel textfieldPanel;
    private JPanel buttonPanel;
    private JPasswordField passwordField;
    private User user;
    private JFrame loginFrame;
    Role role;

    public LoginScreen() {

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(400,450);
        FirstScreen.firstFrame.setContentPane(loginPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);


        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String userName = usernameTextfield.getText();
                String password = String.valueOf(passwordField.getPassword());
                if(userName == null || password == null || userName.trim().equals("") || password.trim().equals("")){
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please enter valid username and password");
                }
                else{
                    UserDAO userDAO = new UserDAO();
                    try {
                        Map<String,String> userInfo = userDAO.authenticateUser(userName,password);
                        if(userInfo==null){
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Authentication Failed");
                            }
                        else{

                            user=userDAO.getUserDetails(userName);

                            if(userInfo.get("role").trim().equals("PROF")){

                                role=new InstructorScreen(user);
                            }
                            else if(userInfo.get("role").trim().equals("GRAD") && UserDAO.isTA(userName)){
                                role=new StudentTALoginScreen(user);
                            }
                            else if(userInfo.get("role").trim().equals("GRAD")|| userInfo.get("role").trim().equals("UG")){
                                String loggedInAs="";
                                role=new StudentScreen(user);
                            }
                            else
                                JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Role not defined");

                        }
                    } catch (ApplicationException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                passwordField.setText("");
                usernameTextfield.setText("");
            }
        });
    }


}
