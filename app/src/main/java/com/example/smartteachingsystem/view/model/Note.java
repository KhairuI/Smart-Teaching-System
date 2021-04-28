package com.example.smartteachingsystem.view.model;

public class Note {

    private String pushKey;
    private String date;
    private String name;
    private String noteText;

    public Note() {

    }

    public Note(String pushKey, String date, String name, String noteText) {
        this.pushKey = pushKey;
        this.date = date;
        this.name = name;
        this.noteText = noteText;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}
