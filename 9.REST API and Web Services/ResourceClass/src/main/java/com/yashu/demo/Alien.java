package com.yashu.demo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Alien{
    private String name;
    private String points;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}