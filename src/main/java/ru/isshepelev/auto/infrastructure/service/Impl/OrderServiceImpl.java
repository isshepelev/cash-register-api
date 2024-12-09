package ru.isshepelev.auto.infrastructure.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    public Order createNewOrder(List<Menu> itemsList){
        List<Menu> menuList = new ArrayList<>();
        itemsList.forEach(item -> {
            Menu menu = new Menu();
            menu.setId(item.getId());
            menu.setName(item.getName());
            menu.setDescription(item.getDescription());
            menu.setCount(item.getCount());
            menuList.add(menu);
        });
        Order order = new Order(UUID.randomUUID(), LocalDate.now().atStartOfDay(), OrderStatus.CREATION, menuList);
        orderRepository.save(order);
        return order;
    }

    @Override
    @SneakyThrows
    public void processingOrder(Order orderCreate) {
        List<Menu> menuItems = menuService.getAllMenuItems();
        List<Menu> selectedItems = menuItems.stream().filter(menu -> orderCreate.getMenuList().contains(menu)).toList();
        List<Menu> stopList = selectedItems.stream().filter(menu -> menu.getCount() == 0).toList();

        Order order = orderRepository.findById(orderCreate.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

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
