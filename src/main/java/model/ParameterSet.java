package model;

import java.util.Map;

public class ParameterSet {
    private Integer paramSetId;
    private Map<Integer, String> params;
    private Integer questionId;

    public ParameterSet() {
    }

    public Integer getParamSetId() {
        return paramSetId;
    }

    public void setParamSetId(Integer paramSetId) {
        this.paramSetId =   paramSetId;
    }

    public Map<Integer, String> getParams() {
        return params;
    }

    public void setParams(Map<Integer, String> params) {
        this.params = params;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
}
