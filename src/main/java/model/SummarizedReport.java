package model;

import java.sql.Timestamp;

public class SummarizedReport {

    private Integer userId;
    private String firstName;
    private String lastName;
    private Integer exerciseId;
    //private Integer attemptNo;
    //private Integer score;
    private Integer finalScore;
    private String scorePolicy;
    //private Timestamp attemptedAt;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer uid) {
        this.userId = uid;
    }

    public String getFirstName() {
     return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getFinalScore() {
        return finalScore;
    }

    public String getScorePolicy() {
        return scorePolicy;
    }
    // public Integer getAttemptNo() {  return attemptNo;    }

    public Integer getExerciseId() {
        return exerciseId;
    }

    //public Integer getScore() {
    //    return score;
    //}

    //public Timestamp getAttemptedAt() {     return attemptedAt;  }

//    public void setAttemptNo(Integer attemptNo) {
//        this.attemptNo = attemptNo;
//    }
//
//    public void setAttemptedAt(Timestamp attemptedAt) {
//        this.attemptedAt = attemptedAt;
//    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;

    }

    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }

    public void setScorePolicy(String scorePolicy) {
        this.scorePolicy = scorePolicy;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    //public void setScore(Integer score) {
    //    this.score = score;
    //}


}
