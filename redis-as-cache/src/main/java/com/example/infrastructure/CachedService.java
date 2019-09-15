package com.example.infrastructure;

import com.example.SomeService;
import org.springframework.cache.annotation.Cacheable;

class CachedService {

    private SomeService someService;

    public CachedService(SomeService someService) {
        this.someService = someService;
    }

    @Cacheable(value = "cachedValue", key = "#parameter")
    public String cachedMethod(String parameter) {
        return someService.longRunningStringProducer(parameter);
    }

}
