package ru.isshepelev.auto.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class Producer {

    private final KafkaTemplate<String, List<UUID>> template;

    public void sendOrder(List<UUID> idOrders){
        template.send("order", idOrders);
    }


}
