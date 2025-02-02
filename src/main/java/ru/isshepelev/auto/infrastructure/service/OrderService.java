package ru.isshepelev.auto.infrastructure.service;

import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;
import ru.isshepelev.auto.infrastructure.persistance.entity.Order;

import java.util.List;
import java.util.UUID;

@Service
public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderById(UUID id);


    Order createNewOrder(List<?> items);

    void processingOrder(Order orderCreate);
}
