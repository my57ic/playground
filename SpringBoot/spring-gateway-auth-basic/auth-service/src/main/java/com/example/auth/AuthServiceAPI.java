package com.example.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
class AuthServiceAPI {

    @Autowired
    private AuthService authService;

    @PostMapping
    ResponseEntity<AuthResponse> canAuthenticate(@RequestBody AuthRequest authRequest) {
        boolean canAuthenticate = authService.canAuthenticate(authRequest.getUserName(), authRequest.getPassword());
        return ResponseEntity.ok(new AuthResponse(canAuthenticate));
    }
}
