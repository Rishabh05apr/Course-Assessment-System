package forms;

import dao.ExerciseDAO;
import dao.SubmissionDAO;
import model.Exercise;
import model.Submission;
import model.User;
import util.CommonUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

public class PastHomeworkScreen {
    private JPanel pastHomeworkPanel;
    private JPanel tablePanel;
    private JScrollPane pastHomeworkTableScrollPane;
    private JTable pastHomeworkTable;
    private JPanel buttonPanel;
    private JTextField homeworkIDTextfield;
    private JButton showDetailsButton;
    private JButton backButton;
    private JPanel pastHomeworkOuterPanel;
    private JLabel snoLabel;
    private User user;
    private DefaultTableModel model;
    private List<Submission> submissionList;
    private SubmissionDAO submissionDAO;

    public PastHomeworkScreen(final User user, final String courseID) {

        this.user=user;

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(400, 450);
        FirstScreen.firstFrame.setContentPane(pastHomeworkPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);
        submissionDAO=new SubmissionDAO();

        try {
            submissionList=submissionDAO.getAllSubmissionsForAStudent(courseID,user);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Error while retrieving submissions");
        }

        String[] columnNames = {"SNO","EX_ID","ATTEMPT NO","SCORE","SUBMISSION TIME"};
        model = new DefaultTableModel(convertListtoObject(submissionList), columnNames){

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        pastHomeworkTable.setModel(model);
        showDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!CommonUtil.isValidString(homeworkIDTextfield.getText()))
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Sno cannot be left blank...");
                    else {
                        Integer submissionid = Integer.parseInt(homeworkIDTextfield.getText());
                        if(submissionid>submissionList.size()||submissionid<0){
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Not a valid Sno");
                        }
                        else {
                            PastExerciseDetailScreen pastExerciseDetailScreen = new PastExerciseDetailScreen(user, submissionList.get(submissionid - 1));
                        }
                    }
                }
                catch (Exception ee){
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Not a valid Sno");
                }

            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StudentHomeworkScreen sh=new StudentHomeworkScreen(user,courseID);
            }
        });

    }
    public Object[][] convertListtoObject(List<Submission> submissionList)
    {
        Object ob[][]=new Object[submissionList.size()][5];
        int i=0;
        Iterator itr=submissionList.iterator();
        while (itr.hasNext())
        {
            Submission s= (Submission) itr.next();
            ob[i][0]=i+1;
            ob[i][1]=s.getExerciseId();
            ob[i][2]=s.getAttemptNo();
            ob[i][3]=s.getTotalScore();
            ob[i][4]=s.getAttemptedAt();

            i++;
        }
        return ob;
    }
}
