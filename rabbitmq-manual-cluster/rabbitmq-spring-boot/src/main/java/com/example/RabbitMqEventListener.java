package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

class RabbitMqEventListener implements EventListener {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqEventListener.class);

    @Override
    @RabbitListener(queues = "test")
    public void receive(Event event) {
        logger.info("{}", event);
    }
}
