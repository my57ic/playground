package com.example;

import org.springframework.kafka.core.KafkaTemplate;

class Publisher {

    private KafkaTemplate<String, SomeEvent> template;

    public Publisher(KafkaTemplate<String, SomeEvent> template) {
        this.template = template;
    }

    public void publish(SomeEvent event) {
        template.send("test", event);
    }
}
