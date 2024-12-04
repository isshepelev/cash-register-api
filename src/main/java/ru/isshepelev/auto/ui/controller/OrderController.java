package ru.isshepelev.auto.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.isshepelev.auto.infrastructure.service.MenuService;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final MenuService menuService;

    @PostMapping("/create")
    @ResponseBody
    public String createOrder(@RequestBody List<UUID> request) {
        System.out.println(request);

        return "{\"status\":\"success\"}";
    }

}
