package forms;

import dao.AnswerDAO;
import dao.ExerciseDAO;
import dao.QuestionDAO;
import dao.SubmissionDAO;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PastExerciseDetailScreen {
    private JPanel outerPanel;
    private JPanel innerPanel;
    private JScrollPane scrollPane;
    private JPanel labelPanel;
    private JLabel exerciseNameLabel;
    private JLabel correctPointLabel;
    private JLabel incorrectPointLabel;
    private JLabel exerciseModeLabel;
    private JLabel numberOfQuestionsLabel;
    private JLabel totalScoreLabel;
    private User user;
    private Exercise exercise;
    private SubmissionDAO submissionDAO;
    private Submission submission;
    private ExerciseDAO exerciseDAO;
    private QuestionDAO questionDAO;
    private AnswerDAO answerDAO;
    private int submissionId;
    private List<SubmissionDetails> submissionDetailsList;

    public PastExerciseDetailScreen(final User user, Submission submission) {

        this.user=user;
        this.submission=submission;

        submissionDAO=new SubmissionDAO();
        exerciseDAO=new ExerciseDAO();
        questionDAO=new QuestionDAO();
        answerDAO=new AnswerDAO();

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(1000, 650);
        FirstScreen.firstFrame.setContentPane(outerPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        exercise=exerciseDAO.getExerciseDetails(submission.getExerciseId());
        submissionDetailsList=submissionDAO.getAllSubmissionDetailsForAnAttempt(submission.getSubmissionId());

        correctPointLabel.setText("Points Per Correct answer: "+exercise.getPointsForCorrectAns());
        incorrectPointLabel.setText("Points Per Wrong answer: "+exercise.getPointsForIncorrectAns());
        exerciseNameLabel.setText("Exercise Name: "+exercise.getExName());
        exerciseModeLabel.setText("Exercise Mode: "+exercise.getMode());
        numberOfQuestionsLabel.setText("Number of Questions: "+exercise.getExNumOfQuestions());
        totalScoreLabel.setText("Total Score: "+submission.getTotalScore()+" out of "+exercise.getPointsForCorrectAns()*exercise.getExNumOfQuestions());

        innerPanel.setLayout(new GridLayout(0,1));

        for(int i=0;i<submissionDetailsList.size();i++){

            SubmissionDetails submissionDetails=submissionDetailsList.get(i);
            Question question=questionDAO.getQuestionDetails(submissionDetails.getQuestionId());

            JPanel p=new JPanel(new GridLayout(0,1));
            JLabel jLabel=new JLabel();
            jLabel.setText("Question "+(i+1)+": "+getQuestionText(question));
            p.add(jLabel);

            if(submissionDetails.getAnsId()!=0){
                Answer answer=answerDAO.getAnswer(submissionDetails.getAnsId());
                JLabel ansLabel=new JLabel();
                ansLabel.setText("Your Answer :"+ answer.getAns());
                p.add(ansLabel);

                if(answer.getCategory().equalsIgnoreCase("INCORRECT")){
                    JLabel corr_incorr=new JLabel();
                    corr_incorr.setText("InCorrect Answer: " + exercise.getPointsForIncorrectAns()+" points");
                    p.add(corr_incorr);
                }
                else if(answer.getCategory().equalsIgnoreCase("CORRECT")){
                    JLabel corr_incorr=new JLabel();
                    corr_incorr.setText("Correct Answer: " + exercise.getPointsForCorrectAns()+" points");
                    p.add(corr_incorr);
                }
            }
            else {
                JLabel corr_incorr=new JLabel();
                corr_incorr.setText("Not Attempted: 0 points");
                p.add(corr_incorr);
            }
            if(System.currentTimeMillis() > exercise.getEndDate().getTime()) {
                JLabel detailed_solution = new JLabel();
                detailed_solution.setText("Detailed Solution: " + question.getDetailedSolution());
                p.add(detailed_solution);
            }
            innerPanel.add(p);
        }


        JButton back=new JButton("BACK");
        outerPanel.add(back,BorderLayout.SOUTH);


        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PastHomeworkScreen pastHomeworkScreen=new PastHomeworkScreen(user,exercise.getCourse());
            }});
        FirstScreen.firstFrame.setVisible(true);
    }

    private String getQuestionText(Question question){
        String questionText = question.getText();
        if(question.getType().equalsIgnoreCase("PARAMETERIZED")){
            question.setParameterList(questionDAO.getParametersForQuestionFromParameterSet(submission.getSubmissionId()));
            for(int i=0;i<question.getParameterList().size();i++){
                questionText = questionText.replaceFirst("<param>",question.getParameterList().get(i).getParamValue());
            }
        }
        return questionText;
    }
}
