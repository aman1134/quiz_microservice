package com.example.quiz_micro_service.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionRequest extends Request {
    private String qzid;
    private String question;
    private String points;
    private ArrayList<String> options;
    private ArrayList<String> answers;
    private int difficultyLevel;

    public QuestionRequest(String id, String password, String qzid, String question, String points, ArrayList<String> answers, ArrayList<String> options, int difficultyLevel) {
        super(id, password);
        this.options = options;
        this.qzid = qzid;
        this.question = question;
        this.points = points;
        this.answers = answers;
        this.difficultyLevel = difficultyLevel;
    }

    public QuestionRequest() {

    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public String getQzid() {
        return qzid;
    }

    public void setQzid(String qzid) {
        this.qzid = qzid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
}
