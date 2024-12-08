package ru.isshepelev.auto.infrastructure.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;
import ru.isshepelev.auto.infrastructure.service.dto.MenuDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface MenuService {

    List<Menu> getAllMenuItems();
    void createMenuItem(MenuDto menuDto);
    void updateMenuItem(UUID id, MenuDto menuDto);
    void deleteMenuItem(UUID id);

    Optional<Menu> findMenuById(UUID id);

    List<Menu> getItems(int page, int pageSize);

    List<Menu> getStopList();

    @Transactional
    void updateMenuItems(List<Menu> menuList);
}
