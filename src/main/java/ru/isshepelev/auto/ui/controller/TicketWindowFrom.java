package ru.isshepelev.auto.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.isshepelev.auto.infrastructure.persistance.entity.Menu;
import ru.isshepelev.auto.infrastructure.service.MenuService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TicketWindowFrom {

    private final MenuService menuService;
    @GetMapping("")
    public String ticket(Model model) {
        model.addAttribute("items", menuService.getItems(0, 5));
        model.addAttribute("currentPage", 0);
        return "ticket-window";
    }

    @GetMapping("/items/page/{page}")
    @ResponseBody
    public List<Menu> getItems(@PathVariable int page) {
        int pageSize = 5;
        return menuService.getItems(page, pageSize);
    }
}
