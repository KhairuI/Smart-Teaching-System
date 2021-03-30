package com.example.smartteachingsystem.view.model;

public class Response {

    private String status;
    private String message;
    private String pushKey;
    private String uId;

    public Response() {
    }

    public Response(String status, String message, String pushKey, String uId) {
        this.status = status;
        this.message = message;
        this.pushKey = pushKey;
        this.uId = uId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
