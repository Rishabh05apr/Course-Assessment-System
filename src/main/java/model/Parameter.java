package model;

public class Parameter {
    private Integer paramSetId;
    private Integer questionId;
    private Integer paramId;
    private String paramValue;

    public Parameter() {
    }

    public Integer getParamSetId() {
        return paramSetId;
    }

    public void setParamSetId(Integer paramSetId) {
        this.paramSetId = paramSetId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getParamId() {
        return paramId;
    }

    public void setParamId(Integer paramId) {
        this.paramId = paramId;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
