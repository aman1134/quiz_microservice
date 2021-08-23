package com.example.quiz_micro_service.response;

public class DefaultErrorResponse {
    private String error;

    public DefaultErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
