package com.example.quiz_micro_service.request;

public class GetQuestionRequest extends Request{

    private String cid;
    private int difficultyLevel;
    private int points;
    private int correctAnswers;

    public GetQuestionRequest(String id, String password, String cid, int difficultyLevel, int points, int correctAnswers) {
        super(id, password);
        this.cid = cid;
        this.difficultyLevel = difficultyLevel;
        this.points = points;
        this.correctAnswers = correctAnswers;
    }

    public GetQuestionRequest() {
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public GetQuestionRequest(String cid) {
        this.cid = cid;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
