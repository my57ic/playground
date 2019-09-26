package com.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableAutoConfiguration
class AppConfig {

    @Value("${brokerUrls:localhost:9092}")
    private String brokerUrls;

    @Bean
    KafkaTemplate<String, SomeEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    ProducerFactory<String, SomeEvent> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerUrls);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    Map<String, Object> consumerConfigs() {
        return Map.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerUrls,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, SomeEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SomeEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckOnError(false);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        return factory;
    }

    @Bean
    ConsumerFactory<String, SomeEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(SomeEvent.class, false));
    }

    @Bean
    Publisher publisher() {
        return new Publisher(kafkaTemplate());
    }

    @Bean
    Listener listener() {
        return new Listener();
    }
}
