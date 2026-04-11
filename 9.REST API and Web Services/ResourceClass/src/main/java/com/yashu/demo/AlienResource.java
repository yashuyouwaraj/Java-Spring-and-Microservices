package com.yashu.demo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("aliens")
public class AlienResource{

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Alien getAlien(){
        System.out.println("...getAlien() called...");
        Alien a= new Alien();
        a.setName("Yashu");
        a.setPoints("100");
        return a;
    }
}