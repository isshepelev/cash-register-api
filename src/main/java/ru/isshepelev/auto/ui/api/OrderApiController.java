package ru.isshepelev.auto.ui.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.auto.infrastructure.kafka.Producer;
import ru.isshepelev.auto.infrastructure.persistance.entity.Order;
import ru.isshepelev.auto.infrastructure.persistance.entity.enums.OrderStatus;
import ru.isshepelev.auto.infrastructure.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderService orderService;
    private final Producer producer;

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody List<UUID> menuId) {

        if (menuId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Список предметов пуст.");
        }
        Order order = orderService.createNewOrder(menuId);
        producer.sendOrder(order);
        if (order.getStatus().equals(OrderStatus.ERROR)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Товары не соответствуют одной ревизии или товар не существует " + order);
        }else {
            return ResponseEntity.status(HttpStatus.CREATED).body(order.toString());
        }

    }

    @GetMapping("")
    public ResponseEntity<List<Order>> orders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable UUID orderId){
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }


    @GetMapping("/{orderId}/status")
    public ResponseEntity<OrderStatus> getStatus(@PathVariable UUID orderId){
        return ResponseEntity.ok(orderService.getOrderById(orderId).getStatus());
    }

}
