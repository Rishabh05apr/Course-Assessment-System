package forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.CourseDAO;
import dao.ExerciseDAO;
import dao.ReportDAO;
import model.DetailedReport;
import model.SummarizedReport;
import model.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;


public class ViewReportDetailsForAStudentScreen {
    private JTextField studentIdTextField;
    private JTextField lastNameTextField;
    private JTextField firstNameTextField;
    private JTextField usernameTextField;
    private JLabel lastNameLabel;
    private JLabel firstNameLabel;
    private JLabel studentIdLabel;
    private JButton backButton;
    private JTable studentReportListTable;
    private JScrollPane studentReportListScrollPane;
    private JPanel studentReportListPanel;
    private JPanel buttonPanel;
    private JPanel textFieldPanel;
    private JPanel labelPanel;
    private JPanel studentDetailsPanel;
    private JPanel viewReportDetailsForAStudentPanel;
    private User user;
    private String courseId;
    private CourseDAO courseDAO;
    private ExerciseDAO exerciseDAO;
    private List<DetailedReport> detailedReportList;
    private DefaultTableModel model;
    private ReportDAO reportDAO;
    private int userId;

    public ViewReportDetailsForAStudentScreen(final User user, final String courseId, final int userId)
    {
        this.user=user;
        this.courseId=courseId;
        this.userId=userId;
        courseDAO=new CourseDAO();
        exerciseDAO=new ExerciseDAO();
        reportDAO=new ReportDAO();
        detailedReportList =reportDAO.generateReportforStudent(courseId,userId);

        studentIdTextField.setText(detailedReportList.get(0).getUserId().toString());
        firstNameTextField.setText(detailedReportList.get(0).getFirstName());
        lastNameTextField.setText(detailedReportList.get(0).getLastName());
        studentIdTextField.setEditable(false);
        firstNameTextField.setEditable(false);
        lastNameTextField.setEditable(false);

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(550, 450);
        FirstScreen.firstFrame.setContentPane(viewReportDetailsForAStudentPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);

        String columnNames[]={"Exercise ID", "Attempt number", "Score", "Attempted at"};
        model = new DefaultTableModel(convertListtoObject(detailedReportList), columnNames){

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        studentReportListTable.setModel(model);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewReportScreen viewReportScreen= new ViewReportScreen(user, courseId);
            }
        });
    }

    public Object[][] convertListtoObject(List<DetailedReport> summarizedReportList)
    {
        Object ob[][]=new Object[summarizedReportList.size()][4];
        int i=0;
        Iterator itr= summarizedReportList.iterator();
        // usernameTextField.setText("");

        while (itr.hasNext())
        {
            DetailedReport detailedReport = (DetailedReport) itr.next();
            //int noOfExcercises=summarizedReport.getUserId();

            ob[i][0]= detailedReport.getExerciseId();
            ob[i][1]= detailedReport.getAttemptNo();
            ob[i][2]= detailedReport.getScore();
            ob[i][3]= detailedReport.getAttemptedAt();
            //ob[i][4]= detailedReport.getFinalScore();
            //ob[i][5]= detailedReport.getScorePolicy();
            /*for(int j=4;j<noOfExcercises+4;j++) {
                ob[i][j] = summarizedReport.getScores()[j];
            }*/

            i++;
        }
        return ob;
    }
}
