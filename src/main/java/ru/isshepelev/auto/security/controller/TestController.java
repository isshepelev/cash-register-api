package ru.isshepelev.auto.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/admin")
public class TestController {

    @GetMapping("")
    public String userAccess(Principal principal){
        return principal.getName();
    }

}
