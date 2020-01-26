package com.example.loanissuance;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class LoanIssuanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanIssuanceApplication.class, args);
	}

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                        .minimumNumberOfCalls(3).build())
                .build());
    }

}

@RestController
class LoanController {

    private static final Logger log = LoggerFactory.getLogger(LoanController.class);

    FraudClient fraudClient;
    RestTemplate restTemplate;
    CircuitBreakerFactory factory;

    public LoanController(@LoadBalanced RestTemplate restTemplate, FraudClient fraudClient, CircuitBreakerFactory factory) {
        this.restTemplate = restTemplate;
        this.fraudClient = fraudClient;
        this.factory = factory;
    }

    @GetMapping("/resttemplate/frauds")
    @SuppressWarnings("unchecked")
    List<String> fraudsByLb() {
        return factory.create("resttemplate").run(()-> {
            var frauds = restTemplate.getForObject("http://fraud-detection/frauds", List.class);
            log.info("Frauds: " + frauds);
            return frauds;
        });
    }

    @GetMapping("/openfeign/frauds")
    List<String> fraudsByFeign() {
        return factory.create("feign").run(()-> {
            var frauds = fraudClient.frauds();
            log.info("Frauds: " + frauds);
            return frauds;
        });
    }

    @GetMapping("/missing")
    @SuppressWarnings("unchecked")
    List<String> missing() {
        return factory.create("missing").run(()-> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.error("Interrupted by circuit breaker");
            }
            return this.restTemplate.getForObject("http://fraud-detection/missing", List.class);
        }, throwable -> Arrays.asList("fixed", "value"));
    }
}

@FeignClient("fraud-detection")
interface FraudClient {

    @GetMapping("/frauds")
    List<String> frauds();

}
