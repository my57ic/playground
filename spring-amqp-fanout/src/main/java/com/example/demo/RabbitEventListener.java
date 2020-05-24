package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

class RabbitEventListener implements EventListener {

    private final Logger logger = LoggerFactory.getLogger(RabbitEventListener.class);

    @RabbitListener(id = "test", queues = {"firstQueue", "secondQueue"})
    @Override
    public void listen(CustomEvent customEvent, @Header(AmqpHeaders.CONSUMER_QUEUE) String queueName) {
        logger.info("Received event from queue {}: {}", queueName, customEvent);
    }
}
