package com.example.quiz_micro_service.request;

public class AnswerRequest extends Request{
    private String qid;
    protected String answer;

    public AnswerRequest(String id, String password, String qid, String answer) {
        super(id, password);
        this.qid = qid;
        this.answer = answer;
    }

    public AnswerRequest() {
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
