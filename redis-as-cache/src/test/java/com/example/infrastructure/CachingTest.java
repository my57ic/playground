package com.example.infrastructure;

import com.example.SomeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CachingTest {

    @Autowired
    CachedService cachedService;

    @SpyBean
    SomeService someService;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @After
    public void afterTest() {
        clearCache();
    }

    private String someValue = "cache this!";

    @Test
    public void shouldGetValueFromCacheIfRequestedTwoTimes() {
        //when
        String value = callServiceTwoTimes();
        //expect
        returnedValueShouldNotBeNull(value);
        numberOfServiceInvocationsShouldBeEqualTo(1);
    }

    @Test
    public void shouldGetValueFromCalledMethodIfCacheExpires() throws InterruptedException {
        //given
        callServiceTwoTimes();
        //when
        waitForCacheToExpire();
        callServiceTwoTimes();
        //expect
        numberOfServiceInvocationsShouldBeEqualTo(2);
    }

    private void waitForCacheToExpire() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
    }

    private void returnedValueShouldNotBeNull(String value) {
        assertThat(value).isNotNull();
    }

    private void numberOfServiceInvocationsShouldBeEqualTo(int numberOfInvocations) {
        verify(someService, times(numberOfInvocations)).longRunningStringProducer(someValue);
    }

    private String callServiceTwoTimes() {
        cachedService.cachedMethod(someValue);
        return cachedService.cachedMethod(someValue);
    }

    private void clearCache() {
        redisTemplate.keys("*").forEach(key->redisTemplate.delete(key));
    }

}
