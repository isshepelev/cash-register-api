package ru.isshepelev.auto.ui.api;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.auto.infrastructure.kafka.Producer;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;
import ru.isshepelev.auto.infrastructure.persistance.entity.Order;
import ru.isshepelev.auto.infrastructure.persistance.entity.enums.OrderStatus;
import ru.isshepelev.auto.infrastructure.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderService orderService;
    private final Producer producer;

//    @PostMapping("/create")
//    public ResponseEntity<String> createOrder(@RequestBody List<UUID> itemId) {
//
//        if (itemId.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Список заказов пуст.");
//        }
//        var order = orderService.createNewOrder(orderItems);
//        producer.sendOrder(order);
//        return ResponseEntity.ok(order.toString());
//    }
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable UUID orderId){
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }


    @GetMapping("/{orderId}/status")
    public ResponseEntity<OrderStatus> getStatus(@PathVariable UUID orderId){
        return ResponseEntity.ok(orderService.getOrderById(orderId).getStatus());
    }

}
