package ru.isshepelev.auto.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TicketWindowFrom {
    @GetMapping("")
    public String ticket(){
        return "ticket-window";
    }
}
