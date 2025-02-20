package ru.isshepelev.auto.infrastructure.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.Employee;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;
import ru.isshepelev.auto.infrastructure.persistance.entity.MenuRevision;
import ru.isshepelev.auto.infrastructure.persistance.entity.Order;
import ru.isshepelev.auto.infrastructure.persistance.entity.enums.OrderStatus;
import ru.isshepelev.auto.infrastructure.persistance.repository.OrderRepository;
import ru.isshepelev.auto.infrastructure.service.MenuService;
import ru.isshepelev.auto.infrastructure.service.OrderService;
import ru.isshepelev.auto.infrastructure.service.dto.OrderMenuDto;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.repository.UserRepository;
import ru.isshepelev.auto.security.service.UserService;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MenuService menuService;
    private final UserService userService;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOwner().getUsername().equals(userService.getUsernameAuthorizedUser())).toList();
    }

    @Override
    public Order getOrderById(UUID id) {
        Optional<Order> orderOptional = orderRepository.findById(id)
                .filter(order -> order.getOwner().getUsername().equals(userService.getUsernameAuthorizedUser()));
        if (orderOptional.isEmpty()){
            throw new NullPointerException();
        }
        return orderOptional.get();
    }

    @Override
    public Order createNewOrder(List<?> items) {
        List<Menu> validMenuItems = new ArrayList<>();
        List<OrderMenuDto> menuList = new ArrayList<>();
        Order order;

        if (items.isEmpty()) {
            order = new Order(UUID.randomUUID(), LocalDate.now().atStartOfDay(), OrderStatus.ERROR, null);
            orderRepository.save(order);
            return order;
        }

        if (items.get(0) instanceof UUID) {
            List<MenuRevision> menuRevisions = menuService.getAllRevisions();
            Set<MenuRevision> foundRevisions = new HashSet<>();

            for (UUID itemId : (List<UUID>) items) {
                Optional<Menu> menuItemOpt = menuRevisions.stream()
                        .flatMap(revision -> revision.getRevision().stream())
                        .filter(menu -> menu.getId().equals(itemId))
                        .findFirst();

                if (menuItemOpt.isEmpty()) {
                    log.error("Элемент " + itemId + " не найден ни в одной ревизии.");
                    order = new Order(UUID.randomUUID(), LocalDate.now().atStartOfDay(), OrderStatus.ERROR, null);
                    orderRepository.save(order);
                    return order;
                }
                Menu menuItem = menuItemOpt.get();

                MenuRevision itemRevision = menuRevisions.stream()
                        .filter(revision -> revision.getRevision().contains(menuItem))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("ревизия для элемента " + itemId + " не найдена."));

                foundRevisions.add(itemRevision);
                validMenuItems.add(menuItem);
            }

            if (foundRevisions.size() > 1) {
                log.error("Элементы принадлежат нескольким ревизиям. Разрешена только одна ревизия.");
                order = new Order(UUID.randomUUID(), LocalDate.now().atStartOfDay(), OrderStatus.ERROR, null);
                orderRepository.save(order);
                return order;
            }
        } else if (items.get(0) instanceof Menu) {
            validMenuItems = (List<Menu>) items;
        } else {
            log.error("Invalid item type.");
            order = new Order(UUID.randomUUID(), LocalDate.now().atStartOfDay(), OrderStatus.ERROR, null);
            orderRepository.save(order);
            return order;
        }

        validMenuItems.forEach(item -> {
            OrderMenuDto menu = new OrderMenuDto();
            menu.setId(item.getId());
            menu.setName(item.getName());
            menu.setDescription(item.getDescription());
            menuList.add(menu);
        });

        order = new Order(UUID.randomUUID(), LocalDate.now().atStartOfDay(), OrderStatus.CREATION, menuList);
        log.info("создание заказа " + order);

        User user = userService.getUserByUsername(userService.getUsernameAuthorizedUser());
        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }

        order.setOwner(user);
        orderRepository.save(order);

        return order;
    }

    @Override
    public void processingOrder(Order orderCreate) {
        if (orderCreate.getStatus().equals(OrderStatus.ERROR)) return;

        List<Menu> menuItems = menuService.getAllMenuItems();
        List<Menu> selectedItems = menuItems.stream()
                .filter(menu -> orderCreate.getMenuList().stream()
                        .anyMatch(orderMenuDto -> orderMenuDto.getId().equals(menu.getId())))
                .toList();
        List<Menu> stopList = selectedItems.stream()
                .filter(menu -> menu.getCount() == 0)
                .toList();

        Order order = orderRepository.findById(orderCreate.getId())
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        if (!stopList.isEmpty()) {
            order.setStatus(OrderStatus.ERROR);
            orderRepository.save(order);
            return;
        }

        order.setStatus(OrderStatus.СOOKING_COMPLETED);
        orderRepository.save(order);

        selectedItems.forEach(menu -> {
            long countInOrder = orderCreate.getMenuList().stream()
                    .filter(orderMenuDto -> orderMenuDto.getId().equals(menu.getId()))
                    .count();
            menu.setCount(menu.getCount() - (int) countInOrder);
        });
        menuService.updateMenuItems(selectedItems);
    }
}
