package com.yashu.demo.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AlienResource {

    @GetMapping("aliens")
    public List<Alien> getAliens() {
        List<Alien> aliens = new ArrayList<>();
        
        Alien a1 = new Alien();
        a1.setId(101);
        a1.setName("Yashu");
        a1.setPoints(100);
        aliens.add(a1);

        Alien a2 = new Alien();
        a2.setId(102);
        a2.setName("John");
        a2.setPoints(200);
        aliens.add(a2); 

        return aliens;
    }
}
