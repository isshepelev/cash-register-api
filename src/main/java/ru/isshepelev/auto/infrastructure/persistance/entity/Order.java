package ru.isshepelev.auto.infrastructure.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.isshepelev.auto.infrastructure.persistance.entity.enums.OrderStatus;
import ru.isshepelev.auto.infrastructure.service.dto.OrderMenuDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    public Order(UUID id, LocalDateTime date, OrderStatus status, List<OrderMenuDto> menuList) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.menuList = menuList != null ? menuList : new ArrayList<>();
    }

    @Id
    private UUID id;
    LocalDateTime date;
    private OrderStatus status;
    @ElementCollection
    private List<OrderMenuDto> menuList;

}
