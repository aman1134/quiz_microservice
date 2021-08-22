package com.example.quiz_micro_service.request;

public class Request {
    public String id;
    public String password;

    public Request(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public Request() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
