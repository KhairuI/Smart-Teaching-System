package com.example.smartteachingsystem.view.model;

public class Message {
    public String dateTime;
    public String messageType;
    public String message;
    public String image;
    public String senderId;

    public Message() {
    }

    public Message(String dateTime, String messageType, String message, String image, String senderId) {
        this.dateTime = dateTime;
        this.messageType = messageType;
        this.message = message;
        this.image = image;
        this.senderId = senderId;
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
}
