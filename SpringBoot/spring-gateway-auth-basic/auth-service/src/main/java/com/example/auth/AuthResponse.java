package com.example.auth;

class AuthResponse {

    private boolean isAuthenticated;

    AuthResponse(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}
