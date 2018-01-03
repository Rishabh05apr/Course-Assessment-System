package forms;

import dao.CourseDAO;
import dao.QuestionDAO;
import dao.TopicDAO;
import model.Course;
import model.Question;
import model.Topic;
import util.CommonUtil;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

public class CreateQuestionScreen implements ActionListener {
    private JRadioButton fixedRadioButton;
    private JRadioButton parameterizedRadioButton;
    private JTextField questionIDTextfield;
    private JTextField hintTextfield;
    private JTextField difficultyLevelTextfield;
    private JTextField topicTextfield;
    private JTextArea textTextArea;
    private JButton addButton;
    private JButton backButton;
    private JLabel questionIDLabel;
    private JLabel topicLabel;
    private JLabel difficultyLevelLabel;
    private JLabel hintLabel;
    private JLabel textLabel;
    private JPanel textAreaPanel;
    private JPanel label2Panel;
    private JPanel textfieldPanel;
    private JPanel buttonPanel;
    private JPanel label1Panel;
    private JPanel createQuestionPanel;
    private JComboBox topicComboBox;
    private JComboBox difficultyLevelComboBox;
    private JScrollPane textScrollPane;
    private JLabel courseIDLabel;
    private JComboBox courseIDComboBox;
    private JTextArea detailedSolutionTextArea;
    private JLabel detailedSolutionLabel;
    private JScrollPane detailedScrollPane;
    private JTextField newTextfield;
    private User user;
    private CourseDAO cdao;
    private TopicDAO tdao;
    private QuestionDAO qdao;

    public CreateQuestionScreen(final User user) {

        this.user = user;

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(550, 450);
        FirstScreen.firstFrame.setContentPane(createQuestionPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);
        ButtonGroup bg=new ButtonGroup();
        bg.add(fixedRadioButton);
        bg.add(parameterizedRadioButton);
        fixedRadioButton.setSelected(true);
        textTextArea.setLineWrap(true);

        cdao=new CourseDAO();
        tdao=new TopicDAO();
        qdao=new QuestionDAO();

        questionIDTextfield.setEditable(false);
        questionIDTextfield.setText((qdao.getLatestQuestionId()+1)+"");


        List<Course> courseList=cdao.getCourseList(user.getUserName());
        Iterator itr=courseList.iterator();

        while (itr.hasNext()){
            Course course=(Course) itr.next();
            courseIDComboBox.addItem(course.getCourseID());
        }
        String firstCourse="";
            if(courseList != null) {
                firstCourse = courseList.get(0).getCourseID();

              /*  List<Topic> topicList = tdao.getAllTopicsForCourse(firstCourse);
                topicComboBox.setModel(new DefaultComboBoxModel(topicList.toArray()));
                topicComboBox.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        if(value instanceof Topic){
                            Topic topic = (Topic) value;
                            setText(topic.getTopicName());
                        }
                        return this;
                    }
                } );
                itr = topicList.iterator();*/

                /*while (itr.hasNext()) {
                    Topic topic = (Topic) itr.next();
                    topicComboBox.addItem(topic.getTopicName());
                }*/
            }
        int diffLevel[]={1,2,3,4,5,6};
        for(int i=0;i<diffLevel.length;i++){
            difficultyLevelComboBox.addItem(diffLevel[i]);
        }

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewAddQuestionBankScreen va=new ViewAddQuestionBankScreen(user);
            }
        });
        addButton.addActionListener(this);

       /* courseIDComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String course = (String)cb.getSelectedItem();

                topicComboBox.removeAllItems();
                List<Topic> topicList=tdao.getAllTopicsForCourse(course);

                Iterator itr=topicList.iterator();

                while (itr.hasNext()){
                    Topic topic=(Topic) itr.next();
                    topicComboBox.addItem(topic.getTopicName());
                }
            }
        });*/
    }
    public void actionPerformed(ActionEvent e) {
          if(!CommonUtil.isValidString(textTextArea.getText())||!CommonUtil.isValidString(detailedSolutionTextArea.getText())) {
            JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please Enter All Details");
        }
        else {
            Question ques=new Question();

            ques.setqId(Integer.parseInt(questionIDTextfield.getText()));
            ques.setqDifficulty((Integer) difficultyLevelComboBox.getSelectedItem());
            ques.setqHint(hintTextfield.getText());
            ques.setText(textTextArea.getText());
            Topic topic = new Topic();
            topic.setTopicId(Integer.parseInt(newTextfield.getText()));
            ques.setTopic(topic);
            ques.setDetailedSolution(detailedSolutionTextArea.getText());

            if(fixedRadioButton.isSelected())
                ques.setType("FIXED");
            else
                ques.setType("PARAMETERIZED");

            // Saving the question
            if(qdao.saveQuestion(ques)) {
                AddParametersAndAnswersScreen aps = new AddParametersAndAnswersScreen(user, ques);
            }
            else{
                JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Question could not be added...!!");
            }
        }
    }
}

