package com.example.quiz_micro_service.request;

public class QuizRequest extends Request{
    private String cutOff;
    private String maxPoints;
    private String maxTime;

    public QuizRequest(String tid, String password, String cutOff, String maxPoints, String maxTime) {
        super(tid, password);
        this.cutOff = cutOff;
        this.maxPoints = maxPoints;
        this.maxTime = maxTime;
    }

    public QuizRequest() {
    }

    public String getCutOff() {
        return cutOff;
    }

    public void setCutOff(String cutOff) {
        this.cutOff = cutOff;
    }

    public String getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(String maxPoints) {
        this.maxPoints = maxPoints;
    }

    public String getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }
}
