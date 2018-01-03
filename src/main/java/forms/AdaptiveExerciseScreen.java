package forms;

import dao.AnswerDAO;
import dao.QuestionDAO;
import dao.SubmissionDAO;
import model.*;
import service.AdaptiveExerciseService;
import service.StandardExerciseService;
import util.CommonUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AdaptiveExerciseScreen {
    private JPanel labelPanel;
    private JLabel exerciseNameLabel;
    private JLabel correctPointLabel;
    private JLabel exerciseModeLabel;
    private JLabel numberOfQuestionsLabel;
    private JLabel incorrectPointLabel;
    private JButton nextButton;
    private JButton submitButton;
    private JPanel adaptiveExercisePanel;
    private JPanel labelOuterPanel;
    private JPanel questionOuterPanel;
    private JScrollPane questionScrollPane;
    private JPanel questionInnerPanel;
    private JPanel buttonPanel;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JRadioButton radioButton4;
    private JLabel questionLabel;
    private List<Integer> questionLevels;
    private QuestionDAO questionDAO;
    private AdaptiveExerciseService adaptiveExerciseService;
    private SubmissionDAO submissionDAO;
    private Integer submissionId;
    private ButtonGroup bg;
    private Exercise exercise;
    private User user;
    private Question firstQuestion;
    private AnswerDAO answerDAO;
    private List<Integer> qIdList;
    private int firstQuestionLevel;
    private int numberOfQuestions;
    private int count;

    public AdaptiveExerciseScreen(final User user, final Exercise exercise) {

        this.user=user;
        this.exercise=exercise;

        qIdList=new ArrayList<Integer>();
        numberOfQuestions=exercise.getExNumOfQuestions();
        count=1;
        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(1000, 650);
        FirstScreen.firstFrame.setContentPane(adaptiveExercisePanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);

        questionDAO= new QuestionDAO();
        submissionDAO = new SubmissionDAO();
        answerDAO=new AnswerDAO();

        correctPointLabel.setText("Points Per Correct answer: "+exercise.getPointsForCorrectAns());
        incorrectPointLabel.setText("Points Per Wrong answer: "+exercise.getPointsForIncorrectAns());
        exerciseNameLabel.setText("Exercise Name: "+exercise.getExName());
        exerciseModeLabel.setText("Exercise Mode: "+exercise.getMode());
        numberOfQuestionsLabel.setText("Number of Questions: "+exercise.getExNumOfQuestions());
        questionInnerPanel.setLayout(new GridLayout(0,1));
        submissionId = submissionDAO.getLatestSubmissionId()+1;

        questionLevels=questionDAO.getAvailableQuestionLevels();
        adaptiveExerciseService=new AdaptiveExerciseService();

        firstQuestionLevel=getFirstQuestionLevel(questionLevels);
        firstQuestion=getFirstQuestion(firstQuestionLevel);
        firstQuestion=adaptiveExerciseService.loadAnswers(firstQuestion);
        exercise.setQuestionBank(new ArrayList<Question>());
        exercise.getQuestionBank().add(firstQuestion);
        List<Answer> options = getOptions(firstQuestion.getRightAnsList(),firstQuestion.getWrongAnsList());

        qIdList.add(firstQuestion.getqId());

        bg = new ButtonGroup();

        questionLabel.setText("Question "+(1)+": "+getQuestionText(firstQuestion));
        Answer randomOption = null;
        randomOption = getRandomChoiceFromOptions(options);
        options.remove(randomOption);
        radioButton1.setText(randomOption.getAns());
        radioButton1.setActionCommand(randomOption.getAnsId().toString());

        randomOption = getRandomChoiceFromOptions(options);
        options.remove(randomOption);
        radioButton2.setText(randomOption.getAns());
        radioButton2.setActionCommand(randomOption.getAnsId().toString());

        randomOption = getRandomChoiceFromOptions(options);
        options.remove(randomOption);
        radioButton3.setText(randomOption.getAns());
        radioButton3.setActionCommand(randomOption.getAnsId().toString());

        randomOption = getRandomChoiceFromOptions(options);
        options.remove(randomOption);
        radioButton4.setText(randomOption.getAns());
        radioButton4.setActionCommand(randomOption.getAnsId().toString());


        bg.add(radioButton1);
        bg.add(radioButton2);
        bg.add(radioButton3);
        bg.add(radioButton4);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SubmissionDetails submissionDetails = new SubmissionDetails();
                Integer ansId;
                try {
                    ansId = Integer.parseInt(bg.getSelection().getActionCommand());
                } catch (NullPointerException ne) {
                    ansId = 0;
                }
                submissionDetails.setAnsId(ansId);
                submissionDetails.setQuestionId(firstQuestion.getqId());
                submissionDetails.setSubmissionId(submissionId);
                submissionDAO.saveStandardExamAttempt(submissionDetails, exercise.getExId(), user.getId());

                Submission submission = new Submission();
                Integer latestAttemptNo = submissionDAO.getLatestSubmissionAttempt(exercise.getExId(),user.getId());
                submission.setSubmissionId(submissionId);
                submission.setAttemptedAt(new Timestamp(System.currentTimeMillis()));
                submission.setAttemptNo(latestAttemptNo+1);
                submission.setExerciseId(exercise.getExId());
                submission.setUserId(user.getId());
                if(submissionDAO.saveAttempt(submission)) {
                    if(submissionDAO.saveFinalScore(user.getId(),exercise.getExId())) {
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
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                count++;
                if(count==numberOfQuestions){
                    nextButton.setEnabled(false);
                }
                SubmissionDetails submissionDetails = new SubmissionDetails();
                Integer ansId;
                try {
                    ansId = Integer.parseInt(bg.getSelection().getActionCommand());
                } catch (NullPointerException ne) {
                    ansId = 0;
                }
                submissionDetails.setAnsId(ansId);
                submissionDetails.setQuestionId(firstQuestion.getqId());
                submissionDetails.setSubmissionId(submissionId);
                submissionDAO.saveStandardExamAttempt(submissionDetails, exercise.getExId(), user.getId());

                Answer answer= answerDAO.getAnswer(ansId);


                if(answer.getCategory().equalsIgnoreCase("CORRECT"))
                    firstQuestion=getNextQuestion(true,firstQuestionLevel);
                else
                    firstQuestion=getNextQuestion(false,firstQuestionLevel);

                firstQuestionLevel=firstQuestion.getDifficulty();

                setNextQuestion(firstQuestion);
            }
        });
    }

    private Answer getRandomChoiceFromOptions(List<Answer> options){
        if(options.size()>1)
            return options.get(CommonUtil.getRandomNumberInRange(0,options.size()-1));
        else
            return options.get(0);
    }

    private void setNextQuestion(Question question){

        question=adaptiveExerciseService.loadAnswers(question);
        exercise.getQuestionBank().add(question);

        bg.clearSelection();

        List<Answer> options = getOptions(question.getRightAnsList(),question.getWrongAnsList());

        qIdList.add(question.getqId());

        questionLabel.setText("Question "+(count)+": "+getQuestionText(question));
        Answer randomOption = null;
        randomOption = getRandomChoiceFromOptions(options);
        options.remove(randomOption);
        radioButton1.setText(randomOption.getAns());
        radioButton1.setActionCommand(randomOption.getAnsId().toString());

        randomOption = getRandomChoiceFromOptions(options);
        options.remove(randomOption);
        radioButton2.setText(randomOption.getAns());
        radioButton2.setActionCommand(randomOption.getAnsId().toString());

        randomOption = getRandomChoiceFromOptions(options);
        options.remove(randomOption);
        radioButton3.setText(randomOption.getAns());
        radioButton3.setActionCommand(randomOption.getAnsId().toString());

        randomOption = getRandomChoiceFromOptions(options);
        options.remove(randomOption);
        radioButton4.setText(randomOption.getAns());
        radioButton4.setActionCommand(randomOption.getAnsId().toString());


    }

    private Question getNextQuestion(boolean iscorrect,int difficultylevel){
        List<Question> questionList;
        if(difficultylevel!=1 && difficultylevel!=6){
            if(iscorrect){
                difficultylevel+=1;
                questionList=questionDAO.getQuestionForDifficultyLevel(difficultylevel,qIdList);
            }else {
                difficultylevel-=1;
                questionList=questionDAO.getQuestionForDifficultyLevel(difficultylevel,qIdList);
            }
        }
        else
            questionList=questionDAO.getQuestionForDifficultyLevel(difficultylevel,qIdList);

        firstQuestionLevel=difficultylevel;
        return questionList.get(CommonUtil.getRandomNumberInRange(0,questionList.size()-1));
    }

    private List<Answer> getOptions(List<Answer> rightAnswerList, List<Answer> wrongAnswerList){
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

    private int getFirstQuestionLevel(List<Integer> questionLevels){
        int size=questionLevels.size();
        Collections.sort(questionLevels);
        System.out.print(questionLevels.size()+":size");
        if(size%2==0)
            return questionLevels.get((size/2)-1);
        else
            return questionLevels.get(size/2);
    }

    private Question getFirstQuestion(int questionLevel){
        List<Question> questionList=questionDAO.getQuestionForDifficultyLevel(questionLevel,qIdList);
        return questionList.get(CommonUtil.getRandomNumberInRange(0,questionList.size()-1));
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
