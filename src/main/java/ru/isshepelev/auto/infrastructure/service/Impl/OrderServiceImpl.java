package ru.isshepelev.auto.infrastructure.service.Impl;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;
import ru.isshepelev.auto.infrastructure.persistance.entity.Order;
import ru.isshepelev.auto.infrastructure.persistance.entity.enums.OrderStatus;
import ru.isshepelev.auto.infrastructure.persistance.repository.OrderRepository;
import ru.isshepelev.auto.infrastructure.service.MenuService;
import ru.isshepelev.auto.infrastructure.service.OrderService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MenuService menuService;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    @Override
    public void createNewOrder(List<UUID> idOrderItems) {
        List<Menu> menuItems = menuService.getAllMenuItems();
        List<Menu> selectedItems = menuItems.stream().filter(menu -> idOrderItems.contains(menu.getId())).toList();
        List<Menu> stopList = selectedItems.stream().filter(menu -> menu.getCount() == 0).toList();
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setDate(LocalDate.now().atStartOfDay());
        order.setMenuList(selectedItems);

        if (!stopList.isEmpty()) {
            order.setStatus(OrderStatus.ERROR);
            orderRepository.save(order);
            return;
        }

        order.setStatus(OrderStatus.СOOKING_COMPLETED);
        orderRepository.save(order);

        selectedItems.forEach(menu -> menu.setCount(menu.getCount() - 1));
        menuService.updateMenuItems(selectedItems);
    }
}
