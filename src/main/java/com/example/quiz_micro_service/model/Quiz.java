package com.example.quiz_micro_service.model;

public class Quiz {
    private String qzid;
    private int cutOff;
    private int maxPoints;
    private int maxTime;

    public Quiz(String qzid, int cutOff, int maxPoints, int maxTime) {
        this.qzid = qzid;
        this.cutOff = cutOff;
        this.maxPoints = maxPoints;
        this.maxTime = maxTime;
    }

    public Quiz() {
    }

    public String getQzid() {
        return qzid;
    }

    public void setQzid(String qzid) {
        this.qzid = qzid;
    }

    public int getCutOff() {
        return cutOff;
    }

    public void setCutOff(int cutOff) {
        this.cutOff = cutOff;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }
}
