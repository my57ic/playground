package com.example.demo;

public interface EventListener {
    void listen(CustomEvent customEvent, String queueName);
}
