package com.example.smartteachingsystem.view.model;

public class StudentApp {
    // This class is used for student appointment information in teacher directory........

    private String status;
    private String message;
    private String name;
    private String id;
    private String uId;
    private String pushKey;
    private String email;
    private String dept;
    private String image;
    private String section;
    private String phone;

    public StudentApp() {
    }

    public StudentApp(String status, String message, String name, String id, String uId, String pushKey, String email, String dept, String image, String section, String phone) {
        this.status = status;
        this.message = message;
        this.name = name;
        this.id = id;
        this.uId = uId;
        this.pushKey = pushKey;
        this.email = email;
        this.dept = dept;
        this.image = image;
        this.section = section;
        this.phone = phone;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
