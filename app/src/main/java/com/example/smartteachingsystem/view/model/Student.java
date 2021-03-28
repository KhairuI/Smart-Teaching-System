package com.example.smartteachingsystem.view.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class Student implements Serializable {

    //  retrieve student profile data ......

    private String name;
    private String id;
    private String email;
    private String phone;
    private String department;
    private String section;
    private String image;

    public Student() {
    }

    public Student(String name, String id, String email, String phone, String department, String section, String image) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.section = section;
        this.image = image;
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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
