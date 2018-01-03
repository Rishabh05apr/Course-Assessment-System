package forms;

import dao.ExerciseDAO;
import dao.ParameterDAO;
import dao.QuestionDAO;
import dao.TopicDAO;
import model.*;
import util.CommonUtil;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

public class AddQuestionsToExerciseScreen {
    private JTextField questionIdTextField;
    private JButton addButton;
    private JButton backButton;
    private JTable questionsListTable;
    private JLabel questionIdLabel;
    private JPanel buttonPanel;
    private JScrollPane questionListScrollPane;
    private JPanel questionListPanel;
    private JPanel addQuestionsToExerciseScreenPanel;
    private JComboBox topicComboBox;
    private Exercise exercise;
    private ExerciseDAO exerciseDAO;
    private List<Question> questions;
    private Object data[][];
    private DefaultTableModel model;
    private User user;
    private String courseId;
    private TopicDAO topicDAO;
    private List<Topic> topicList;
    private ParameterDAO parameterDAO;
    private QuestionDAO questionDAO;

    public AddQuestionsToExerciseScreen(final User user, final Exercise exercise, final String courseId) {

        this.user = user;
        this.courseId = courseId;
        this.exercise = exercise;
        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(550, 450);
        FirstScreen.firstFrame.setContentPane(addQuestionsToExerciseScreenPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);
        exerciseDAO = new ExerciseDAO();
        questionDAO = new QuestionDAO();
        topicDAO = new TopicDAO();
        parameterDAO = new ParameterDAO();


        List<Topic> topicList = topicDAO.getAllTopicsForCourse(courseId);

        Topic allTopics = new Topic();
        allTopics.setTopicName("ALL");
        topicList.add(0,allTopics);


        topicComboBox.setModel(new DefaultComboBoxModel(topicList.toArray()));
        topicComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Topic) {
                    Topic topic = (Topic) value;
                    setText(topic.getTopicName());
                }
                return this;
            }
        });

        refreshQuestionTable(courseId);

        topicComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshQuestionTable(courseId);
            }
        });
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!CommonUtil.isValidString(questionIdTextField.getText()))
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Question Id cannot be left blank...");
                else{
                    Integer questionId = Integer.parseInt(questionIdTextField.getText());
                    Question question = questionDAO.getQuestionDetails(questionId);
                    if(question.getType().equalsIgnoreCase("FIXED")){
                        if(exerciseDAO.addQuestionToExercise(questionId,exercise.getExId(),null)) {
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Question added to exercise......");
                            AddRemoveQuestionsFromExerciseScreen addRemoveQuestionsFromExerciseScreen = new AddRemoveQuestionsFromExerciseScreen(courseId, user, exercise);
                        }
                        else{
                            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Error while adding question....");
                        }
                    }
                    else{
                        List<Parameter> parameterList = parameterDAO.getParametersForQuestion(questionId);
                        question.setParameterList(parameterList);
                        AddParameterSetToQuestionScreen addParameterSetToQuestionScreen = new AddParameterSetToQuestionScreen(user,exercise,courseId,question);
                    }
                }

            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddRemoveQuestionsFromExerciseScreen addRemoveQuestionsFromExerciseScreen = new AddRemoveQuestionsFromExerciseScreen(courseId,user,exercise);
            }
        });
    }

    private void refreshQuestionTable(String courseId) {
        Topic selectedTopic = (Topic)topicComboBox.getSelectedItem();
        List<Question> questionList = questionDAO.getQuestionBankForTopic(selectedTopic.getTopicName(),courseId);
        String[] columnNames = {"Question Id", "Text", "Type", "Difficulty", "Hint"};
        model = new DefaultTableModel(convertListtoObject(questionList), columnNames){

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        questionsListTable.setModel(model);
    }

    private Object[][] convertListtoObject(List<Question> questions)
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

