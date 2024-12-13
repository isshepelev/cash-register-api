package ru.isshepelev.auto.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;
import ru.isshepelev.auto.infrastructure.persistance.entity.MenuRevision;
import ru.isshepelev.auto.infrastructure.service.MenuService;
import ru.isshepelev.auto.infrastructure.service.dto.MenuDto;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;
    @GetMapping()
    public String showMenu(Model model) {
        List<MenuRevision> revisions = menuService.getAllRevisions();
        model.addAttribute("revisions", revisions);
        return "menu";
    }

    @GetMapping("/revision/{id}")
    public String showMenuItemsByRevision(@PathVariable Long id, Model model) {
        List<Menu> menuItems = menuService.getMenuFromRevision(id);
        List<MenuRevision> revisions = menuService.getAllRevisions();
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("revisions", revisions);
        model.addAttribute("selectedRevisionId", id);
        return "menu";
    }

    @GetMapping("/create")
    public String viewCreateMenu(){
        return "create-menu";
    }
    @PostMapping("/create")
    public ResponseEntity<Void> createNewMenu(@RequestBody List<MenuDto> menuDto) {
        menuService.createNewMenu(menuDto);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/"))
                .build();
    }

    @PostMapping("/add")
    public String addMenuItem(@RequestParam String name, @RequestParam String description, @RequestParam int count, @RequestParam Long revisionId) { //TODO дто не судьба получать да ?
        System.out.println(revisionId);
//        menuService.createMenuItem(new MenuDto(name, description, count));
        return "redirect:/menu";
    }

    @PostMapping("/delete/{id}")
    public String deleteMenuItem(@PathVariable UUID id) {
        menuService.deleteMenuItem(id);
        return "redirect:/menu";
    }
    @GetMapping("/edit/{id}")
    public String editMenuItemForm(@PathVariable UUID id, Model model){
        model.addAttribute("menuItems", menuService.getAllMenuItems());
        model.addAttribute("item" , menuService.findMenuById(id).get());
        return "edit-menu";
    }

    @PostMapping("/edit/{id}")
    public String editMenuItem(@PathVariable UUID id, @ModelAttribute MenuDto menuDto){
        menuService.updateMenuItem(id, menuDto);
        return "redirect:/menu";
    }
    @GetMapping("/stop-list")
    public String stopList(Model model){
        List<Menu> stopList = menuService.getStopList();
        model.addAttribute("stopList", stopList);
        return "stop-list";
    }

    @PostMapping("/stop-list")
    @ResponseBody
    public ResponseEntity<List<Menu>> stopList(){
        List<Menu> stopList = menuService.getAllMenuItems()
                .stream()
                .filter(e -> e.getCount() == 0)
                .toList();
        return ResponseEntity.ok().body(stopList);
    }
}
