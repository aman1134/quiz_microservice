package com.example.quiz_micro_service.request;

public class QuizQuestionRequest extends Request{
    private String qzid;
    private String qid;

    public QuizQuestionRequest(String tid, String password, String qzid, String qid) {
        super(tid, password);
        this.qzid = qzid;
        this.qid = qid;
    }

    public QuizQuestionRequest() {
    }

    public String getQzid() {
        return qzid;
    }

    public void setQzid(String qzid) {
        this.qzid = qzid;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }
}
