package forms;


import dao.SubmissionDAO;
import model.*;
import service.StandardExerciseService;
import util.CommonUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class StandardExerciseScreen {

    private JScrollPane standardExerciseScrollPane;
    private JPanel standardExerciseOuterPanel;
    private JPanel standardExerciseInnerPanel;
    private JLabel exerciseNameLabel;
    private JLabel correctPointLabel;
    private JLabel exerciseModeLabel;
    private JLabel numberOfQuestionsLabel;
    private JLabel incorrectPointLabel;
    private JPanel labelPanel;
    private StandardExerciseService standardExerciseService;
    private Exercise standardExercise;
    private SubmissionDAO submissionDAO;
    private User user;
    private Exercise exercise;
    private int submissionId;

    public StandardExerciseScreen(final User user, final Exercise exercise) {

        this.user=user;
        this.exercise=exercise;

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(1000, 650);
        FirstScreen.firstFrame.setContentPane(standardExerciseOuterPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);

        standardExerciseService = new StandardExerciseService();
        submissionDAO = new SubmissionDAO();

        correctPointLabel.setText("Points Per Correct answer: "+exercise.getPointsForCorrectAns());
        incorrectPointLabel.setText("Points Per Wrong answer: "+exercise.getPointsForIncorrectAns());
        exerciseNameLabel.setText("Exercise Name: "+exercise.getExName());
        exerciseModeLabel.setText("Exercise Mode: "+exercise.getMode());
        numberOfQuestionsLabel.setText("Number of Questions: "+exercise.getExNumOfQuestions());

        standardExerciseInnerPanel.setLayout(new GridLayout(0,1));
        standardExercise = standardExerciseService.loadStandardExercise(exercise);

        final ButtonGroup bg[] = new ButtonGroup[standardExercise.getExNumOfQuestions()];
        final Integer [] questionIdArray = new Integer[standardExercise.getExNumOfQuestions()];
        for(int i=0;i<exercise.getQuestionBank().size();i++){
            Question question = exercise.getQuestionBank().get(i);
            List<Answer> options = getOptions(question.getRightAnsList(),question.getWrongAnsList());
            questionIdArray[i] = question.getqId();
            Answer randomOption = null;
            bg[i]=new ButtonGroup();
            JRadioButton r1=new JRadioButton();
            randomOption = getRandomChoiceFromOptions(options);
            options.remove(randomOption);
            r1.setText(randomOption.getAns());
            r1.setActionCommand(randomOption.getAnsId().toString());
            JRadioButton r2=new JRadioButton();
            randomOption = getRandomChoiceFromOptions(options);
            options.remove(randomOption);
            r2.setText(randomOption.getAns());
            r2.setActionCommand(randomOption.getAnsId().toString());
            JRadioButton r3=new JRadioButton();
            randomOption = getRandomChoiceFromOptions(options);
            options.remove(randomOption);
            r3.setText(randomOption.getAns());
            r3.setActionCommand(randomOption.getAnsId().toString());
            JRadioButton r4=new JRadioButton();
            randomOption = getRandomChoiceFromOptions(options);
            options.remove(randomOption);
            r4.setText(randomOption.getAns());
            r4.setActionCommand(randomOption.getAnsId().toString());
            bg[i].add(r1);
            bg[i].add(r2);
            bg[i].add(r3);
            bg[i].add(r4);

            JPanel p=new JPanel(new GridLayout(0,1));
            JLabel jLabel=new JLabel();
            jLabel.setText("Question "+(i+1)+": "+getQuestionText(question));
            p.add(jLabel);
            p.add(r1);
            p.add(r2);
            p.add(r3);
            p.add(r4);

            standardExerciseInnerPanel.add(p);
        }
        JButton submitButton=new JButton();
        submitButton.setText("SUBMIT");
        standardExerciseOuterPanel.add(submitButton,BorderLayout.SOUTH);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                submissionId = submissionDAO.getLatestSubmissionId()+1;
                for(int i=0;i<standardExercise.getExNumOfQuestions();i++){
                    SubmissionDetails submissionDetails = new SubmissionDetails();
                    Integer ansId;
                    try {
                        ansId = Integer.parseInt(bg[i].getSelection().getActionCommand());
                    }catch(NullPointerException ne){
                        ansId = 0;
                    }
                    submissionDetails.setAnsId(ansId);
                    submissionDetails.setQuestionId(questionIdArray[i]);
                    submissionDetails.setSubmissionId(submissionId);
                    submissionDAO.saveStandardExamAttempt(submissionDetails,exercise.getExId(),user.getId());
                }
                Submission submission = new Submission();
                Integer latestAttemptNo = submissionDAO.getLatestSubmissionAttempt(exercise.getExId(),user.getId());
                submission.setSubmissionId(submissionId);
                submission.setAttemptedAt(new Timestamp(System.currentTimeMillis()));
                submission.setAttemptNo(latestAttemptNo+1);
                submission.setExerciseId(exercise.getExId());
                submission.setUserId(user.getId());
                if(submissionDAO.saveAttempt(submission)) {
                    Integer finalScore = submissionDAO.getAvgFinalScore(user.getId(),exercise.getExId(),exercise.getScoringPolicy());
                    String action = "";
                    if(submissionDAO.exists(user.getId(),exercise.getExId()))
                        action = "UPDATE";
                    else
                        action = "INSERT";
                    //if(submissionDAO.saveFinalScore(user.getId(),exercise.getExId()))
                    if(submissionDAO.saveScoreSummary(user.getId(),exercise.getExId(),finalScore,"INSERT"))
                    {
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Exercise Submitted Successfully....!!!");
                    }
                    else{
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Final score could not be saved...");
                    }
                }
                else
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame,"Error while submitting attempt...!!");
                ViewResultScreen viewResultScreen=new ViewResultScreen(user,exercise,submissionId);
            }
        });
    }

    private List<Answer> getOptions(List<Answer> rightAnswerList,List<Answer> wrongAnswerList){
        List<Answer> options = new LinkedList<Answer>();
        for (int i=0;i<3;i++){
            Answer answer = null;
            if(wrongAnswerList.size()>1){
                 answer = wrongAnswerList.get(CommonUtil.getRandomNumberInRange(0,wrongAnswerList.size()-1));
            }
            else
                answer = wrongAnswerList.get(0);
            options.add(answer);
            wrongAnswerList.remove(answer);
        }
        Answer answer = rightAnswerList.size()>1 ? rightAnswerList.get(CommonUtil.getRandomNumberInRange(0,rightAnswerList.size()-1)):rightAnswerList.get(0);
        options.add(answer);

        return options;
    }

    private Answer getRandomChoiceFromOptions(List<Answer> options){
        if(options.size()>1)
            return options.get(CommonUtil.getRandomNumberInRange(0,options.size()-1));
        else
            return options.get(0);
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
