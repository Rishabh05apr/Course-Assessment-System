package model;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by pinak on 10/28/2017.
 */
public class Exercise {
    private Integer exId;
    private Timestamp startDate;
    private Timestamp endDate;
    private String exName;
    private Integer exAttempts;
    private Integer exNumOfQuestions;
    private Integer pointsForIncorrectAns;
    private Integer pointsForCorrectAns;
    private String scoringPolicy;
    private String mode;
    private String course;
    private List<Question> questionBank;

    public Exercise() {
    }

    public Integer getExId() {
        return exId;
    }

    public void setExId(Integer exId) {
        this.exId = exId;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName = exName;
    }

    public Integer getExAttempts() {
        return exAttempts;
    }

    public void setExAttempts(Integer exAttempts) {
        this.exAttempts = exAttempts;
    }

    public Integer getExNumOfQuestions() {
        return exNumOfQuestions;
    }

    public void setExNumOfQuestions(Integer exNumOfQuestions) {
        this.exNumOfQuestions = exNumOfQuestions;
    }

    public Integer getPointsForIncorrectAns() {
        return pointsForIncorrectAns;
    }

    public void setPointsForIncorrectAns(Integer pointsForIncorrectAns) {
        this.pointsForIncorrectAns = pointsForIncorrectAns;
    }

    public Integer getPointsForCorrectAns() {
        return pointsForCorrectAns;
    }

    public void setPointsForCorrectAns(Integer pointsForCorrectAns) {
        this.pointsForCorrectAns = pointsForCorrectAns;
    }

    public String getScoringPolicy() {
        return scoringPolicy;
    }

    public void setScoringPolicy(String scoringPolicy) {
        this.scoringPolicy = scoringPolicy;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<Question> getQuestionBank() {
        return questionBank;
    }

    public void setQuestionBank(List<Question> questionBank) {
        this.questionBank = questionBank;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }


}
