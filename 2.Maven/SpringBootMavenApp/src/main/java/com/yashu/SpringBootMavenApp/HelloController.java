package com.yashu.SpringBootMavenApp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/api/hello")
    public String apiHello() {
        return "Hello from REST API!";
    }
}