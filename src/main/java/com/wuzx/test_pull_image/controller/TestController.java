package com.wuzx.test_pull_image.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @GetMapping("/hello")
    public String getHello() {
        return "hello1"; 
    }
        
}
