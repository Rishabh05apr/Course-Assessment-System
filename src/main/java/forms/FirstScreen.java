package forms;

import config.DBConfig;
import util.JdbcDataUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FirstScreen {
    private JPanel firstPanel;
    private JButton loginButton;
    private JButton exitButton;

    public static JFrame firstFrame;

    public FirstScreen() {

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginScreen loginScreen=new LoginScreen();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                firstFrame.setVisible(false);
                firstFrame.dispose();
            }
        });

    }

    public static void main(String[] args) {
        JdbcDataUtil.dbConfig = new DBConfig("jdbc.properties");
        firstFrame=new JFrame();
        firstFrame.setSize(400,450);
        firstFrame.setContentPane(new FirstScreen().firstPanel );
        firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        firstFrame.setVisible(true);
    }


}
