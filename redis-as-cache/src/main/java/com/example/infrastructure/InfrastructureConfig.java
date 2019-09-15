package com.example.infrastructure;

import com.example.SomeService;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class InfrastructureConfig {

    @Bean
    CachedService cachedService(SomeService someService) {
        return new CachedService(someService);
    }
}