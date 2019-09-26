package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private Publisher publisher;

    @Override
    public void run(String... args) {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(() -> {
            int value = new Random().nextInt(1000);
            for (int i=0; i<value; i++) {
                publisher.publish(new SomeEvent("Publisher: publishing event " + LocalDateTime.now() + " to Kafka!"));
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
