package ru.isshepelev.auto.infrastructure.persistance.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.isshepelev.auto.infrastructure.persistance.entity.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private UUID id;
    LocalDateTime date;
    private OrderStatus status;
    @ManyToMany
    private List<Menu> menuList;

}
