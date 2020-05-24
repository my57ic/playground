package com.example.demo;

import org.springframework.amqp.core.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

	@Bean
	Exchange fanoutExchange() {
		return ExchangeBuilder.fanoutExchange("fanout").build();
	}

	@Bean
    Queue firstQueue() {
        return QueueBuilder.nonDurable("firstQueue").build();
    }

    @Bean
    Queue secondQueue() {
        return QueueBuilder.nonDurable("secondQueue").build();
    }

    @Bean
    Binding firstBinding() {
        return BindingBuilder.bind(firstQueue()).to(fanoutExchange()).with("").noargs();
    }

    @Bean
    Binding secondBinding() {
        return BindingBuilder.bind(secondQueue()).to(fanoutExchange()).with("").noargs();
    }

    @Bean
    Publisher publisher(AmqpTemplate amqpTemplate) {
        return new Publisher(amqpTemplate);
    }

    @Bean
    EventListener eventListener() {
        return new RabbitEventListener();
    }

    static class Publisher {
        private final AmqpTemplate amqpTemplate;


        public Publisher(AmqpTemplate amqpTemplate) {
            this.amqpTemplate = amqpTemplate;
        }

        public void publish(String message) {
            amqpTemplate.convertAndSend("fanout", "", message);
        }
    }
}