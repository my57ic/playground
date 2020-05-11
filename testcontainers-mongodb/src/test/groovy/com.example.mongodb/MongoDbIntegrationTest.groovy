package com.example.mongodb

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.annotation.DirtiesContext
import spock.lang.Specification

@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MongoDbIntegrationTest extends Specification{

    @Autowired
    CustomerRepository customerRepository

    def "should persist customer"() {
        given:
            def customer = getCustomer()
        when:
            customerIsPersisted(customer)
        then:
            customerFromDb(customer.firstName) != null
    }

    def "should retrieve customer"() {
        setup:
            customerRepository.save(getCustomer())
        when:
            def customer = customerRepository.findByFirstName("John")
        then:
            !customer.empty
            with(customer.get()) {
                firstName == "John"
                lastName == "Doe"
            }
    }

    private Customer customerIsPersisted(Customer customer) {
        customerRepository.save(customer)
    }

    private Optional<Customer> customerFromDb(String firstName) {
        customerRepository.findByFirstName(firstName)
    }

    private static Customer getCustomer() {
        new Customer("John", "Doe")
    }

}
