package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ClusterTest {

    @Autowired
    private EventPublisher publisher;
    @SpyBean
    private EventListener listener;

    @Test
    void shouldBeAbleToReceivedMessageFromMirroredQueue() {
        //given
        var event = new Event("test");
        //when
        publisher.publish(event);
        //then
        verify(listener, timeout(1000)).receive(event);
    }
}