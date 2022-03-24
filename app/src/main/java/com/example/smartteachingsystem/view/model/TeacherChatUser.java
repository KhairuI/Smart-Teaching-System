package com.example.smartteachingsystem.view.model;

public class TeacherChatUser {
    public String dateTime;
    public String lastMessage;
    public String messageType;
    public String image;
    public String messageUid;
    public String receiverUid;
    public String name;

    public TeacherChatUser() {
    }

    public TeacherChatUser(String dateTime, String lastMessage, String messageType, String receiverImage, String receiverUid, String senderUid, String receiverName) {
        this.dateTime = dateTime;
        this.lastMessage = lastMessage;
        this.messageType = messageType;
        this.image = receiverImage;
        this.messageUid = receiverUid;
        this.receiverUid = senderUid;
        this.name = receiverName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessageUid() {
        return messageUid;
    }

    public void setMessageUid(String messageUid) {
        this.messageUid = messageUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
