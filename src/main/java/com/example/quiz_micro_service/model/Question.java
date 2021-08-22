package com.example.quiz_micro_service.model;

import java.util.List;

public class Question {
    private int points;
    private int difficultyLevel;
    private String qid;
    private String question;
    private List<String> answers;
    private List<String> options;

    public Question(int points, int difficultyLevel, String qid, String question, List<String> answers, List<String> options) {
        this.points = points;
        this.difficultyLevel = difficultyLevel;
        this.qid = qid;
        this.question = question;
        this.answers = answers;
        this.options = options;
    }

    public Question() {
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

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
