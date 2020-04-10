package com.example;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = TestcontainersInformixTest.Initializer.class)
class TestcontainersInformixTest {


    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Integer mappedPort = ifxContainer.getMappedPort(9088);
            String jdbcUrl = MessageFormat.format("spring.datasource.url=jdbc:informix-sqli://localhost:{0,number,#}/sysadmin:INFORMIXSERVER=informix", mappedPort);
            TestPropertyValues.of(jdbcUrl).applyTo(applicationContext.getEnvironment());
        }
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Container
    private static GenericContainer ifxContainer = new GenericContainer<>("ibmcom/informix-developer-database:latest")
            .withExposedPorts(9088)
            .withEnv("LICENSE", "accept")
            .waitingFor(Wait.forLogMessage(".*Maximum server connections 1.*", 1));

    @Test
    @DisplayName("Command history should not be empty")
    void commandHistoryShouldNotBeEmpty() {
        assertTrue(commandHistoryNotEmpty());
    }

    private boolean commandHistoryNotEmpty() {
        return !jdbcTemplate.queryForList("select * from command_history").isEmpty();
    }

}
