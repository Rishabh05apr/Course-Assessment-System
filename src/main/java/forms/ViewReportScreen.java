package forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import dao.CourseDAO;
import dao.ExerciseDAO;
import dao.ReportDAO;
import model.DetailedReport;
import model.SummarizedReport;
import model.User;
import util.CommonUtil;

public class ViewReportScreen {
    private JButton backButton;
    private JTable reportListTable;
    private JPanel buttonPanel;
    private JScrollPane reportListScrollPane;
    private JPanel reportListPanel;
    private JPanel viewReportScreenPanel;
    private JTextField userIdTextField;
    private JButton viewReportButton;
    private JLabel userIdLabel;
    private User user;
    private String courseID;
    private DefaultTableModel model;
    private List<SummarizedReport> summarizedReportList;
    private ExerciseDAO exerciseDAO;
    private CourseDAO courseDAO;
    private ReportDAO reportDAO;

    public ViewReportScreen(final User user,final String courseID) {

        this.user=user;
        this.courseID=courseID;
        exerciseDAO=new ExerciseDAO();
        courseDAO=new CourseDAO();
        reportDAO= new ReportDAO();
        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(400,450);
        FirstScreen.firstFrame.setContentPane(viewReportScreenPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);

        summarizedReportList =reportDAO.generateReport(courseID);

        String columnNames[]={"Student Id","First name", "Last name", "Exercise ID", "Final Score", "Scoring Policy"};
        //int numberOfExercises=exerciseDAO.numberOfExercises(courseID);
        //for(int i=3;i<numberOfExercises+3;i++)
        //    columnNames[i]= "Exercise " + i + " Score";

        model = new DefaultTableModel(convertListtoObject(summarizedReportList), columnNames){

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        reportListTable.setModel(model);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewCoursesScreen vcs=new ViewCoursesScreen(user,courseID);
            }
        });
        viewReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userId = userIdTextField.getText();
                if (userId.equals("") || userId == null) {
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Valid Student ID");
                }
                else
                {
                    List<DetailedReport> detailedReport =reportDAO.generateReportforStudent(courseID, Integer.parseInt(userId));
                    if (detailedReport != null) {
                        boolean flag = false;
                        String oraMessage = "";
                        try {
                            ViewReportDetailsForAStudentScreen scn = new ViewReportDetailsForAStudentScreen(user, courseID,Integer.parseInt(userId));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            oraMessage = CommonUtil.formatOracleErrorMessage(e1.getMessage());
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, oraMessage);
                        }
                    }
                }
            }
        });
    }

    public Object[][] convertListtoObject(List<SummarizedReport> summarizedReportList)
    {
        Object ob[][]=new Object[summarizedReportList.size()][6];
        int i=0;
        Iterator itr= summarizedReportList.iterator();

        while (itr.hasNext())
        {
            SummarizedReport summarizedReport = (SummarizedReport) itr.next();

            ob[i][0]= summarizedReport.getUserId();
            ob[i][1]= summarizedReport.getFirstName();
            ob[i][2]= summarizedReport.getLastName();
            ob[i][3]= summarizedReport.getExerciseId();
            ob[i][4]= summarizedReport.getFinalScore();
            ob[i][5]= summarizedReport.getScorePolicy();
            
            i++;
        }
        return ob;
    }
}
