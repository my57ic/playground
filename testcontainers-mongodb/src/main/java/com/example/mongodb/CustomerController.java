package com.example.mongodb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@RestController
class CustomerController {

    @Autowired
    CustomerRepository repository;

    @PostMapping
    ResponseEntity<Void> addCustomer(@RequestBody @Valid CustomerDto customer) {
        repository.save(new Customer(customer.getFirstName(), customer.getLastName()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<CustomerDto> findByFirstName(@RequestParam String firstName) throws NotFoundException {
        Customer customer = repository.findByFirstName(firstName).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(getCustomerDto(customer), HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleException(MethodArgumentNotValidException ex) throws JsonProcessingException {
        List<String> collect = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        String message = MessageFormat.format("'{'\"errors\": {0}'}'", new ObjectMapper().writeValueAsString(collect));
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNoFound() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private CustomerDto getCustomerDto(Customer customer) {
        return new CustomerDto(customer.getFirstName(), customer.getLastName());
    }
}
