package com.example.order;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public final class OrderDto {

    private String number;
    private final Instant creationDate;
    private final List<ProductDto> products;

    public OrderDto(String number, Instant creationDate, List<ProductDto> products) {
        this.number = number;
        this.creationDate = creationDate;
        this.products = products;
    }

    public String getNumber() {
        return number;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public List<ProductDto> getProducts() {
        return Collections.unmodifiableList(products);
    }
}
