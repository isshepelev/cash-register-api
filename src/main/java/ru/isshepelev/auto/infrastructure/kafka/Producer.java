package ru.isshepelev.auto.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.isshepelev.auto.infrastructure.persistance.entity.Order;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class Producer {

    private final KafkaTemplate<String, Order> template;

    public void sendOrder(Order order){
        template.send("order", order);
    }


}
