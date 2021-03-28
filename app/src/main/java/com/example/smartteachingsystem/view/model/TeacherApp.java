package com.example.smartteachingsystem.view.model;

public class TeacherApp {
    // This class is used for teacher appointment information in student directory.............
    private String status;
    private String message;
    private String name;
    private String id;
    private String uId;
    private String pushKey;
    private String email;
    private String dept;
    private String image;
    private String office;
    private String initial;
    private String designation;
    private String phone;

    public TeacherApp() {
    }

    public TeacherApp(String status, String message, String name, String id, String uId, String pushKey, String email, String dept, String image, String office, String initial, String designation, String phone) {
        this.status = status;
        this.message = message;
        this.name = name;
        this.id = id;
        this.uId = uId;
        this.pushKey = pushKey;
        this.email = email;
        this.dept = dept;
        this.image = image;
        this.office = office;
        this.initial = initial;
        this.designation = designation;
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

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
