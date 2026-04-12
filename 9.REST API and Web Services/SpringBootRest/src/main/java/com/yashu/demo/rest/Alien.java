package com.yashu.demo.rest;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Alien{
    private String name;
    private int points;
    private int Id;

  

    

    @Override
    public String toString() {
        return "Alien [name=" + name + ", points=" + points + ", Id=" + Id + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }
}