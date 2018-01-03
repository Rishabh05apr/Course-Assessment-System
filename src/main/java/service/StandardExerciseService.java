package service;

import dao.AnswerDAO;
import dao.ExerciseDAO;
import dao.QuestionDAO;
import model.Exercise;
import model.Parameter;
import model.Question;

import java.util.List;

public class StandardExerciseService {

    public Exercise loadStandardExercise(Exercise exercise){
        ExerciseDAO exerciseDAO = new ExerciseDAO();
        AnswerDAO answerDAO = new AnswerDAO();
        QuestionDAO questionDAO = new QuestionDAO();

        List<Question> questionBank = exerciseDAO.getQuestionsForExercise(exercise.getExId());
        if(questionBank!=null){
            for(Question question: questionBank){
                if(question.getType().equalsIgnoreCase("FIXED")) {
                    question.setRightAnsList(answerDAO.getAnswerListForQuestion(question.getqId(), "CORRECT"));
                    question.setWrongAnsList(answerDAO.getAnswerListForQuestion(question.getqId(), "INCORRECT"));
                }
                else if(question.getType().equalsIgnoreCase("PARAMETERIZED")) {
                    question.setParameterList(exerciseDAO.getParametersForQuestionInExam(exercise.getExId(), question.getqId()));
                    question.setRightAnsList(answerDAO.getAnswerListForQuestionWithParameter(question.getqId(), "CORRECT", question.getParamSetId()));
                    question.setWrongAnsList(answerDAO.getAnswerListForQuestionWithParameter(question.getqId(), "INCORRECT", question.getParamSetId()));
                }
            }
        }
        exercise.setQuestionBank(questionBank);
        return exercise;
    }
}
