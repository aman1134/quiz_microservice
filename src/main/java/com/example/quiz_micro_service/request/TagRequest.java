package com.example.quiz_micro_service.request;

public class TagRequest extends Request {
    private String tag;
    private String qid;
    private int points;

    public TagRequest(String tid, String password, String tag, String qid, int points) {
        super(tid, password);
        this.tag = tag;
        this.qid = qid;
        this.points = points;
    }

    public TagRequest() {
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }
}
