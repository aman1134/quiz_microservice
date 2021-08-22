package com.example.quiz_micro_service.request;

public class OptionRequest extends Request{
    private String qid;
    protected String option;

    public OptionRequest(String tid, String password, String qid, String option) {
        super(tid, password);
        this.qid = qid;
        this.option = option;
    }

    public OptionRequest() {
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
