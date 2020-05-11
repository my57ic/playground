package com.example.mongodb

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.GenericContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification

import java.text.MessageFormat

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = Initializer.class)
class TestcontainersMongoDbIntegrationTest extends Specification{

    static GenericContainer mongoDb = new GenericContainer("mongo:latest").withExposedPorts(27017)

    @Autowired
    private CustomerRepository customerRepository

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        void initialize(ConfigurableApplicationContext applicationContext) {
            mongoDb.start()
            Integer mappedPort = mongoDb.getMappedPort(27017)
            String mongoPort = MessageFormat.format("spring.data.mongodb.port={0,number,#}", mappedPort)
            TestPropertyValues.of(mongoPort).applyTo(applicationContext.getEnvironment())
        }
    }

    def "should persist customer"() {
        given:
            def customer = getCustomer()
        when:
            customerRepository.save(customer)
            def customerFromDb = findCustomer(customer.firstName)
        then:
            !customerFromDb.empty
            with(customerFromDb.get()) {
                firstName == customer.firstName
                lastName == customer.lastName
            }
    }

    private Optional<Customer> findCustomer(String firstName) {
        customerRepository.findByFirstName(firstName)
    }

    private static Customer getCustomer() {
        new Customer("John", "Doe")
    }

}
