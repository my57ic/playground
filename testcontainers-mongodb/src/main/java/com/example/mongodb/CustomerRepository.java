package com.example.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

interface CustomerRepository extends MongoRepository<Customer, String> {

    Optional<Customer> findByFirstName(String firstName);
}

