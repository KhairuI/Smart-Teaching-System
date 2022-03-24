package com.example.smartteachingsystem.view.model;

public class TeacherMessage {
    public String dateTime;
    public String messageType;
    public String message;
    public String image;
    public String senderId;
    public String receiverId;

    public TeacherMessage() {
    }

    public TeacherMessage(String dateTime, String messageType, String message, String image, String senderId, String receiverId) {
        this.dateTime = dateTime;
        this.messageType = messageType;
        this.message = message;
        this.image = image;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
