package com.csu.jpetstore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class test {
    @GetMapping("/hello")
    public String hello() {
        return "Spring Boot";
    }
}
