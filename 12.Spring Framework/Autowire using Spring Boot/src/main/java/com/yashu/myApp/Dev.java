package com.yashu.myApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Dev {

    //field Injection
    @Autowired
    @Qualifier("laptop")
    private Computer comp;

    //constructor injection
//    Laptop laptop;
//    public Dev(Laptop laptop){
//        this.laptop = laptop;
//    }

    //setter Injection
//    Laptop laptop;
//    @Autowired
//    public void setLaptop(Laptop laptop){
//        this.laptop = laptop;
//    }


    public void build(){
        System.out.println("Working on Awesome Project");
        comp.compile();
    }
}
