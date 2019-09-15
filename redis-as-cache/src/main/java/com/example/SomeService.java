package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SomeService {

    private Logger logger = LoggerFactory.getLogger(SomeService.class);

    public String longRunningStringProducer(String parameter) {
        logger.info("Service call with argument: {}", parameter);
        return parameter;
    }
}
