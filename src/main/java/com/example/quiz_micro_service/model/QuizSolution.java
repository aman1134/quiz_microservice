package com.example.quiz_micro_service.model;

import com.example.quiz_micro_service.request.Request;

import java.util.ArrayList;

public class QuizSolution extends Request {

    private String cid;
    private ArrayList<QuestionSolution> questionSolutions;

    public QuizSolution(String id , String password, String cid, ArrayList<QuestionSolution> questionSolutions) {
        this.id = id;
        this.cid = cid;
        this.password = password;
        this.questionSolutions = questionSolutions;
    }

    public QuizSolution() {
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public ArrayList<QuestionSolution> getQuestionSolutions() {
        return questionSolutions;
    }

    public void setQuestionSolutions(ArrayList<QuestionSolution> questionSolutions) {
        this.questionSolutions = questionSolutions;
    }
}
