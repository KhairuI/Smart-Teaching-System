package com.example.smartteachingsystem.view.model;

        // retrieve teacher profile data....

import java.io.Serializable;

public class Teacher implements Serializable {
    private String name;
    private String id;
    private String email;
    private String phone;
    private String department;
    private String designation;
    private String image;
    private String office;
    private String counseling;
    private String initial;

    public Teacher() {
    }

    public Teacher(String name, String id, String email, String phone, String department, String designation, String image, String office, String counseling, String initial) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.designation = designation;
        this.image = image;
        this.office = office;
        this.counseling = counseling;
        this.initial = initial;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public String getCounseling() {
        return counseling;
    }

    public void setCounseling(String counseling) {
        this.counseling = counseling;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }
}
