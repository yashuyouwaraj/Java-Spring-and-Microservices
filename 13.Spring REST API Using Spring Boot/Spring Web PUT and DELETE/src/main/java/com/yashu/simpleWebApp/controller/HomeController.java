package com.yashu.simpleWebApp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @RequestMapping("/")
    public  String greet(){
        return "Welcome to Yashu!!!";
    }

    @RequestMapping("/about")
    public String about(){
        return "I am a software engineer";
    }
}
