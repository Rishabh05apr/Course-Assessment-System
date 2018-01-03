package forms;

import model.Question;
import model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewQuestionScreen {
    private JButton backButton;
    private JTextField idTextfield;
    private JTextField typeTextfield;
    private JTextField difficultyTextfield;
    private JTextArea questionTextArea;
    private JTextArea solutionTextArea;
    private JLabel questionidLabel;
    private JLabel questionTypeLabel;
    private JLabel questionTextLabel;
    private JLabel difficultyLabel;
    private JLabel detailedSolutionLabel;
    private JPanel textfieldPanel;
    private JScrollPane sc1;
    private JScrollPane sc2;
    private JPanel labelPanel;
    private JPanel viewQuestionPanel;

    public ViewQuestionScreen(final User user, Question qId) {

        FirstScreen.firstFrame.getContentPane().removeAll();
        FirstScreen.firstFrame.setSize(500, 500);
        FirstScreen.firstFrame.setContentPane(viewQuestionPanel);
        FirstScreen.firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FirstScreen.firstFrame.setVisible(true);

        idTextfield.setText(qId.getqId()+"");
        typeTextfield.setText(qId.getType());
        difficultyTextfield.setText(qId.getDifficulty()+"");
        solutionTextArea.setText(qId.getDetailedSolution());
        questionTextArea.setText(qId.getText());
        idTextfield.setEditable(false);
        typeTextfield.setEditable(false);
        difficultyTextfield.setEditable(false);
        solutionTextArea.setEditable(false);
        questionTextArea.setEditable(false);


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewAddQuestionBankScreen viewAddQuestionBankScreen=new ViewAddQuestionBankScreen(user);
            }
        });
    }
}
