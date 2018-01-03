package model;

public class Answer {
    private Integer ansId;
    private Integer qId;
    private String ans;
    private String shortExplaination;
    private String category;

    public Answer() {
    }

    public Integer getAnsId() {
        return ansId;
    }

    public void setAnsId(Integer ansId) {
        this.ansId = ansId;
    }

    public Integer getqId() {
        return qId;
    }

    public void setqId(Integer qId) {
        this.qId = qId;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getShortExplaination() {
        return shortExplaination;
    }

    public void setShortExplaination(String shortExplaination) {
        this.shortExplaination = shortExplaination;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
