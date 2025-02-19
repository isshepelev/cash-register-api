package ru.isshepelev.auto.infrastructure.persistance.repository;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isshepelev.auto.infrastructure.persistance.entity.Order;
import ru.isshepelev.auto.security.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
//    List<Order> findByOwner(User owner);
}
