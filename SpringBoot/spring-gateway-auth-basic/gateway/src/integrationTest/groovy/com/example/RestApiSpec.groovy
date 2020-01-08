package com.example

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestApiSpec extends Specification{

    @Autowired
    WebTestClient webTestClient

    def shouldReturnOkCodeAndSuccessfulResult() {
        given:
            def authRequest = """{ 
                                    "userName": "test",
                                    "password": "test12345"
                                }"""
        expect:
            callAuthApi(authRequest)
                    .expectStatus().isOk()
                    .expectBody().jsonPath('$.authenticated').isEqualTo(true)

    }

    def shouldReturnOkCodeAndUnsuccessfulResult() {
        given:
            def authRequest = """{ 
                                    "userName": "test",
                                    "password": "test123456"
                                }"""
        expect:
            callAuthApi(authRequest)
                    .expectStatus().isOk()
                    .expectBody().jsonPath('$.authenticated').isEqualTo(false)

    }

    private WebTestClient.ResponseSpec callAuthApi(String authRequest) {
        webTestClient.post().uri("/auth")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(authRequest), String.class)
                .exchange()
    }
}
