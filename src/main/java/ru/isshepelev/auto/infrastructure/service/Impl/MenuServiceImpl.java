package ru.isshepelev.auto.infrastructure.service.Impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;
import ru.isshepelev.auto.infrastructure.persistance.entity.MenuRevision;
import ru.isshepelev.auto.infrastructure.persistance.repository.MenuRepository;
import ru.isshepelev.auto.infrastructure.persistance.repository.MenuRevisionRepository;
import ru.isshepelev.auto.infrastructure.service.MenuService;
import ru.isshepelev.auto.infrastructure.service.dto.MenuDto;
import ru.isshepelev.auto.security.entity.User;
import ru.isshepelev.auto.security.repository.UserRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
@Data
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuRevisionRepository menuRevisionRepository;
    private final UserRepository userRepository; //TODO сделать сервис

    private final Map<String, Long> activeRevisions = new ConcurrentHashMap<>();

    @Override
    public List<Menu> getItems(int page, int pageSize) {
        Long activeRevisionId = activeRevisions.get(SecurityContextHolder.getContext().getAuthentication().getName());
        if (activeRevisionId == null) {
            return Collections.emptyList();
        }

        MenuRevision activeRevision = menuRevisionRepository.findById(activeRevisionId).orElse(null);
        if (activeRevision == null) {
            return Collections.emptyList();
        }
        List<Menu> revisionMenu = activeRevision.getRevision();
        int start = page * pageSize;
        int end = Math.min(start + pageSize, revisionMenu.size());
        return revisionMenu.subList(start, end);
    }


    @Override
    public List<Menu> getAllMenuItems() {
        return menuRepository.findAll();
    }


    @Override
    @Transactional
    public void createMenuItem(MenuDto menuDto, Long revisionId) {
        MenuRevision menuRevision = menuRevisionRepository.findById(revisionId)
                .orElseThrow(() -> {
                    log.error("Отсутствует ревизия с id {}", revisionId);
                    return new IllegalArgumentException("Ревизия с id " + revisionId + " не найдена");
                });

        Menu menu = new Menu();
        menu.setId(UUID.randomUUID());
        menu.setName(menuDto.getName());
        menu.setDescription(menuDto.getDescription());
        menu.setCount(menuDto.getCount());
        menuRevision.getRevision().add(menu);

        log.info("Добавление товара {} в ревизию №{}", menu, revisionId);
        menuRepository.save(menu);
        menuRevisionRepository.save(menuRevision);
    }


    @Override
    public void createNewMenu(List<MenuDto> menuDtoList) {
        List<Menu> menuList = new ArrayList<>();
        for (MenuDto dto : menuDtoList) {
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

        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }

        revision.setOwner(user);
        menuRepository.saveAll(menuList);
        menuRevisionRepository.save(revision);
    }


    @Override
    @Transactional
    public void updateMenuItem(UUID id, MenuDto menuDto) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Меню с id " + id + " не найдено"));

        menu.setName(menuDto.getName());
        menu.setDescription(menuDto.getDescription());
        menu.setCount(Math.max(menuDto.getCount(), 0));

        log.info("Изменение товара {}", menu);
        menuRepository.save(menu);
    }

    @Override
    @Transactional
    public void deleteMenuItem(UUID id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Меню с id " + id + " не найдено"));

        menuRevisionRepository.findAll().forEach(revision -> {
            if (revision.getRevision().removeIf(m -> m.getId().equals(id))) {
                menuRevisionRepository.save(revision);
            }
        });

        log.info("Удаление меню: {}", menu);
        menuRepository.delete(menu);
    }


    @Override
    public Menu getMenuById(UUID id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid menu ID: " + id));
    }


    @Override
    @Transactional
    public List<Menu> getItems(int page, int pageSize, Long revisionId) {
        MenuRevision currentRevision = Optional.ofNullable(revisionId)
                .flatMap(menuRevisionRepository::findById)
                .orElseGet(menuRevisionRepository::findTopByOrderByDateOfCreateDesc);

        if (currentRevision == null) {
            return Collections.emptyList();
        }

        List<Menu> revisionMenu = currentRevision.getRevision();
        int start = page * pageSize;
        int end = Math.min(start + pageSize, revisionMenu.size());
        return revisionMenu.subList(start, end);
    }


    @Override
    public List<MenuRevision> getAllRevisions() {
        return menuRevisionRepository.findByOwnerUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }


    @Override
    public List<Menu> getMenuFromRevision(Long revisionId) {
        Optional<MenuRevision> revisionOpt = menuRevisionRepository.findByIdAndOwnerUsername(revisionId, SecurityContextHolder.getContext().getAuthentication().getName());
        if (revisionOpt.isPresent()) {
            activeRevisions.put(SecurityContextHolder.getContext().getAuthentication().getName(), revisionId);
            return revisionOpt.get().getRevision();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Menu> getStopList() {
        return getAllRevisions().stream()
                .flatMap(revision -> revision.getRevision().stream())
                .filter(menu -> menu.getCount() == 0)
                .toList();
    }


    @Override
    @Transactional
    public void updateMenuItems(List<Menu> menuList) {
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
    public MenuRevision getRevisionById(Long revisionId) {
        return menuRevisionRepository.findByIdAndOwnerUsername(revisionId, SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid revision ID or unauthorized: " + revisionId));
    }
}
//TODO сделать отдельный метод для получения username SecurityContextHolder.getContext().getAuthentication().getName()