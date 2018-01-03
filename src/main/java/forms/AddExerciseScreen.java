package forms;

import dao.ExerciseDAO;
import model.Exercise;
import model.User;
import util.CommonUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

public class AddExerciseScreen {

    private JTextField startDateTextField;
    private JTextField endDateTextField;
    private JTextField nameTextField;
    private JTextField attemptsTextField;
    private JTextField CorrectPointsTextField;
    private JTextField IncorrectPointsTextField;
    private JTextField numberOfQuestionsTextField;
    private JButton saveExerciseButton;
    private JPanel buttonPanel;
    private JPanel labelPanel;
    private JPanel textFieldPanel;
    private JLabel incorrectPointsLabel;
    private JLabel correctPointsLabel;
    private JLabel attemptsLabel;
    private JLabel numberOfQuestionsLabel;
    private JLabel scorePolicyLabel;
    private JLabel nameLabel;
    private JLabel endDateLabel;
    private JLabel modeLabel;
    private JLabel startDateLabel;
    private JPanel AddExerciseScreenPanel;
    private JButton resetButton;
    private JButton backButton;
    private JComboBox exModeCombo;
    private JComboBox scorePolicyCombo;
    private User user;
    private String courseId;
    private ExerciseDAO exerciseDAO;

    public AddExerciseScreen(final User user, final String courseId)
    {
        this.user =user;
        this.courseId=courseId;

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(550, 450);
        FirstScreen.firstFrame.setContentPane(AddExerciseScreenPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);
        exerciseDAO =new ExerciseDAO();
        final Exercise exercise=new Exercise();
        String[] exModesArray = { "Standard", "Adaptive" };
        exModeCombo.setModel(new DefaultComboBoxModel(exModesArray));

        final String [] scorePolicyArray = {"Latest Attempt", "Maximum Score", "Average Score"};
        scorePolicyCombo.setModel(new DefaultComboBoxModel(scorePolicyArray));



        saveExerciseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    try {
                        if(CommonUtil.isValidString(startDateTextField.getText()))
                            exercise.setStartDate(Timestamp.valueOf(startDateTextField.getText() + " 00:00:00"));
                        else
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Start Date.");
                        if(CommonUtil.isValidString(endDateTextField.getText()))
                            exercise.setEndDate(Timestamp.valueOf(endDateTextField.getText() + " 00:00:00"));
                        else
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter End Date.");
                        if(CommonUtil.isValidString(attemptsTextField.getText()))
                            exercise.setExAttempts(Integer.parseInt(attemptsTextField.getText()));
                        else
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Number of Attempts.");
                        if(CommonUtil.isValidString(CorrectPointsTextField.getText()))
                            exercise.setPointsForCorrectAns(Integer.parseInt(CorrectPointsTextField.getText()));
                        else
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Points for Correct Answer.");
                        if(CommonUtil.isValidString(IncorrectPointsTextField.getText()))
                            exercise.setPointsForIncorrectAns(Integer.parseInt(IncorrectPointsTextField.getText()));
                        else
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Points for Incorrect Answer.");
                        if(CommonUtil.isValidString(numberOfQuestionsTextField.getText()))
                            exercise.setExNumOfQuestions(Integer.parseInt(numberOfQuestionsTextField.getText()));
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
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Error while adding exercise...!!");
                    }
                }
        });
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                startDateTextField.setText("");
                endDateTextField.setText("");
                nameTextField.setText("");
                attemptsTextField.setText("");
                CorrectPointsTextField.setText("");
                IncorrectPointsTextField.setText("");
                numberOfQuestionsTextField.setText("");
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewAddExerciseScreen vaes= new ViewAddExerciseScreen(courseId, user);
            }
        });
    }
}
