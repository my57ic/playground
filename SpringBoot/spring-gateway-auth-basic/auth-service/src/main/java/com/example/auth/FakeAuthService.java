package com.example.auth;

class FakeAuthService implements AuthService{
    @Override
    public boolean canAuthenticate(String userName, String password) {
        return "test".equals(userName) && "test12345".equals(password);
    }
}
