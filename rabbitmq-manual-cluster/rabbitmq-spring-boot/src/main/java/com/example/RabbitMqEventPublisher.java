package com.example;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

class RabbitMqEventPublisher implements EventPublisher {

    private RabbitTemplate rabbitTemplate;

    RabbitMqEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(Event event) {
        rabbitTemplate.convertAndSend("test", event);
    }
}
