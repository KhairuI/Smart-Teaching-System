package com.example.smartteachingsystem.view.model;

public class Token {

    String name;
    String Token;

    public Token() {
    }

    public Token(String name, String token) {
        this.name = name;
        Token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
