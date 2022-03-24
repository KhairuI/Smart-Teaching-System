package com.example.smartteachingsystem.view.model;

public class ChatUser {
    public String dateTime;
    public String lastMessage;
    public String messageType;
    public String receiverImage;
    public String receiverUid;
    public String senderUid;
    public String receiverName;

    public ChatUser() {
    }

    public ChatUser(String dateTime, String lastMessage, String messageType, String receiverImage, String receiverUid, String senderUid, String receiverName) {
        this.dateTime = dateTime;
        this.lastMessage = lastMessage;
        this.messageType = messageType;
        this.receiverImage = receiverImage;
        this.receiverUid = receiverUid;
        this.senderUid = senderUid;
        this.receiverName = receiverName;
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

    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
