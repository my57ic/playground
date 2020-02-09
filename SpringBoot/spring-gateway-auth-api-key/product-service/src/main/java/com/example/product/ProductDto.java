package com.example.product;

public final class ProductDto {

    private final String name;
    private final Integer quantity;
    private final Money price;

    public ProductDto(String name, Integer quantity, Money price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
