package com.example.quiz_micro_service.request;

public class UpdateResourceRequest extends Request {

    private String rid;
    private String colName;
    private String value;

    public UpdateResourceRequest(String tid, String rid, String colName, String value, String password) {
        super(tid, password);
        this.rid = rid;
        this.colName = colName;
        this.value = value;
    }

    public UpdateResourceRequest() {
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
