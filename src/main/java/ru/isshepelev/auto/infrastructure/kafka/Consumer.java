package ru.isshepelev.auto.infrastructure.kafka;

import ch.qos.logback.core.joran.conditional.ThenAction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.isshepelev.auto.infrastructure.service.OrderService;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class Consumer {

    private final OrderService orderService;

    @KafkaListener(topics = "order", groupId = "1")
    @SneakyThrows
    public void orderListener(List<UUID> idOrders){
        System.out.println(idOrders);
        orderService.createNewOrder(idOrders);
    }
}
