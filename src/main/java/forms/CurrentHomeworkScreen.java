package forms;

import dao.ExerciseDAO;
import dao.SubmissionDAO;
import model.Exercise;
import model.User;
import service.AdaptiveExerciseService;
import util.CommonUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.HTMLDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

public class CurrentHomeworkScreen {
    private JTable currentHomeworkTable;
    private JButton attemptButton;
    private JTextField homeworkIDTextfield;
    private JButton backButton;
    private JPanel currentHomeworkPanel;
    private JPanel tablePanel;
    private JScrollPane currentHomeworkTableScrollPane;
    private JPanel buttonPanel;
    private User user;
    private ExerciseDAO exerciseDAO;
    private List<Exercise> exerciseList;
    private DefaultTableModel model;
    private SubmissionDAO submissionDAO;

    public CurrentHomeworkScreen(final User user,final String courseID) {

        this.user=user;

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(400, 450);
        FirstScreen.firstFrame.setContentPane(currentHomeworkPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);
        exerciseDAO = new ExerciseDAO();
        submissionDAO = new SubmissionDAO();


        try {
            exerciseList= exerciseDAO.getCurrentExerciseList(courseID);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Error while retrieving exercises..");
        }

        String[] columnNames = {"Exercise ID","Start Date","End Date","Name", "Attempts","Number of questions","Incorrect points","Correct points"};
        model = new DefaultTableModel(convertListtoObject(exerciseList), columnNames){

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        currentHomeworkTable.setModel(model);

        attemptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!CommonUtil.isValidString(homeworkIDTextfield.getText()))
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Exercise ID cannot be left blank...");
                else{
                    try {
                        Integer exerciseId = Integer.parseInt(homeworkIDTextfield.getText());
                        boolean flag=false;
                        Iterator itr=exerciseList.iterator();
                        while(itr.hasNext()){
                            Exercise ee= (Exercise) itr.next();
                            if(ee.getExId()==exerciseId)
                                flag=true;
                        }
                        if(flag==true) {

                            Exercise exercise = exerciseDAO.getExerciseDetails(exerciseId);


                            if (exercise != null) {
                                if(exercise.getExAttempts()<0 || exercise.getExAttempts()>submissionDAO.getLatestSubmissionAttempt(exerciseId,user.getId())) {
                                        if (exercise.getMode().equalsIgnoreCase("STANDARD")) {
                                        StandardExerciseScreen se = new StandardExerciseScreen(user, exercise);
                                    } else if (exercise.getMode().equalsIgnoreCase("ADAPTIVE")) {
                                        AdaptiveExerciseScreen ae = new AdaptiveExerciseScreen(user, exercise);
                                    } else {
                                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Not a valid Exercise Mode....!!!");
                                    }
                                }else
                                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "You have exhausted your attempt limit for this exercise");


                            } else
                                JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Exercise with given Exercise ID could not be found....");

                        }
                        else
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Exercise with given Exercise ID could not be found....");

                    } catch (NumberFormatException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please enter a valid Exercise ID");
                    }
                }

            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StudentHomeworkScreen sh=new StudentHomeworkScreen(user,courseID);
            }
        });
    }
    public Object[][] convertListtoObject(List<Exercise> exerciseList)
    {
        Object ob[][]=new Object[exerciseList.size()][8];
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
            i++;
        }
        return ob;
    }

}
