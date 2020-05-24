package com.example.demo;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.mockito.LatchCountDownAndCallRealMethodAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = AmqpFanoutTest.Initializer.class)
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
class AmqpFanoutTest {


    @Autowired
    private DemoApplication.Publisher publisher;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Container
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer();

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.rabbitmq.port=" + rabbitMQContainer.getMappedPort(5672)
            );
            values.applyTo(applicationContext);
        }
    }


    @Test
    public void everyEventListener_shouldReceiveMessage() throws InterruptedException {
        //given
        EventListener listenerSpy = this.harness.getSpy("test");
        assertNotNull(listenerSpy);
        LatchCountDownAndCallRealMethodAnswer answer = new LatchCountDownAndCallRealMethodAnswer(2);
        doAnswer(answer).when(listenerSpy).listen(any(), anyString());

        //when
        messageIsPublishedWithValue("test");

        //then
        waitForMessagesToArrive(answer);
        verify(listenerSpy, times(2)).listen(matchesEventWithTestMessage(), matchesOneOfTheQueues());

    }

    private CustomEvent matchesEventWithTestMessage() {
        return eq(new CustomEvent("test"));
    }

    private String matchesOneOfTheQueues() {
        return argThat(argument -> List.of("firstQueue", "secondQueue").contains(argument));
    }

    private void waitForMessagesToArrive(LatchCountDownAndCallRealMethodAnswer answer) throws InterruptedException {
        assertTrue(answer.getLatch().await(10, TimeUnit.SECONDS));
    }

    private void messageIsPublishedWithValue(String message) {
        publisher.publish(message);
    }
}
