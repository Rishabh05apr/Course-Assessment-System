package forms;

import dao.ExerciseDAO;
import model.Exercise;
import model.Question;
import model.User;
import util.CommonUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AddRemoveQuestionsFromExerciseScreen {
    private JTextField questionIdTextField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton backButton;
    private JTable questionListTable;
    private JScrollPane questionListScrollPane;
    private JPanel questionListPanel;
    private JPanel buttonPanel;
    private JPanel AddRemoveQuestionsFromExercisePanel;
    private JLabel questionIdLabel;
    private JPanel exerciseAttributesPanel;
    private JPanel labelPanel;
    private JPanel textFieldPanel;
    private JTextField exerciseIdTextField;
    private JTextField startDateTextField;
    private JTextField incorrectPointsTextField;
    private JTextField endDateTextField;
    private JTextField correctPointsTextField;
    private JTextField nameTextField;
    private JTextField attemptsTextField;
    private JLabel nameLabel;
    private JLabel startDateLabel;
    private JLabel endDateLabel;
    private JLabel attemptsLabel;
    private JLabel correctAnswerPointsLabel;
    private JLabel incorrectAnswerPointsLabel;
    private JLabel noOfQuestionsLabel;
    private JLabel scoringPolicyLabel;
    private JLabel modeLabel;
    private JLabel exerciseIdLabel;
    private JButton saveAsNewExerciseButton;
    private JTextField numberOfQuestionsText;
    private JComboBox scorePolicyCombo;
    private JComboBox exModeCombo;
    private Exercise ex;
    private List<Question> questions;
    private DefaultTableModel model;
    private ExerciseDAO exerciseDAO;
    private User user;
    private String courseId;

    public AddRemoveQuestionsFromExerciseScreen(final String courseId,final User user,final Exercise exercise){

        this.user=user;
        this.ex=exercise;
        this.courseId=courseId;

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(700, 450);
        FirstScreen.firstFrame.setContentPane(AddRemoveQuestionsFromExercisePanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);


        exerciseDAO =new ExerciseDAO();
        questions= exerciseDAO.getQuestionsForExercise(exercise.getExId());

        String[] exModesArray = { "Standard", "Adaptive" };
        exModeCombo.setModel(new DefaultComboBoxModel(exModesArray));

        final String [] scorePolicyArray = {"Latest Attempt", "Maximum Score", "Average Score"};
        scorePolicyCombo.setModel(new DefaultComboBoxModel(scorePolicyArray));

        //Populating fields with current exercise values.
        exerciseIdTextField.setText(exercise.getExId().toString());
        exerciseIdTextField.setEditable(false);
        nameTextField.setText(exercise.getExName());
        startDateTextField.setText(exercise.getStartDate().toString());
        endDateTextField.setText(exercise.getEndDate().toString());
        attemptsTextField.setText(exercise.getExAttempts().toString());
        correctPointsTextField.setText(exercise.getPointsForCorrectAns().toString());
        incorrectPointsTextField.setText(exercise.getPointsForIncorrectAns().toString());
        numberOfQuestionsText.setText(exercise.getExNumOfQuestions().toString());
        scorePolicyCombo.setSelectedItem(exercise.getScoringPolicy());
        exModeCombo.setSelectedItem(exercise.getMode());

        String[] columnNames = {"Question Id", "Text", "Type", "Difficulty", "Hint"};
        model = new DefaultTableModel(convertListtoObject(questions), columnNames){

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        questionListTable.setModel(model);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddQuestionsToExerciseScreen aqtes= new AddQuestionsToExerciseScreen(user,exercise,courseId);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String qId = questionIdTextField.getText();
                //QuestionDAO qdao= new QuestionDAO();
                if (qId.equals("") || qId == null) {
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Valid Question ID");
                }
                else if(exerciseDAO.getQuestionsForExercise(exercise.getExId())!=null){
                    boolean flag = false;
                    String oraMessage = "";
                    try {
                        flag = exerciseDAO.deleteQuestionFromExercise(exercise.getExId(), Integer.parseInt(qId));
                    } catch(Exception e1) {
                        oraMessage = CommonUtil.formatOracleErrorMessage(e1.getMessage());
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, oraMessage);
                    }
                    if(flag) {
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Success");
                        questions= exerciseDAO.getQuestionsForExercise(exercise.getExId());

                        model.setRowCount(0);
                        Object o[][]=convertListtoObject(questions);
                        for(int i=0;i<o.length;i++)
                        {
                            model.addRow(new Object[]{o[i][0],o[i][1],o[i][2],o[i][3],o[i][4]});
                        }
                    }
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewAddExerciseScreen vaes=new ViewAddExerciseScreen(courseId, user);
            }
        });
        saveAsNewExerciseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if(CommonUtil.isValidString(startDateTextField.getText()))
                        exercise.setStartDate(Timestamp.valueOf(startDateTextField.getText()));
                    else
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Start Date.");
                    if(CommonUtil.isValidString(endDateTextField.getText()))
                        exercise.setEndDate(Timestamp.valueOf(endDateTextField.getText()));
                    else
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter End Date.");
                    if(CommonUtil.isValidString(attemptsTextField.getText()))
                        exercise.setExAttempts(Integer.parseInt(attemptsTextField.getText()));
                    else
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Number of Attempts.");
                    if(CommonUtil.isValidString(correctPointsTextField.getText()))
                        exercise.setPointsForCorrectAns(Integer.parseInt(correctPointsTextField.getText()));
                    else
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Points for Correct Answer.");
                    if(CommonUtil.isValidString(incorrectPointsTextField.getText()))
                        exercise.setPointsForIncorrectAns(Integer.parseInt(incorrectPointsTextField.getText()));
                    else
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Points for Incorrect Answer.");
                    if(CommonUtil.isValidString(numberOfQuestionsText.getText()))
                        exercise.setExNumOfQuestions(Integer.parseInt(numberOfQuestionsText.getText()));
                    else
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Number of Questions.");
                    if(CommonUtil.isValidString(nameTextField.getText()))
                        exercise.setExName(nameTextField.getText());
                    else
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Exercise Name.");

                    exercise.setMode(exModeCombo.getSelectedItem().toString().toUpperCase());
                    exercise.setScoringPolicy(scorePolicyCombo.getSelectedItem().toString().toUpperCase());

                    exercise.setExId(exerciseDAO.getLatestExerciseId()+1);
                    exercise.setCourse(courseId);

                    if (exerciseDAO.addExercise(exercise)) {
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Exercise Successfully Added");
                        ViewAddExerciseScreen viewAddExerciseScreen = new ViewAddExerciseScreen(courseId,user);
                    } else
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Error while adding exercise....!!");

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Error while adding exercise...!!");
                }
            }
        });
    }

    public Object[][] convertListtoObject(List<Question> questions)
    {
        Object ob[][]=new Object[questions.size()][5];
        int i=0;
        Iterator itr=questions.iterator();
        while (itr.hasNext())
        {
            Question q= (Question) itr.next();
            ob[i][0]=q.getqId();
            ob[i][1]=q.getText();
            ob[i][2]=q.getType();
            ob[i][3]=q.getDifficulty();
            ob[i][4]=q.getHint();

            i++;
        }
        return ob;
    }
}
