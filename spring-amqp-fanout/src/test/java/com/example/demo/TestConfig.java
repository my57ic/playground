package com.example.demo;

import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@RabbitListenerTest
@Configuration
public class TestConfig {

    @Bean
    EventListener eventListener() {
        return new RabbitEventListener();
    }

}
