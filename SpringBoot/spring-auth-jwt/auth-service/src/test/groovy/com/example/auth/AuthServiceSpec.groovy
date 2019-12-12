package com.example.auth

import spock.lang.Specification

class AuthServiceSpec extends Specification {
    AuthService authService = new FakeAuthService()

    def shouldReturnSuccessfulAuthentication() {
        given:
            def userName = "test"
            def password = "test12345"
        when:
            def successfullyAuthenticated = authService.canAuthenticate(userName, password)
        then:
            successfullyAuthenticated
    }

    def shouldReturnUnsuccessfulAuthentication() {
        given:
            def userName = "test"
            def password = "test123456"
        when:
            def successfullyAuthenticated = authService.canAuthenticate(userName, password)
        then:
            !successfullyAuthenticated
    }
}
