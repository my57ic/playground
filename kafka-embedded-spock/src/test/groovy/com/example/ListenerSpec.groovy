package com.example


import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.spockframework.spring.SpringSpy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.context.TestPropertySource
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import static org.mockito.Mockito.*;

@EmbeddedKafka
@SpringBootTest(classes = AppConfig)
@TestPropertySource(properties = ["spring.kafka.consumer.auto-offset-reset=earliest", 'brokerUrls: ${spring.embedded.kafka.brokers}'])
class ListenerSpec extends Specification {

    private static final String TOPIC = "test"

    @SpyBean
    Listener listener;

    @Autowired
    Publisher publisher;

    @Captor
    ArgumentCaptor<SomeEvent> captor;

    def "should receive sent message"() {
        given:
            def event = new SomeEvent("test message!")
        when:
            publisher.publish(event)
        then:
            verify(listener, timeout(1000)).receive(captor.capture(), any())
            captor.getValue().message == event.message
    }
}
