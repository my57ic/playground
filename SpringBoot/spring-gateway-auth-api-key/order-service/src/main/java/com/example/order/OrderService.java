package com.example.order;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.List;

class OrderService {

    private RestTemplate restTemplate;

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    List<OrderDto> orders() {
        List<ProductDto> products = restTemplate.getForObject("http://localhost:8080/products", List.class);
        return List.of(new OrderDto("01/01/12345", Instant.now().minus(Duration.ofDays(30)), products));

    }
}
