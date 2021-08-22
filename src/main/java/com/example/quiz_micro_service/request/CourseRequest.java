package com.example.quiz_micro_service.request;

public class CourseRequest extends Request{
    private String qzid;
    private String lid;

    public CourseRequest(String id, String password, String qzid, String lid) {
        super(id, password);
        this.qzid = qzid;
        this.lid = lid;
    }

    public CourseRequest() {
    }

    public String getQzid() {
        return qzid;
    }

    public void setQzid(String qzid) {
        this.qzid = qzid;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }
}
