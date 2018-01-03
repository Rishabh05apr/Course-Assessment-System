package forms;

import dao.AnswerDAO;
import dao.ParameterDAO;
import dao.QuestionDAO;
import model.*;
import org.apache.commons.lang.StringUtils;
import util.CommonUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class AddParametersAndAnswersScreen {
    private JButton saveButton;
    private JButton backButton;
    private JTextArea parametersTextArea;
    private JPanel rightAnswersPanel;
    private JPanel addParametersPanel;
    private JPanel wrongAnswersPanel;
    private JTextArea wrongAnswersTextArea;
    private JTextArea rightAnswersTextArea;
    private JButton addNewParametersButton;
    private JTextArea questionTextArea;
    private JScrollPane wrongAnswersScrollPane;
    private JScrollPane rightAnswersScrollPane;
    private JScrollPane parametersScrollPane;
    private JScrollPane questionScrollPane;
    private JPanel questionTextPanel;
    private JPanel parametersPanel;
    private JLabel questionLabel;
    private User user;
    private Question question;
    private ParameterDAO parametersDAO;
    private QuestionDAO questionDAO;
    private AnswerDAO ansDAO;



    public AddParametersAndAnswersScreen(final User user, final Question ques) {

        this.user = user;
        this.question = ques;

        this.ansDAO = new AnswerDAO();
        this.questionDAO = new QuestionDAO();
        this.parametersDAO = new ParameterDAO();

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(750, 650);
        FirstScreen.firstFrame.setContentPane(addParametersPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);


        questionTextArea.setLineWrap(true);
        parametersTextArea.setLineWrap(true);
        rightAnswersTextArea.setLineWrap(true);
        wrongAnswersTextArea.setLineWrap(true);

        questionTextArea.setEditable(false);
        questionTextArea.setText(ques.getText());

        if (ques.getType().equalsIgnoreCase("Fixed")) {
            parametersTextArea.setEnabled(false);
            addNewParametersButton.setEnabled(false);
        }

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!CommonUtil.isValidString(rightAnswersTextArea.getText()) || !CommonUtil.isValidString(wrongAnswersTextArea.getText()))
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Answers cannot be left blank..");
                else if(question.getType().equalsIgnoreCase("PARAMETERIZED")
                        && StringUtils.countMatches(question.getText(),"<param>")!= (StringUtils.countMatches(parametersTextArea.getText(),"\n")+1)){
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please enter correct number of parameters..");
                }
                else{
                    if(question.getType().equalsIgnoreCase("FIXED")){
                        saveFixedQuestionDetails();
                    }
                    else if(question.getType().equalsIgnoreCase("PARAMETERIZED"))
                        saveParamQuestionDetails();
                    else
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Invalid question type....");
                    CreateQuestionScreen cqs=new CreateQuestionScreen(user);
                }
            }
        });

        addNewParametersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                saveParamQuestionDetails();
                rightAnswersTextArea.setText("");
                parametersTextArea.setText("");
                wrongAnswersTextArea.setText("");
            }
        });
    }
    private void saveFixedQuestionDetails(){
        try {
            List<Answer> answerList = extractAnswerList();
            ansDAO.saveAnswer(answerList);
            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Answers saved successfully..");
            CreateQuestionScreen createQuestionScreen = new CreateQuestionScreen(user);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Error while saving answers : "+e.getMessage());
        }
    }

    private void saveParamQuestionDetails(){
            String paramsArray[]=parametersTextArea.getText().split("\n");
            int paramsetId= parametersDAO.getLatestParamSetId()+1;
            List<Parameter> parameterList = new LinkedList<Parameter>();
            for(int i=0;i<paramsArray.length;i++){
                Parameter parameter = new Parameter();
                parameter.setParamSetId(paramsetId);
                parameter.setParamId(i+1);
                parameter.setQuestionId(question.getqId());
                parameter.setParamValue(paramsArray[i]);

                parameterList.add(parameter);
            }
            if(parametersDAO.saveParamters(parameterList)){
                //extract answers
                List<Answer> answerList = extractAnswerList();
                if(ansDAO.saveAnswer(answerList))
                    parametersDAO.saveAnsParamMapping(paramsetId,answerList);
                else {
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Could not save answers....");
                }
            }
            else
                JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Could not save parameters....");
            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Successfully saved parameters and answers...");
    }
    private List<Answer> extractAnswerList(){
        String [] rightAnsArray = rightAnswersTextArea.getText().split("\n");
        String [] wrongAnsArray = wrongAnswersTextArea.getText().split("\n");

        Integer ansId = ansDAO.getLatestAnswerId()+1;
        List<Answer> answerList = new LinkedList<Answer>();
        for(int i=0;i<rightAnsArray.length;i++){
            Answer answer = new Answer();
            answer.setAns(rightAnsArray[i].split("~")[0]);
            answer.setShortExplaination(rightAnsArray[i].split("~")[1]);
            answer.setCategory("CORRECT");
            answer.setqId(question.getqId());
            answer.setAnsId(ansId++);
            //ansDAO.saveAnswer(answer);
            answerList.add(answer);
        }
        for(int i=0;i<wrongAnsArray.length;i++){
            Answer answer = new Answer();
            answer.setAns(wrongAnsArray[i].split("~")[0]);
            answer.setShortExplaination(wrongAnsArray[i].split("~")[1]);
            answer.setCategory("INCORRECT");
            answer.setqId(question.getqId());
            answer.setAnsId(ansId++);
            //ansDAO.saveAnswer(answer);
            answerList.add(answer);
        }
        return answerList;
    }
}
