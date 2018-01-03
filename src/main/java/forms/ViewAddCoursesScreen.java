package forms;

import dao.CourseDAO;
import model.Course;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

public class ViewAddCoursesScreen {
    private JTable courseListTable;
    private JButton viewCourseDetailsButton;
    private JButton addNewCourseButton;
    private JPanel buttonPanel;
    private JPanel tablePanel;
    private JPanel viewaddcoursesPanel;
    private JLabel coursesLabel;
    private JScrollPane courseListScrollPane;
    private JButton backButton;
    private List<Course> courseList;
    private User user;
    private Object data[][];
    private CourseDAO courseDAO;

    public ViewAddCoursesScreen(final User user) {

        this.user = user;
        courseDAO=new CourseDAO();
        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(900, 450);
        FirstScreen.firstFrame.setContentPane(viewaddcoursesPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);

        String[] columnNames = {"CourseID", "Course Name","Start Date","End Date","Instructor","TA"};
        CourseDAO cdao=new CourseDAO();
        if(user.getRole().equalsIgnoreCase("PROF"))
            courseList=cdao.getCourseList(user.getUserName());
        else if(user.getRole().equalsIgnoreCase("TA")) {
            courseList = cdao.getTACourseList(user.getUserName());
            addNewCourseButton.setEnabled(false);
        }

        DefaultTableModel model = new DefaultTableModel(convertListtoObject(courseList), columnNames){

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        courseListTable.setModel(model);

        addNewCourseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddCoursesScreen acs=new AddCoursesScreen(user);
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(user.getRole().equalsIgnoreCase("PROF")){
                InstructorScreen is=new InstructorScreen(user);
                }
                else if(user.getRole().equalsIgnoreCase("TA")){
                TAScreen ta=new TAScreen(user);
                }
            }
        });
        viewCourseDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewCoursesScreen vcs=new ViewCoursesScreen(user);
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
            ob[i][0]=c.getCourseID();
            ob[i][1]=c.getCourseName();
            ob[i][2]=c.getStartDate();
            ob[i][3]=c.getEndDate();
            ob[i][4]=user.getFirstName();
            ob[i][5]=ta;
            i++;
        }
        return ob;
    }
}
