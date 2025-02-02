package ru.isshepelev.auto.infrastructure.service.Impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;
import ru.isshepelev.auto.infrastructure.persistance.entity.MenuRevision;
import ru.isshepelev.auto.infrastructure.persistance.repository.MenuRepositroty;
import ru.isshepelev.auto.infrastructure.persistance.repository.MenuRevisionRepository;
import ru.isshepelev.auto.infrastructure.service.MenuService;
import ru.isshepelev.auto.infrastructure.service.dto.MenuDto;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final MenuRepositroty menuRepository;
    private final MenuRevisionRepository menuRevisionRepository;
    private MenuRevision activeRevision;
    @Override
    public MenuRevision getActiveRevision() {
        return activeRevision;
    }
    @Override
    public void setActiveRevision(MenuRevision revision) {
        this.activeRevision = revision;
    }
    @Override
    public List<Menu> getItems(int page, int pageSize) {
        if (activeRevision != null) {
            List<Menu> revisionMenu = activeRevision.getRevision();
            int start = page * pageSize;
            int end = Math.min((start + pageSize), revisionMenu.size());
            return revisionMenu.subList(start, end);
        } else {
            return Collections.emptyList();
        }
    }


    @Override
    public List<Menu> getAllMenuItems() {
        return menuRepository.findAll();
    }

    @Override
    @Transactional
    public void createMenuItem(MenuDto menuDto, Long revisionId) {
        MenuRevision menuRevision = menuRevisionRepository.findById(revisionId).get();
        Menu menu = new Menu();
        menu.setId(UUID.randomUUID());
        menu.setName(menuDto.getName());
        menu.setDescription(menuDto.getDescription());
        menu.setCount(menuDto.getCount());
        menuRevision.getRevision().add(menu);
        log.info("добавление товара{} ", menu);
        menuRepository.save(menu);
        menuRevisionRepository.save(menuRevision);

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
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Меню с id " + id + " не найдено"));
        List<MenuRevision> menuRevisions = menuRevisionRepository.findAll();

        for (MenuRevision menuRevision : menuRevisions) {
            if (menuRevision.getRevision().removeIf(m -> m.getId().equals(id))) {
                menuRevisionRepository.save(menuRevision);
            }
        }
        log.info("Удаление меню: {}", menu);
        menuRepository.delete(menu);
    }
    @Override
    public Menu getMenuById(UUID id){
        return menuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid menu ID: " + id));
    }
    @Override
    @Transactional
    public List<Menu> getItems(int page, int pageSize, Long revisionId) {
        MenuRevision currentRevision = null;

        if (revisionId != null) {
            Optional<MenuRevision> revisionOptional = menuRevisionRepository.findById(revisionId);
            if (revisionOptional.isPresent()) {
                currentRevision = revisionOptional.get();
            }
        } else {
            currentRevision = menuRevisionRepository.findTopByOrderByDateOfCreateDesc();
        }

        if (currentRevision != null) {
            List<Menu> revisionMenu = currentRevision.getRevision();
            int start = page * pageSize;
            int end = Math.min(start + pageSize, revisionMenu.size());
            return revisionMenu.subList(start, end);
        } else {
            return Collections.emptyList();
        }
    }
    @Override
    public List<MenuRevision> getAllRevisions() {
        return menuRevisionRepository.findAll();
    }
    @Override
    public List<Menu> getMenuFromRevision(Long revisionId) {
        Optional<MenuRevision> revisionOptional = menuRevisionRepository.findById(revisionId);
        if (revisionOptional.isPresent()) {
            setActiveRevision(revisionOptional.get());
            return revisionOptional.get().getRevision();
        }
        return Collections.emptyList();
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
    @Override
    public MenuRevision getRevisionById(Long revisionId){
        return menuRevisionRepository.findById(revisionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid revision ID: " + revisionId));
    }
}
