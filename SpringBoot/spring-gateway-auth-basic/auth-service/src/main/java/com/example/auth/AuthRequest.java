package com.example.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

final class AuthRequest {

    private final String userName;
    private final String password;

    @JsonCreator
    public AuthRequest(@JsonProperty(value = "userName") String userName,
                       @JsonProperty(value = "password") String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthRequest{" +
                "userName='" + userName + '\'' +
                '}';
    }

    String getUserName() {
        return userName;
    }

    String getPassword() {
        return password;
    }
}
