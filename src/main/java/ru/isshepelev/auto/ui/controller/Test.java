package ru.isshepelev.auto.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.isshepelev.auto.infrastructure.persistance.entity.Order;
import ru.isshepelev.auto.infrastructure.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Test {
    private final OrderService orderService;

    @GetMapping("/api/orders")
    public List<Order> test(){
        return orderService.getAllOrders();
    }


}
