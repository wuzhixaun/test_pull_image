package com.wuzx.test_pull_image.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @GetMapping("/hello")
    public String getHello() {
        return "hello my world"; 
    }

    @GetMapping("/java")
    public String getJava() {
        return "java1";
    }

    @GetMapping("/go")
    public String getGo() {
        return "go1";
    }

    @GetMapping("/python")
    public String getPython() {
        return "python1";
    }
        
}
