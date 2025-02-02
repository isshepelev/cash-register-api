package ru.isshepelev.auto.ui.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;
import ru.isshepelev.auto.infrastructure.persistance.entity.MenuRevision;
import ru.isshepelev.auto.infrastructure.service.MenuService;
import ru.isshepelev.auto.infrastructure.service.dto.MenuDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
public class MenuApiController {
    private final MenuService menuService;
//    @GetMapping("/{revisionId}/menu")
//    public ResponseEntity<List<Menu>> getMenuByRevisionId(@PathVariable Long revisionId){
//        return ResponseEntity.ok(menuService.getRevisionById(revisionId).getRevision());
//    }
    @GetMapping("/revisions")
    public ResponseEntity<List<MenuRevision>> getRevisions(){
        return ResponseEntity.ok(menuService.getAllRevisions());
    }

    @GetMapping("/revisions/{revisionId}")
    public ResponseEntity<MenuRevision> getRevisionById(@PathVariable Long revisionId){
        return ResponseEntity.ok(menuService.getRevisionById(revisionId));
    }

    @GetMapping("")
    public ResponseEntity<List<Menu>> getAllMenu(){
        return ResponseEntity.ok(menuService.getAllMenuItems());
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<Menu> getMenuById(@PathVariable UUID menuId){
        return ResponseEntity.ok(menuService.getMenuById(menuId));
    }
    @GetMapping("/stop-list")
    public ResponseEntity<List<Menu>> getStopList(){
        return ResponseEntity.ok(menuService.getStopList());
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createNewMenu(@RequestBody List<MenuDto> menuDto) {
        menuService.createNewMenu(menuDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/add-item/{revisionId}")
    public ResponseEntity<String> addItemForRevision(@PathVariable Long revisionId, @RequestBody MenuDto menuDto){
        try {
            menuService.createMenuItem(menuDto,revisionId);
            return ResponseEntity.ok().build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body("ревизия с id " + revisionId + " не найдена");
        }
    }
}
