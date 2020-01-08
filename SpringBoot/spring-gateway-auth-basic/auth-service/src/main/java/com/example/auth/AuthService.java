package com.example.auth;

interface AuthService {

    boolean canAuthenticate(String userName, String password);
}
