package com.example.quiz_micro_service.request;

public class SkipQuestionRequest extends Request{
    private int points;
    private String qid;
    private String cid;
    private String qzid;
    private int difficultyLevel;
    private int correctAnswers;
    private int questionPoints;

    public SkipQuestionRequest(String id, String password, int points, String qid, String cid, String qzid, int difficultyLevel, int correctAnswers, int questionPoints) {
        super(id, password);
        this.points = points;
        this.qid = qid;
        this.cid = cid;
        this.qzid = qzid;
        this.difficultyLevel = difficultyLevel;
        this.correctAnswers = correctAnswers;
        this.questionPoints = questionPoints;
    }

    public SkipQuestionRequest() {
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQzid() {
        return qzid;
    }

    public void setQzid(String qzid) {
        this.qzid = qzid;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getQuestionPoints() {
        return questionPoints;
    }

    public void setQuestionPoints(int questionPoints) {
        this.questionPoints = questionPoints;
    }
}
