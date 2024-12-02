package ru.isshepelev.auto.infrastructure.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;
import ru.isshepelev.auto.infrastructure.persistance.repository.MenuRepositroty;
import ru.isshepelev.auto.infrastructure.service.MenuService;
import ru.isshepelev.auto.infrastructure.service.dto.MenuDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final MenuRepositroty menuRepository;

    @Override
    public List<Menu> getAllMenuItems() {
        return menuRepository.findAll();
    }

    @Override
    @Transactional
    public void createMenuItem(MenuDto menuDto) {
        Menu menu = new Menu();
        menu.setId(UUID.randomUUID());
        menu.setName(menuDto.getName());
        menu.setDescription(menuDto.getDescription());
        menu.setCount(menuDto.getCount());
        log.info("добавление товара{} ", menu);
        menuRepository.save(menu);
    }

    @Override
    @Transactional
    public void updateMenuItem(UUID id, MenuDto menuDto) {
        Optional<Menu> menuOptional = menuRepository.findById(id);
        if (menuOptional.isPresent()) {
            Menu menu = menuOptional.get();
            menu.setName(menuDto.getName());
            menu.setDescription(menuDto.getDescription());
            menu.setCount(menuDto.getCount());
            log.info("изменение товара {} ", menu);
            menuRepository.save(menu);
        }
    }

    @Override
    @Transactional
    public void deleteMenuItem(UUID id) {
        log.info("удаление товара {} ", menuRepository.findById(id));
        menuRepository.deleteById(id);
    }

    private MenuDto convertToDto(Menu menu) {
        MenuDto menuDto = new MenuDto();
        menuDto.setName(menu.getName());
        menuDto.setDescription(menu.getDescription());
        menuDto.setCount(menu.getCount());
        return menuDto;
    }
    @Override
    public Optional<Menu> findMenuById(UUID id){
        return menuRepository.findById(id);
    }
}
