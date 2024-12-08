package ru.isshepelev.auto.ui.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.auto.infrastructure.kafka.Producer;
import ru.isshepelev.auto.infrastructure.service.MenuService;
import ru.isshepelev.auto.infrastructure.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final Producer producer;

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody List<UUID> orderItems, HttpSession session) {
        String employeeName = (String) session.getAttribute("employeeName");

        if (employeeName == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Вы не авторизованы.");
        }
        if (orderItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Список заказов пуст.");
        }
        producer.sendOrder(orderItems); //TODO передавать сам заказ с статусом в процессе и списком товаров
        return ResponseEntity.ok("Заказ успешно создан.");
    }

}
