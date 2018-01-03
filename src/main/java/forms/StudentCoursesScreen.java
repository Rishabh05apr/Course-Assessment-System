package forms;

import dao.CourseDAO;
import dao.UserDAO;
import model.Course;
import model.User;
import util.CommonUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

public class StudentCoursesScreen {
    private JTable courseListTable;
    private JTextField courseIDTextfield;
    private JButton goButton;
    private JButton backButton;
    private JLabel courseIDLabel;
    private JPanel courseIDPanel;
    private JScrollPane coursesListScrollPane;
    private JPanel tablePanel;
    private JPanel viewCoursesPanel;
    private User user;
    private List<Course> courseList;
    private CourseDAO courseDAO;
    private UserDAO userDAO;

    public StudentCoursesScreen(final User userName) {

        this.user=userName;
        courseDAO=new CourseDAO();
        userDAO=new UserDAO();

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(900, 450);
        FirstScreen.firstFrame.setContentPane(viewCoursesPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);

        String[] columnNames = {"CourseID", "Course Name","Start Date","End Date","Instructor","TA"};
        CourseDAO cdao=new CourseDAO();
        courseList=cdao.getStudentCourseList(userName.getId()+"");
        DefaultTableModel model = new DefaultTableModel(convertListtoObject(courseList), columnNames){

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        courseListTable.setModel(model);

        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!CommonUtil.isValidString(courseIDTextfield.getText().toUpperCase())){
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter valid CourseID...!!");
                }
                else{
                    StudentHomeworkScreen sh=new StudentHomeworkScreen(user,courseIDTextfield.getText().toUpperCase());
                }

            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StudentScreen sc=new StudentScreen(userName);
            }
        });
    }
    public Object[][] convertListtoObject(List<Course> courseList)
    {
        Object ob[][]=new Object[courseList.size()][6];
        int i=0;
        Iterator itr=courseList.iterator();
        while (itr.hasNext())
        {

            Course c= (Course) itr.next();
            List<User> userList=courseDAO.getTAsForCourse(c.getCourseID());
            String ta="";
            for(User u:userList){
                ta+=u.getFirstName()+",";
            }
            if(ta.length()>0)
                ta=ta.substring(0, ta.length() - 1);

            User prof=userDAO.getUserDetails(courseDAO.getProfForACourse(c.getCourseID()));
            ob[i][0]=c.getCourseID();
            ob[i][1]=c.getCourseName();
            ob[i][2]=c.getStartDate();
            ob[i][3]=c.getEndDate();
            ob[i][4]=prof.getFirstName();
            ob[i][5]=ta;
            i++;
        }
        return ob;
    }
}
