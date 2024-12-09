package ru.isshepelev.auto.infrastructure.service;

import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;
import ru.isshepelev.auto.infrastructure.persistance.entity.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface OrderService {

    List<Order> getAllOrders();

    Optional<Order> getOrderById(UUID id);

    Order createNewOrder(List<Menu> itemsList);

    void processingOrder(Order orderCreate);
}
