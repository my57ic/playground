package com.example.mongodb;

import javax.validation.constraints.NotNull;

class CustomerDto {

    @NotNull(message = "First name should be provided")
    private final String firstName;
    @NotNull(message = "Last name should be provided")
    private final String lastName;

    public CustomerDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
