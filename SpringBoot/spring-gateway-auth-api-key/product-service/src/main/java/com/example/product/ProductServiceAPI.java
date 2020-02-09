package com.example.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class ProductServiceAPI {

    private ProductService productService;

    @Autowired
    public ProductServiceAPI(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    List<ProductDto> products() {
        return productService.products();
    }

}
