package ru.isshepelev.auto.infrastructure.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;
import ru.isshepelev.auto.infrastructure.persistance.entity.MenuRevision;
import ru.isshepelev.auto.infrastructure.persistance.entity.enums.StatusRevision;
import ru.isshepelev.auto.infrastructure.persistance.repository.MenuRepositroty;
import ru.isshepelev.auto.infrastructure.persistance.repository.MenuRevisionRepository;
import ru.isshepelev.auto.infrastructure.service.MenuService;
import ru.isshepelev.auto.infrastructure.service.dto.MenuDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final MenuRepositroty menuRepository;
    private final MenuRevisionRepository menuRevisionRepository;


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
    public void createNewMenu(List<MenuDto> menuDtoList){
        List<Menu> menuList = new ArrayList<>();
        for (MenuDto dto : menuDtoList){
            Menu menu = new Menu();
            menu.setId(UUID.randomUUID());
            menu.setName(dto.getName());
            menu.setCount(dto.getCount());
            menu.setDescription(dto.getDescription());
            menuList.add(menu);
        }
        MenuRevision revision = new MenuRevision();
        revision.setDateOfCreate(LocalDate.now());
        revision.setStatus(StatusRevision.USED);
        revision.setRevision(menuList);
        menuRepository.saveAll(menuList);
        menuRevisionRepository.save(revision);

    }

    @Override
    @Transactional
    public void updateMenuItem(UUID id, MenuDto menuDto) {
        Optional<Menu> menuOptional = menuRepository.findById(id);
        if (menuOptional.isPresent()) {
            Menu menu = menuOptional.get();
            menu.setName(menuDto.getName());
            menu.setDescription(menuDto.getDescription());

            if (menuDto.getCount() < 0){
                menu.setCount(0);
            }

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
    @Override
    public Optional<Menu> findMenuById(UUID id){
        return menuRepository.findById(id);
    }
    @Override
    public List<Menu> getItems(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Menu> menuPage = menuRepository.findAll(pageable);
        return menuPage.getContent();
    }

    @Override
    public List<Menu> getStopList(){
        return getAllMenuItems().stream()
                .filter(e -> e.getCount() == 0)
                .toList();
    }
    @Override
    @Transactional
    public void updateMenuItems(List<Menu> menuList){
        if (menuList == null || menuList.isEmpty()) {
            return;
        }
        menuList.forEach(menu -> {
            Optional<Menu> existingMenu = menuRepository.findById(menu.getId());
            if (existingMenu.isPresent()) {
                Menu updatedMenu = existingMenu.get();
                updatedMenu.setName(menu.getName());
                updatedMenu.setDescription(menu.getDescription());
                updatedMenu.setCount(menu.getCount());
                menuRepository.save(updatedMenu);
            }
        });
    }
}
