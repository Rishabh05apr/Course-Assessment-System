package forms;

import dao.UserDAO;
import exception.ApplicationException;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ViewProfileScreen {
    private JTextField firstnameTextfield;
    private JTextField lastnameTextfield;
    private JTextField idTextfield;
    private JTextField emailTextfield;
    private JTextField dobTextfield;
    private JPanel labelPanel;
    private JPanel textfieldPanel;
    private JPanel buttonLabel;
    private JButton backButton;
    private JLabel firstnameLabel;
    private JLabel lastnameLabel;
    private JLabel idLabel;
    private JLabel emailLabel;
    private JPanel viewprofilePanel;
    private JButton updateButton;
    private Role role;
    private String r="";
    String userName;


    public ViewProfileScreen(final User user){

        //this.loggedInAs=loggedInAs;
        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(400,450);
        FirstScreen.firstFrame.setContentPane(viewprofilePanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);
        idTextfield.setEditable(false);



        try {


            firstnameTextfield.setText(user.getFirstName());
            emailTextfield.setText(user.getEmail());
            idTextfield.setText(user.getId()+"");
            lastnameTextfield.setText(user.getLastName());

        } catch(Exception e) {
            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Error while retrieving details");
        }


        try {
            r=user.getRole();
            userName=user.getUserName();

            if((r.equals("GRAD")&& !UserDAO.isTA(userName))||r.equals("UG")){
                idLabel.setText("StudentID");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Error while retrieving details");
        }


        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(r.equals("TA")) {
                        role=new TAScreen(user);
                }
                else if(r.equals("PROF")){
                        role=new InstructorScreen(user);
                }
                else if(r.equals("UG") || r.equals("GRAD")){
                        role=new StudentScreen(user);
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (firstnameTextfield.getText().equals("") || lastnameTextfield.getText().equals("")
                        || emailTextfield.getText().equals("")) {
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please enter all the details");
                } else {

                    UserDAO udao = new UserDAO();
                    user.setFirstName(firstnameTextfield.getText());
                    user.setLastName(lastnameTextfield.getText());
                    user.setEmail(emailTextfield.getText());
                    user.setUserName(userName);

                    try {
                        if(udao.updateUserDetails(user))
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Success");
                        else
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Could not update the details");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Could not update the details");
                    }
                }
            }
        });

    }
}
