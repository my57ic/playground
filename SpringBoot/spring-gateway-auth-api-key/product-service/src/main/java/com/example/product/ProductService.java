package com.example.product;

import com.example.product.Money;
import com.example.product.ProductDto;

import java.util.List;

class ProductService {

    List<ProductDto> products() {
        return List.of(
          new ProductDto("product 1", 1, new Money(10.99, Money.Currency.PLN)),
          new ProductDto("product 2", 23, new Money(12.99, Money.Currency.USD)),
          new ProductDto("product 3", 11, new Money(111.99, Money.Currency.EUR))
        );
    }
}
