package forms;

import dao.ExerciseDAO;
import dao.ParameterDAO;
import model.*;
import org.apache.commons.lang.StringUtils;
import util.CommonUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AddParameterSetToQuestionScreen {
    private JPanel innerParametersPanel;
    private JScrollPane parameterScrollPane;
    private JPanel parameterIDPanel;
    private JTextField parameterIDTextfield;
    private JButton addButton;
    private JPanel addParameterSetToQuestionPanel;
    private User user;
    private Exercise exercise;
    private String courseId;
    private Map<Integer,List<Parameter>> parameterSetMap;
    private ParameterDAO parameterDAO;
    private ExerciseDAO exerciseDAO;

    public AddParameterSetToQuestionScreen(final User user, final Exercise exercise, final String courseId, final Question question) {

        this.user = user;
        this.courseId = courseId;
        this.exercise = exercise;
        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(550, 450);
        FirstScreen.firstFrame.setContentPane(addParameterSetToQuestionPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);
        innerParametersPanel.setLayout(new GridLayout(0,1));
        parameterDAO = new ParameterDAO();

        parameterSetMap = getParameterSetList(question.getqId());

        exerciseDAO = new ExerciseDAO();

        for(Integer parameterSetId:parameterSetMap.keySet()){
            JPanel p=new JPanel();
            p.setLayout(new GridLayout(0,2));
            JLabel jLabel=new JLabel();
            jLabel.setText(parameterSetId+"");
            JTextField textField=new JTextField();
            textField.setText(getParamStringFromList(parameterSetMap.get(parameterSetId)));
            textField.setEditable(false);
            p.add(jLabel);
            p.add(textField);
            innerParametersPanel.add(p);
        }


        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!CommonUtil.isValidString(parameterIDTextfield.getText()))
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Parameter ID cannot be left blank");
                else{
                    try {
                        Integer parameterSetId = Integer.parseInt(parameterIDTextfield.getText().trim());
                        if(exerciseDAO.addQuestionToExercise(question.getqId(),exercise.getExId(),parameterSetId)){
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Question added successfully to exercise.");
                            AddRemoveQuestionsFromExerciseScreen addRemoveQuestionsFromExerciseScreen
                                    = new AddRemoveQuestionsFromExerciseScreen(courseId,user,exercise);
                        }
                        else {
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Error while adding question to exercise..");
                        }
                    } catch (NumberFormatException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter Valid Parameter ID");
                    }
                    catch (Exception e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Error while adding question to exercise.");
                    }
                }
            }
        });
    }

    private Map<Integer,List<Parameter>> getParameterSetList(Integer questionId){
        Map<Integer,List<Parameter>> paramterSetMap = new LinkedHashMap<Integer, List<Parameter>>();
        List<Integer> parameterSetIds = parameterDAO.getParameterSetIdsForQuestion(questionId);
        for(Integer id: parameterSetIds){
            List<Parameter> parameterList = parameterDAO.getParameterList(id);
            if(parameterList!=null && !parameterList.isEmpty())
                paramterSetMap.put(id,parameterList);
        }

        return paramterSetMap;
    }

    private String getParamStringFromList(List<Parameter> parameterList){
        StringBuilder paramBuilder = new StringBuilder();
        int i=0;
        for(Parameter parameter : parameterList){
            paramBuilder.append(parameter.getParamValue());
            if(i!=parameterList.size()-1)
                paramBuilder.append(", ");
        }

        return paramBuilder.toString();
    }
}
