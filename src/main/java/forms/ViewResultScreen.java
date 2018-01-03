package forms;

import dao.SubmissionDAO;
import model.Answer;
import model.Exercise;
import model.Question;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import model.Question;

public class ViewResultScreen {
    private JPanel outerPanel;
    private JLabel exerciseNameLabel;
    private JLabel correctPointLabel;
    private JLabel incorrectPointLabel;
    private JLabel exerciseModeLabel;
    private JLabel numberOfQuestionsLabel;
    private JLabel totalScoreLabel;
    private JPanel labelPanel;
    private JPanel innerPanel;
    private JScrollPane scrollpane;
    private User user;
    private Exercise exercise;
    private SubmissionDAO submissionDAO;
    private Answer answer;

    public ViewResultScreen(final User user, final Exercise exercise, int submissionId) {
        this.user=user;
        this.exercise=exercise;
        submissionDAO=new SubmissionDAO();
        answer=new Answer();


        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(1000, 650);
        FirstScreen.firstFrame.setContentPane(outerPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        correctPointLabel.setText("Points Per Correct answer: "+exercise.getPointsForCorrectAns());
        incorrectPointLabel.setText("Points Per Wrong answer: "+exercise.getPointsForIncorrectAns());
        exerciseNameLabel.setText("Exercise Name: "+exercise.getExName());
        exerciseModeLabel.setText("Exercise Mode: "+exercise.getMode());
        numberOfQuestionsLabel.setText("Number of Questions: "+exercise.getExNumOfQuestions());
        totalScoreLabel.setText("Total Score: "+submissionDAO.getTotalScore(submissionId)+" out of "+exercise.getPointsForCorrectAns()*exercise.getExNumOfQuestions());
        innerPanel.setLayout(new GridLayout(0,1));

        int score;
        for(int i=0;i<exercise.getQuestionBank().size();i++){
            Question question = exercise.getQuestionBank().get(i);

            JPanel p=new JPanel(new GridLayout(0,1));
            JLabel jLabel=new JLabel();
            jLabel.setText("Question "+(i+1)+": "+getQuestionText(question));
            p.add(jLabel);
            score=submissionDAO.getPointScoredForAQuestion(submissionId,question.getqId());

            JLabel ans=new JLabel();
            if(score>0) {
                ans.setText("Correct Answer: " + exercise.getPointsForCorrectAns()+" points");
                p.add(ans);
            }
            else if (score<0) {
                ans.setText("InCorrect Answer: " + exercise.getPointsForIncorrectAns()+" points");
                p.add(ans);
                JLabel hint=new JLabel();
                hint.setText("Hint: "+question.getHint());
                p.add(hint);
            }
            else if(score==0) {
                ans.setText("Not Attempted: 0 points");
                p.add(ans);
            }

            innerPanel.add(p);

    }

        JButton back=new JButton("BACK");
        outerPanel.add(back,BorderLayout.SOUTH);


        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CurrentHomeworkScreen currentHomeworkScreen=new CurrentHomeworkScreen(user,exercise.getCourse());
            }});
        FirstScreen.firstFrame.setVisible(true);


    }
        private String getQuestionText(Question question){
            String questionText = question.getText();
            if(question.getType().equalsIgnoreCase("PARAMETERIZED")){
                for(int i=0;i<question.getParameterList().size();i++){
                    questionText = questionText.replaceFirst("<param>",question.getParameterList().get(i).getParamValue());
                }
            }
            return questionText;
        }
}
