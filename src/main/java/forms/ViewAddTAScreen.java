package forms;

import dao.CourseDAO;
import dao.UserDAO;
import model.User;
import util.CommonUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class ViewAddTAScreen {
    private JTable taListTable;
    private JButton addTAButton;
    private JButton backButton;
    private JScrollPane taListScrollPane;
    private JPanel viewAddTAPanel;
    private JTextField studentUsernameTextfield;
    private JLabel studentUsernameLabel;
    private String courseID;
    private User user;
    private CourseDAO cdao;
    private UserDAO udao;
    private List<User> users;
    private Object data[][];
    private DefaultTableModel model;

    public ViewAddTAScreen(final User user,final String courseID) {

        this.courseID=courseID;
        this.user =user;


            FirstScreen.firstFrame.getContentPane().removeAll();
            FirstScreen.firstFrame.setSize(550, 450);
            FirstScreen.firstFrame.setContentPane(viewAddTAPanel);
            FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            FirstScreen.firstFrame.setVisible(true);
            cdao=new CourseDAO();
            udao=new UserDAO();
            users=cdao.getTAsForCourse(courseID);

            String[] columnNames = {"UserID", "First Name","Last Date"};
             model = new DefaultTableModel(convertListtoObject(users), columnNames){

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        taListTable.setModel(model);



        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            ViewCoursesScreen vcs=new ViewCoursesScreen(user,courseID);
            }
        });
        addTAButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ta_id = studentUsernameTextfield.getText();
                if (ta_id.equals("") || ta_id == null) {
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Valid Student ID");
                }
                else if(udao.getUserDetails(ta_id)!=null){
                    boolean flag = false;
                    String oraMessage = "";
                    try {
                         flag = udao.addTA(courseID,udao.getUserDetails(ta_id).getUserName());
                    } catch (SQLException e1) {
                        oraMessage = CommonUtil.formatOracleErrorMessage(e1.getMessage());
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, oraMessage);
                    }
                    if(flag) {
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Success");
                        users=cdao.getTAsForCourse(courseID);

                        model.setRowCount(0);
                        Object o[][]=convertListtoObject(users);
                        for(int i=0;i<o.length;i++)
                        {
                            model.addRow(new Object[]{o[i][0],o[i][1],o[i][2]});
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Error");
                }
                else
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Error");
            }
        });
    }
    public Object[][] convertListtoObject(List<User> users)
    {
        Object ob[][]=new Object[users.size()][3];
        int i=0;
        Iterator itr=users.iterator();
        while (itr.hasNext())
        {
            User c= (User) itr.next();
            ob[i][0]=c.getUserName();
            ob[i][1]=c.getFirstName();
            ob[i][2]=c.getLastName();

            i++;
        }
        return ob;
    }
}
