package com.example.frauddetection;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class FraudDetectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(FraudDetectionApplication.class, args);
    }

}

@Configuration
class FraudConfiguration {

    @Bean
    Counter counter(MeterRegistry meterRegistry) {
        return meterRegistry.counter("fraud-counter");
    }
}


@RestController
class FraudController {

    private static final Logger log = LoggerFactory.getLogger(FraudController.class);

    private Counter counter;

    public FraudController(Counter counter) {
        this.counter = counter;
    }

    @GetMapping("/frauds")
    List<String> frauds() {
        var frauds = List.of("Fraud 1", "Fraud 2", "Fraud 3");
        log.info("Frauds: " + frauds);
        counter.increment();
        return frauds;
    }

    @GetMapping("frauds/count")
    double count() {
        return counter.count();
    }
}
