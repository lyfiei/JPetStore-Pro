package com.csu.jpetstore.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainFormController {

    @GetMapping("/mainForm")
    public String main() {
        return "catalog/main";
    }
}

