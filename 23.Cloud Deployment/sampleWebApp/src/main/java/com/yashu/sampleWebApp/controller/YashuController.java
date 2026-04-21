package com.yashu.sampleWebApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YashuController {
    @GetMapping("/")
    public String home(){
        return "Welcome to Yashu Domain";
    }

    @GetMapping("/info")
    public String getInfo(){
        return "Visit Yashu.com for courses information";
    }
}
