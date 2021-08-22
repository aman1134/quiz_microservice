package com.example.quiz_micro_service.model;

import java.util.ArrayList;

public class QuestionSolution {
    private String qid;
    private ArrayList<String> answers;

    public QuestionSolution(String qid, ArrayList<String> answers) {
        this.qid = qid;
        this.answers = answers;
    }

    public QuestionSolution() {
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }
}
