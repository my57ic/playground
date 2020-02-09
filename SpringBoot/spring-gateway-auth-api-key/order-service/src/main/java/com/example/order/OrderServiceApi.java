package com.example.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class OrderServiceApi {

    private OrderService orderService;

    @Autowired
    public OrderServiceApi(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    List<OrderDto> orders() {
        return orderService.orders();
    }

}
