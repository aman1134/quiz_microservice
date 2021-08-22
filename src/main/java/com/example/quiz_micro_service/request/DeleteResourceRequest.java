package com.example.quiz_micro_service.request;

public class DeleteResourceRequest extends Request{
    private String rid;

    public DeleteResourceRequest(String id, String rid, String password) {
        super(id, password);
        this.rid = rid;
    }

    public DeleteResourceRequest() {
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

}
