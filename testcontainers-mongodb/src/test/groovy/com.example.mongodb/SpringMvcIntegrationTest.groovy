package com.example.mongodb

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import static groovy.json.JsonOutput.toJson
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@WebMvcTest
class SpringMvcIntegrationTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @MockBean
    CustomerRepository customerRepository

    def "should allow to add customer"() {
        given:
            Map request = [
                    firstName: 'John',
                    lastName : 'Doe'
            ]
        when:
            def result = mockMvc.perform(post("/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(toJson(request)))
        then:
            result.andDo(print()).andExpect(status().isCreated())
    }

    def "should return customer"() {
        setup:
            when(customerRepository.findByFirstName("John")).thenReturn(Optional.of(new Customer("John", "Doe")))
        when:
            def result = mockMvc.perform(get("/")
                            .param("firstName", "John"))
        then:
            result.andDo(print()).andExpect(status().isOk())
    }

}
