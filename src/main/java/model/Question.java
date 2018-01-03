package model;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.util.List;

public class Question {
    private Integer qId;
    private String text;
    private String type;
    private Integer difficulty;
    private String hint;
    private String detailedSolution;
    private Topic topic;
    private List<Parameter> parameterList;
    private List<Answer> rightAnsList;
    private List<Answer> wrongAnsList;
    private Integer paramSetId;

    public Question() {
    }

   public Integer getqId() {return qId;}
   public String getText() {return text;}
   public String getType() {return type;}
   public Integer getDifficulty() {return difficulty;}
   public String getHint() {return hint;}

   public void setqId(Integer qId) {this.qId=qId;}
    public void setText(String text) {this.text = text;}
    public void setType(String type) {this.type = type;}
    public void setqDifficulty(int difficulty) {this.difficulty = difficulty;}
    public void setqHint(String hint) {this.hint = hint;}

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getDetailedSolution() {
        return detailedSolution;
    }

    public void setDetailedSolution(String detailedSolution) {
        this.detailedSolution = detailedSolution;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public List<Answer> getRightAnsList() {
        return rightAnsList;
    }

    public void setRightAnsList(List<Answer> rightAnsList) {
        this.rightAnsList = rightAnsList;
    }

    public List<Answer> getWrongAnsList() {
        return wrongAnsList;
    }

    public void setWrongAnsList(List<Answer> wrongAnsList) {
        this.wrongAnsList = wrongAnsList;
    }

    public List<Parameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<Parameter> parameterList) {
        this.parameterList = parameterList;
    }

    public Integer getParamSetId() {
        return paramSetId;
    }

    public void setParamSetId(Integer paramSetId) {
        this.paramSetId = paramSetId;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
