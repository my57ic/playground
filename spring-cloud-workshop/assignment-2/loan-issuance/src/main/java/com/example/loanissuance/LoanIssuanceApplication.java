package com.example.loanissuance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
public class LoanIssuanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanIssuanceApplication.class, args);
	}

}


@Configuration
@EnableFeignClients
class LoanConfig {

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

@RestController
class LoanController {

    FraudClient fraudClient;
    RestTemplate restTemplate;

    public LoanController(@LoadBalanced RestTemplate restTemplate, FraudClient fraudClient) {
        this.restTemplate = restTemplate;
        this.fraudClient = fraudClient;
    }

    @GetMapping("/resttemplate/frauds")
    List<String> fraudsByLb() {
        return restTemplate.getForObject("http://fraud-detection/frauds", List.class);
    }

    @GetMapping("/openfeign/frauds")
    List<String> fraudsByFeign() {
        return fraudClient.frauds();
    }
}

@FeignClient("fraud-detection")
interface FraudClient {

    @GetMapping("/frauds")
    List<String> frauds();

}
