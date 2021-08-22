package com.example.quiz_micro_service.request;

public class StudentCourseRequest extends Request {

    private String cid;

    public StudentCourseRequest(String id, String password, String cid) {
        super(id, password);
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
