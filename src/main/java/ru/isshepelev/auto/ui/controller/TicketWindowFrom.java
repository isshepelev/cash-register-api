package ru.isshepelev.auto.ui.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;
import ru.isshepelev.auto.infrastructure.persistance.entity.MenuRevision;
import ru.isshepelev.auto.infrastructure.service.MenuService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TicketWindowFrom {

    private final MenuService menuService;
    @GetMapping("")
    public String ticket(Model model) {
        List<MenuRevision> revisions = menuService.getAllRevisions();
        model.addAttribute("revisions", revisions);
        model.addAttribute("currentPage", 0);
        return "ticket-window";
    }

    @GetMapping("/items/page/{page}")
    @ResponseBody
    public List<Menu> getItems(@PathVariable int page, @RequestParam(required = false) Long revisionId) {
        int pageSize = 5;
        if (revisionId != null) {
            return menuService.getMenuFromRevision(revisionId);
        } else {
            return menuService.getItems(page, pageSize);
        }
    }
}
