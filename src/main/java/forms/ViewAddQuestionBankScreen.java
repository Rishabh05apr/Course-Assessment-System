package forms;

import dao.QuestionDAO;
import dao.TopicDAO;
import model.Parameter;
import model.Question;
import model.Topic;
import model.User;
import util.CommonUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

public class ViewAddQuestionBankScreen {
    private JTable questionListTable;
    private JTextField questionIdTextField;
    private JButton searchButton;
    private JButton addNewQuestionButton;
    private JButton backButton;
    private JLabel questionIdLabel;
    private JPanel buttonPanel;
    private JPanel button1Panel;
    private JScrollPane questionListScrollPane;
    private JPanel viewAddQuestionPanel;
    private JPanel tablePanel;
    private JComboBox topicComboBox;
    private User user;
    private List<Question> questionList;
    private QuestionDAO questionDAO;
    private DefaultTableModel model;
    private List<Topic> topicList;
    private TopicDAO topicDAO;


    public ViewAddQuestionBankScreen(final User user) {

        this.user = user;
        questionDAO=new QuestionDAO();
        topicDAO= new TopicDAO();
        final String topicName="ALL";

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(400, 450);
        FirstScreen.firstFrame.setContentPane(viewAddQuestionPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);

        questionList=questionDAO.getAllQuestions(user.getUserName());


        topicList = topicDAO.getAllTopicsForAProf(user.getUserName());

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


        refreshQuestionTable();

        addNewQuestionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateQuestionScreen aqs=new CreateQuestionScreen(user);
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InstructorScreen is=new InstructorScreen(user);
            }
        });
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String qId=questionIdTextField.getText();
                if (!CommonUtil.isValidString(qId)) {
                    JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please enter a valid question id");
                } else {
                    try {
                        Question question = questionDAO.getQuestionDetails(Integer.parseInt(qId));
                        ViewQuestionScreen viewQuestionScreen = new ViewQuestionScreen(user,question);
                    }
                    catch(Exception ee){
                        JOptionPane.showMessageDialog(FirstScreen.firstFrame, "Please enter a valid question id");
                    }

                }

            }
        });



        topicComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                refreshQuestionTable();

                }

        });

    }

    private void refreshQuestionTable() {
        Topic selectedTopic = (Topic)topicComboBox.getSelectedItem();
        List<Question> questionList = questionDAO.getAllQuestionsForATopic(selectedTopic.getTopicName(),user.getUserName());
        String[] columnNames = {"Question Id", "Text", "Type", "Difficulty", "Hint"};
        model = new DefaultTableModel(convertListtoObject(questionList), columnNames){

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        questionListTable.setModel(model);
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
