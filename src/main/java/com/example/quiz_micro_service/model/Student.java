package com.example.quiz_micro_service.model;

public class Student extends User{
    private int totalPoints;
    private String sid;

    public Student(String name, String password, int totalPoints, String sid) {
        super(name, password);
        this.totalPoints = totalPoints;
        this.sid = sid;
    }

    public Student(int totalPoints, String sid) {
        this.totalPoints = totalPoints;
        this.sid = sid;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
