package forms;

import dao.CourseDAO;
import dao.ExerciseDAO;
import dao.UserDAO;
import model.Course;
import model.Exercise;
import util.CommonUtil;
import model.User;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class ViewAddExerciseScreen {
    private JTextField exerciseIdTextField;
    private JButton modifyExerciseButton;
    private JButton backButton;
    private JTable exerciseListTable;
    private JScrollPane exerciseListScrollPane;
    private JPanel viewAddExercisePanel;
    private JPanel exerciseListPanel;
    private JPanel buttonPanel;
    private JLabel exerciseIdLabel;
    private JButton addExerciseButton;
    private int exId;
    private String courseId;
    private User user;
    private List<Exercise> exercises;
    private Object data[][];
    private DefaultTableModel model;
    private ExerciseDAO edao;

    public ViewAddExerciseScreen(final String courseId, final User user){

        this.courseId=courseId;
        this.user = user;

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(550, 450);
        FirstScreen.firstFrame.setContentPane(viewAddExercisePanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);
        edao=new ExerciseDAO();
        exercises=edao.getExerciseList(courseId);

        if(user.getRole().equalsIgnoreCase("TA")){
            modifyExerciseButton.setEnabled(false);
            addExerciseButton.setEnabled(false);
            exerciseIdTextField.setEditable(false);
        }

        String[] columnNames = {"Exercise ID","Start Date","End Date","Name", "Attempts","Number of questions","Incorrect points","Correct points","Score Policy","Mode"};
        model = new DefaultTableModel(convertListtoObject(exercises), columnNames){

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        exerciseListTable.setModel(model);
        modifyExerciseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String exId = exerciseIdTextField.getText();
                if (exId.equals("") || exId == null) {
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Valid Exercise ID");
                }
                else
                {
                    Exercise ex=edao.getExerciseDetails(Integer.parseInt(exId));
                    if (ex != null) {
                    boolean flag = false;
                    String oraMessage = "";
                    try {
                        AddRemoveQuestionsFromExerciseScreen scn = new AddRemoveQuestionsFromExerciseScreen(courseId,user, ex);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        oraMessage = CommonUtil.formatOracleErrorMessage(e1.getMessage());
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, oraMessage);
                    }
                }
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewCoursesScreen vcs=new ViewCoursesScreen(user,courseId);
            }
        });
        addExerciseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddExerciseScreen aes=new AddExerciseScreen(user,courseId);
            }
        });
    }

    public Object[][] convertListtoObject(List<Exercise> exerciseList)
    {
        Object ob[][]=new Object[exerciseList.size()][10];
        int i=0;
        Iterator itr=exerciseList.iterator();
        while (itr.hasNext())
        {
            Exercise e= (Exercise) itr.next();
            ob[i][0]=e.getExId();
            ob[i][1]=e.getStartDate();
            ob[i][2]=e.getEndDate();
            ob[i][3]=e.getExName();
            ob[i][4]=e.getExAttempts();
            ob[i][5]=e.getExNumOfQuestions();
            ob[i][6]=e.getPointsForIncorrectAns();
            ob[i][7]=e.getPointsForCorrectAns();
            ob[i][8]=e.getScoringPolicy();
            ob[i][9]=e.getMode();
            i++;
        }
        return ob;
    }


}
