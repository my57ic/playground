package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

class Listener {

    private static final Logger logger = LoggerFactory.getLogger(Listener.class);

    @KafkaListener(topics = "test", groupId = "test.group")
    public void receive(SomeEvent event, @Header(KafkaHeaders.OFFSET) Long offset) throws Exception {
        Thread.sleep(1);
        logger.info("Offset: {}, Received event from Kafka: {}", offset, event);
        //throw new Exception();
    }
}
