package service;

import dao.AnswerDAO;
import dao.ExerciseDAO;
import dao.ParameterDAO;
import dao.QuestionDAO;
import model.Exercise;
import model.Question;
import util.CommonUtil;


import java.util.List;

public class AdaptiveExerciseService {
    private ExerciseDAO exerciseDAO;
    private ParameterDAO parameterDAO;
    private AnswerDAO answerDAO;
    public AdaptiveExerciseService() {
        exerciseDAO = new ExerciseDAO();
        parameterDAO = new ParameterDAO();
        answerDAO = new AnswerDAO();
    }

    public Question loadAnswers(Question question) {


        if(question.getType().equalsIgnoreCase("FIXED")) {
            question.setRightAnsList(answerDAO.getAnswerListForQuestion(question.getqId(), "CORRECT"));
            question.setWrongAnsList(answerDAO.getAnswerListForQuestion(question.getqId(), "INCORRECT"));
        }
        else if(question.getType().equalsIgnoreCase("PARAMETERIZED")) {

            Integer paramSetId = 0;
            List<Integer> paramSetIdList = parameterDAO.getParameterSetIdsForQuestion(question.getqId());
            paramSetId = paramSetIdList.get(CommonUtil.getRandomNumberInRange(0,paramSetIdList.size()-1));
            question.setParameterList(parameterDAO.getParameterList(paramSetId));
            question.setRightAnsList(answerDAO.getAnswerListForQuestionWithParameter(question.getqId(), "CORRECT", paramSetId));
            question.setWrongAnsList(answerDAO.getAnswerListForQuestionWithParameter(question.getqId(), "INCORRECT", paramSetId));
        }
    return question;
    }
}
